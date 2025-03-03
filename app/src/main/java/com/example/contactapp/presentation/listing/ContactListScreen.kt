package com.example.contactapp.presentation.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.contactapp.presentation.SharedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    onAction: (ContactAction) -> Unit,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    isMobile: Boolean
) {
    if (isMobile){
        sharedViewModel.resetSelectedId()
    } else {
        sharedViewModel.setIndexWithDetailContact()
    }
    val contacts = sharedViewModel.contactPagingFlow.collectAsLazyPagingItems()
    val selectedContact by sharedViewModel.selectedContactId.collectAsStateWithLifecycle()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Contacts",
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            //Drawer Open
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    contentPadding = PaddingValues(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(contacts.itemCount) { index  ->
                        val contact = contacts[index]
                        contact?.let {
                            ContactItemUI(
                                contact = contacts[index],
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selectedContact != it.id) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer)
                                    .clickable {
                                        if (isMobile) navController.navigate("contact_Detail")
                                        onAction(ContactAction.SelectContact(contacts[index]))
                                        sharedViewModel.setIndex(it.id)
                                    },
                                isSelected = selectedContact == it.id
                            )
                        }
                    }
                    item {
                        if (contacts.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator(modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 8.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}


