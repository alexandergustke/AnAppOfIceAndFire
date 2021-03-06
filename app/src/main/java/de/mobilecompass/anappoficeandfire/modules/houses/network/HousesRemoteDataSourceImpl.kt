package de.mobilecompass.anappoficeandfire.modules.houses.network

import de.mobilecompass.anappoficeandfire.modules.houses.network.models.HousesDataDTO
import de.mobilecompass.anappoficeandfire.modules.houses.network.models.HousesRemoteException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import timber.log.Timber
import javax.inject.Inject

class HousesRemoteDataSourceImpl @Inject constructor(
    private val housesApi: HousesApi
) : HousesRemoteDataSource {
    companion object {
        val LOG_TAG: String = HousesRemoteDataSourceImpl::class.java.simpleName
    }

    override suspend fun getHouses(url: String): Result<HousesDataDTO> =
        withContext(Dispatchers.IO) {
            Timber.i("Fetch content at $url")

            val housesResponse = try {
                housesApi.getHousesByURL(url)
            } catch (exception: Exception) {
                return@withContext Result.failure(HousesRemoteException(exception.localizedMessage))
            }

            val housesList = housesResponse.body()

            if (!housesResponse.isSuccessful || housesList.isNullOrEmpty()) {
                val errorMessage = housesResponse.message() ?: "Unknown error"
                Timber.e("Error while fetching houses for url: $errorMessage")
                return@withContext Result.failure(HousesRemoteException(housesResponse.message()))
            }

            val headers = housesResponse.headers()
            val linkHeaderEntries = getLinkHeaderValues(headers)

            val previousUrl = linkHeaderEntries?.firstNotNullOfOrNull {
                if (it.key == "prev") it.value else null
            }

            val nextUrl = linkHeaderEntries?.firstNotNullOfOrNull {
                if (it.key == "next") it.value else null
            }

            Timber.d("Previous url: $previousUrl")
            Timber.d("Next url: $nextUrl")
            Timber.d("Houses count: ${housesList.size}")

            Result.success(HousesDataDTO(housesList, previousUrl, nextUrl))
        }

    /**
     * Tries to parse the link header and map the entries into a map.
     * This map should consist of the "prev", "next", "current" keys with their
     * corresponding values.
     *
     * @param headers the headers of the response
     *
     * @return the parsed map of the link header
     */
    private fun getLinkHeaderValues(headers: Headers): Map<String, String>? {
        val linkHeader = headers["link"]
        val linkHeaderRegExString = ".*<(.+)>;.*rel=\"(.+)\".*"
        val linkHeaderRegEx = Regex(linkHeaderRegExString)
        val linkHeaderEntries = linkHeader?.split(",")
        val parsedLinkHeaderEntries = linkHeaderEntries
            ?.map {
                val match = linkHeaderRegEx.find(it) ?: return@map null
                val (url, linkType) = match.destructured
                linkType to url
            }
            ?.filterNotNull()
            ?.toMap()

        return parsedLinkHeaderEntries
    }

}