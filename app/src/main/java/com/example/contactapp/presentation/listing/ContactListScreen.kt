package com.example.contactapp.presentation.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.contactapp.domain.Contact
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.net.URLEncoder


@Composable
fun ContactListScreen(
    contacts: LazyPagingItems<Contact>,
    modifier: Modifier = Modifier,
    onAction: (ContactAction) -> Unit,
    navController: NavController,
    contactListViewModel: ContactListViewModel
) {

    val state by contactListViewModel.state.collectAsState()

    LaunchedEffect(state.selectedContact) {
        state.selectedContact?.let {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Contact::class.java).lenient()
            val contactJson = jsonAdapter.toJson(it)
            val encodedJson = URLEncoder.encode(contactJson,"utf-8")
            navController.navigate("contact_Detail/${encodedJson}")
        }
    }
    DisposableEffect(Unit){
        onDispose {
            contactListViewModel.resetSelectedState()
        }
    }
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (contacts.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "My Contacts",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(contacts.itemCount) { index ->
                        ContactItemUI(
                            contact = contacts[index],
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAction(ContactAction.SelectContact(contacts[index]))
                                }
                                .padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        if(contacts.loadState.append is LoadState.Loading){
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}