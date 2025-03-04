package com.example.carsparts.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.carsparts.domain.entity.PartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartDao {

    @Query("SELECT * FROM part WHERE carId = :carId")
    fun getPartsForCar(carId: Int): Flow<List<PartEntity>>

    @Query("SELECT * FROM part WHERE id = :partId LIMIT 1")
    fun getPartById(partId: Int): Flow<PartEntity?>

    @Insert
    suspend fun insertPart(part: PartEntity)

    @Update
    suspend fun updatePart(part: PartEntity)

    @Query("DELETE FROM part WHERE id = :partId")
    suspend fun deletePart(partId: Int)

    @Query("SELECT id FROM part")
    fun getAllPartIds(): Flow<List<Int>>
}