package com.picapica.app.viewmodel

import androidx.lifecycle.*
import com.picapica.app.data.Shift
import com.picapica.app.data.ShiftDao
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Duration

class PicaPicaViewModel(private val shiftDao: ShiftDao) : ViewModel() {

    private val _timerText = MutableStateFlow("00:00")
    val timerText: StateFlow<String> = _timerText

    private val _intervalText = MutableStateFlow("00:00 - 00:00")
    val intervalText: StateFlow<String> = _intervalText

    private val _isPiqueActive = MutableStateFlow(false)
    val isPiqueActive: StateFlow<Boolean> = _isPiqueActive

    private val _isStopped = MutableStateFlow(false)
    val isStopped: StateFlow<Boolean> = _isStopped

    private var startTime: LocalDateTime? = null
    private var timerJob: Job? = null

    fun togglePique() {
        if (_isPiqueActive.value) {
            stopPique()
        } else {
            if (_isStopped.value) {
                reset()
            }
            startPique()
        }
    }

    fun stopIfActive() {
        if (_isPiqueActive.value) {
            stopPique()
        }
    }

    private fun startPique() {
        startTime = LocalDateTime.now()
        _isPiqueActive.value = true
        _isStopped.value = false
        _intervalText.value = "${formatTime(startTime!!)} - 00:00"
        _timerText.value = "00:00"
        
        timerJob = viewModelScope.launch {
            while (true) {
                val now = LocalDateTime.now()
                val duration = Duration.between(startTime, now)
                _timerText.value = formatDuration(duration)
                delay(1000)
            }
        }
    }

    private fun stopPique() {
        timerJob?.cancel()
        val endTime = LocalDateTime.now()
        _isPiqueActive.value = false
        _isStopped.value = true
        _intervalText.value = "${formatTime(startTime!!)} - ${formatTime(endTime)}"
        
        val duration = Duration.between(startTime, endTime)
        
        viewModelScope.launch {
            // Logic to keep only one per day could be added here
            shiftDao.insertShift(
                Shift(
                    date = startTime!!,
                    startTime = startTime!!,
                    endTime = endTime,
                    durationMillis = duration.toMillis()
                )
            )
        }
    }

    fun reset() {
        timerJob?.cancel()
        _isPiqueActive.value = false
        _isStopped.value = false
        _timerText.value = "00:00"
        _intervalText.value = "00:00 - 00:00"
        startTime = null
    }

    private fun formatTime(time: LocalDateTime): String {
        return "%02d:%02d".format(time.hour, time.minute)
    }

    private fun formatDuration(duration: Duration): String {
        val hours = duration.toHours()
        val minutes = (duration.toMinutes() % 60)
        val seconds = (duration.seconds % 60)
        return if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }

    private val _weeklyHistory = MutableStateFlow<Map<String, String>>(emptyMap())
    val weeklyHistory: StateFlow<Map<String, String>> = _weeklyHistory

    init {
        loadWeeklyHistory()
    }

    private fun loadWeeklyHistory() {
        viewModelScope.launch {
            val monday = getStartOfWeek()
            shiftDao.getRecentShifts(monday).collect { shifts ->
                // Group by day and keep latest
                val consolidated = shifts
                    .groupBy { it.date.dayOfWeek.name } // Use DayOfWeek name
                    .mapValues { (_, dayShifts) ->
                        val latest = dayShifts.first()
                        "${formatTime(latest.startTime)} - ${formatTime(latest.endTime ?: latest.startTime)}"
                    }
                
                _weeklyHistory.value = consolidated
            }
        }
    }

    private fun getStartOfWeek(): LocalDateTime {
        val now = LocalDateTime.now()
        // Reset to Monday at 00:00
        return now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
    }
}

