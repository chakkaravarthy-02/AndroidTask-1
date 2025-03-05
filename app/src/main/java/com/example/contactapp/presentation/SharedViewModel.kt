package com.example.contactapp.presentation

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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


    private val _contactViewState = MutableStateFlow(ContactViewState())
    var contactViewState: StateFlow<ContactViewState> = _contactViewState

    private val _detailState = MutableStateFlow(DetailViewState())
    val detailState : StateFlow<DetailViewState> = _detailState


    fun onContactAction(contactAction: ContactAction) {
        when (contactAction) {
            is ContactAction.SelectContact -> {
                viewModelScope.launch {
                    _detailState.value.selectedContact = contactAction.contact
                    delay(3000L)
                }
            }
        }
    }

    fun onDetailAction(detailAction: DetailAction){
        when(detailAction){
            is DetailAction.DeleteContact -> {
                //TODO()
            }
        }
    }

    fun resetSelectedId() {
        _contactViewState.value.selectedContactId = null
    }

    fun resetSelectedContact(){
        _detailState.value.selectedContact = null
        _contactViewState.value.selectedContactId = null
    }

    fun setIndex(id: Int) {
        _contactViewState.value.selectedContactId = id
    }

    fun setIndexWithDetailContact(){
        _contactViewState.value.selectedContactId = _detailState.value.selectedContact?.id
    }
}