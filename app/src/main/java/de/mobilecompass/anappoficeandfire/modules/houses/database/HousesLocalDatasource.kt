package de.mobilecompass.anappoficeandfire.modules.houses.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import de.mobilecompass.anappoficeandfire.modules.houses.database.models.HouseDB
import de.mobilecompass.anappoficeandfire.modules.houses.database.models.HouseRemoteKeysDB

interface HousesLocalDatasource {

    suspend fun insertHousesAndRemoteKeys(houses: List<HouseDB>, remoteKeys: List<HouseRemoteKeysDB>)

    fun getHouse(url: String): LiveData<HouseDB?>

    suspend fun getHouses(): List<HouseDB>

    suspend fun getRemoteKeysByHouseId(id: Long): HouseRemoteKeysDB?

    suspend fun getHousesCount(): Int

    suspend fun deleteAll()

    fun pagingSource(): PagingSource<Int, HouseDB>
}