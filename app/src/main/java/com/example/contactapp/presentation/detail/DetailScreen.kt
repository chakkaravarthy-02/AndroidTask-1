package com.example.contactapp.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import com.example.contactapp.domain.Contact

@Composable
fun DetailScreen(
    contact: Contact?,
    detailViewModel: DetailViewModel
) {

    //val state by detailViewModel.state.collectAsState()

    if(contact != null){
        Column{
            Text(text = contact.firstName)
            AsyncImage(model = contact.picture, contentDescription = "")
    }
    } else {
        println("not yet")
    }
}