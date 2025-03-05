package com.example.contactapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.contactapp.data.network.toContact
import com.example.contactapp.domain.Contact
import com.example.contactapp.domain.ContactRepository
import com.example.contactapp.presentation.listing.ContactAction
import com.example.contactapp.presentation.listing.ContactState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedViewModel(
    repository: ContactRepository
) : ViewModel() {

    val phoneContactPagingFlow = repository.getContacts()
        .flow
        .map { pagingData ->
            pagingData.map { it.toContact() }
        }
        .cachedIn(viewModelScope)

    val contactPagingFlow = repository.getContacts()
        .flow
        .map { pagingData ->
            pagingData.map { it.toContact() }
        }
        .cachedIn(viewModelScope)


    private val _selectedContactId = MutableStateFlow<Int?>(null)
    var selectedContactId: StateFlow<Int?> = _selectedContactId

    private val _selectedContactForExpandableScreen = MutableStateFlow<Contact?>(null)
    val selectedContactForExpandableScreen: StateFlow<Contact?> = _selectedContactForExpandableScreen

    fun onAction(listingAction: ContactAction) {
        when (listingAction) {
            is ContactAction.SelectContact -> {
                viewModelScope.launch {
                    _selectedContactForExpandableScreen.value = listingAction.contact
                    delay(3000L)
                }
            }
        }
    }

    fun resetSelectedId() {
        _selectedContactId.value = null
    }

    fun resetSelectedContact(){
        _selectedContactForExpandableScreen.value = null
        _selectedContactId.value = null
    }

    fun setIndex(id: Int) {
        _selectedContactId.value = id
    }

    fun setIndexWithDetailContact(){
        _selectedContactId.value = _selectedContactForExpandableScreen.value?.id
    }
}