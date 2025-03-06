package com.example.contactapp.presentation

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.contactapp.data.network.toContact
import com.example.contactapp.domain.ContactRepository
import com.example.contactapp.presentation.detail.DetailAction
import com.example.contactapp.presentation.detail.DetailViewState
import com.example.contactapp.presentation.listing.ContactAction
import com.example.contactapp.presentation.listing.ContactViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    fun loadPhoneContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setAllPhoneContactsIntoDatabase()
            val response = repository.getAllPhoneContacts()
            withContext(Dispatchers.Main) {
                _contactViewState.value = _contactViewState.value.copy(
                    phoneContactsList = response
                )
            }
        }
    }

    val contactPagingFlow = repository.getContacts()
        .flow
        .map { pagingData ->
            pagingData.map { it.toContact() }
        }
        .cachedIn(viewModelScope)


    private val _contactViewState = MutableStateFlow(ContactViewState())
    var contactViewState: StateFlow<ContactViewState> = _contactViewState

    private val _detailState = MutableStateFlow(DetailViewState())
    val detailState: StateFlow<DetailViewState> = _detailState

    fun onContactAction(contactAction: ContactAction) {
        when (contactAction) {

            is ContactAction.SelectContactInApi -> {
                viewModelScope.launch {
                    _detailState.value = _detailState.value.copy(
                        selectedContact = contactAction.contact,
                        selectedPhoneContact = null
                    )
                    delay(3000L)
                }
            }

            is ContactAction.SelectContactInPhone -> {
                viewModelScope.launch {
                    _detailState.value = _detailState.value.copy(
                        selectedPhoneContact = contactAction.contact,
                        selectedContact = null
                    )
                    delay(3000L)
                }
            }
        }
    }

    fun onDetailAction(detailAction: DetailAction) {
        when (detailAction) {
            is DetailAction.DeleteContact -> {
                //TODO()
            }
        }
    }

    fun resetSelectedId() {
        _contactViewState.value = _contactViewState.value.copy(
            selectedContactId = null,
            selectedPhoneContactId = null
        )
    }

    fun resetSelectedContact() {
        _detailState.value.selectedContact = null
        _detailState.value.selectedPhoneContact = null
        _contactViewState.value = _contactViewState.value.copy(
            selectedContactId = null,
            selectedPhoneContactId = null
        )
    }

    fun setIndex(id: Int) {
        _contactViewState.value = _contactViewState.value.copy(
            selectedContactId = id
        )
    }

    fun setIndexWithDetailContact() {
        _contactViewState.value = _contactViewState.value.copy(
            selectedContactId = _detailState.value.selectedContact?.id
        )
    }

    fun setIndexForPhone(id: String) {
        _contactViewState.value = _contactViewState.value.copy(
            selectedPhoneContactId = id
        )
    }

    fun setIndexForPhoneWithDetailContact() {
        _contactViewState.value = _contactViewState.value.copy(
            selectedPhoneContactId = _detailState.value.selectedPhoneContact?.id
        )
    }

    fun isPhoneContactsOrApi(value: Boolean) {
        _detailState.value = _detailState.value.copy(
            isPhoneContact = value
        )
    }
}