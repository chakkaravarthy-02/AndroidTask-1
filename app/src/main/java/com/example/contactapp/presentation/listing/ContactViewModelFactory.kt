package com.example.contactapp.presentation.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contactapp.domain.ContactRepository

@Suppress("unchecked_cast")
class ContactViewModelFactory(
    private val repository: ContactRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ContactListViewModel::class.java)){
            return ContactListViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown class")
        }
    }
}