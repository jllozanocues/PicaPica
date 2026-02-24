package com.picapica.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "shifts")
data class Shift(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDateTime,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val durationMillis: Long = 0
)

