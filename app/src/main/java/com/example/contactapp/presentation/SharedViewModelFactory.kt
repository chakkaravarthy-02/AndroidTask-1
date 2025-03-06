package com.example.contactapp.presentation

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contactapp.domain.ContactRepository

@Suppress("unchecked_cast")
class SharedViewModelFactory(
    private val repository: ContactRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SharedViewModel::class.java)){
            return SharedViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown class")
        }
    }
}