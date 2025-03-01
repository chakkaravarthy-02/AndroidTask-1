package com.example.contactapp.presentation.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.contactapp.data.network.toContact
import com.example.contactapp.domain.Contact
import com.example.contactapp.domain.ContactRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactListViewModel(
    repository: ContactRepository
) : ViewModel() {

    val contactPagingFlow = repository.getContacts()
        .flow
        .map { pagingData ->
            pagingData.map { it.toContact() }
        }
        .cachedIn(viewModelScope)

    private val _state = MutableStateFlow(ContactState())
    val state = _state.asStateFlow()

    private val _selectedContactForExpandableScreen = MutableStateFlow<Contact?>(null)
    val selectedContactForExpandableScreen: StateFlow<Contact?> = _selectedContactForExpandableScreen

    fun onAction(listingAction: ContactAction) {
        when (listingAction) {
            is ContactAction.SelectContact -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isSelectedContact = true,
                            selectedContact = listingAction.contact
                        )
                    }
                    _selectedContactForExpandableScreen.value = listingAction.contact
                    delay(3000L)
                }
            }
        }
    }

    fun resetSelectedState() {
        _state.update {
            it.copy(
                isSelectedContact = false,
                selectedContact = null
            )
        }
        resetSelectedContact()
    }

    fun resetSelectedContact(){
        _selectedContactForExpandableScreen.value = null
    }
}