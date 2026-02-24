package com.picapica.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ShiftDao {
    @Query("SELECT * FROM shifts WHERE date >= :sinceDate ORDER BY date DESC")
    fun getRecentShifts(sinceDate: LocalDateTime): Flow<List<Shift>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShift(shift: Shift)

    @Query("DELETE FROM shifts WHERE date < :olderThan")
    suspend fun deleteOldShifts(olderThan: LocalDateTime)

    @Query("SELECT * FROM shifts ORDER BY id DESC LIMIT 1")
    suspend fun getLastShift(): Shift?
}

