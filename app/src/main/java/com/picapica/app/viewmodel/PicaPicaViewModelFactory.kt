package com.picapica.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.picapica.app.data.ShiftDao

class PicaPicaViewModelFactory(private val shiftDao: ShiftDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PicaPicaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PicaPicaViewModel(shiftDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

