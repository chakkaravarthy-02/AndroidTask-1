package com.example.contactapp.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.contactapp.domain.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel(
    private val repository: ContactRepository
): ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()
}