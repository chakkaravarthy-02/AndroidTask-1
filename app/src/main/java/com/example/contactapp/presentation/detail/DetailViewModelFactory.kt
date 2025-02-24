package com.example.contactapp.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contactapp.domain.ContactRepository
import com.example.contactapp.presentation.listing.ContactListViewModel

@Suppress("unchecked_cast")
class DetailViewModelFactory(
    private val repository: ContactRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown class")
        }
    }
}