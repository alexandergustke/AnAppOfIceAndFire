package de.mobilecompass.anappoficeandfire.modules.houses.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.mobilecompass.anappoficeandfire.modules.houses.database.models.HouseDB

@JsonClass(generateAdapter = true)
data class HouseDTO(
    @Json(name = "url")
    val url: String,

    @Json(name = "name")
    val name: String = "",

    @Json(name = "region")
    val region: String = "",

    @Json(name = "coatOfArms")
    val coatOfArms: String = "",

    @Json(name = "words")
    val words: String = "",

    @Json(name = "titles")
    val titles: List<String> = listOf(),

    @Json(name = "seats")
    val seats: List<String> = listOf(),

    @Json(name = "currentLord")
    val currentLord: String = "",

    @Json(name = "heir")
    val heir: String = "",

    @Json(name = "overlord")
    val overlordUrl: String = "",

    @Json(name = "founded")
    val founded: String = "",

    @Json(name = "founder")
    val founderUrl: String = "",

    @Json(name = "diedOut")
    val diedOut: String = "",

    @Json(name = "ancestralWeapons")
    val ancestralWeapons: List<String> = listOf(),

    @Json(name = "cadetBranches")
    val cadetBranches: List<String> = listOf(),

    @Json(name = "swornMembers")
    val swornMembersUrls: List<String> = listOf()
)

fun HouseDTO.asHouseDB(): HouseDB {
    val id = url.split("/").last().toLong()
    return HouseDB(
        id = id,
        url = url,
        name = name,
        region = region,
        coatOfArms = coatOfArms,
        words = words,
        titles = titles,
        seats = seats,
        currentLord = currentLord,
        overlordUrl = overlordUrl,
        heir = heir,
        founded = founded,
        founderUrl = founderUrl,
        diedOut = diedOut,
        ancestralWeapons = ancestralWeapons,
        cadetBranches = cadetBranches,
        swornMembersUrls = swornMembersUrls
    )
}

fun List<HouseDTO>.asHousesDB() = map {
    it.asHouseDB()
}
