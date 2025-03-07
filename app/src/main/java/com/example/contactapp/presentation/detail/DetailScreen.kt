package com.example.contactapp.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.contactapp.R
import com.example.contactapp.presentation.SharedViewModel
import com.example.contactapp.presentation.add_edit.AddEditScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    isMobile: Boolean
) {
    val detailViewState by sharedViewModel.detailState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (isMobile) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                                sharedViewModel.resetSelectedId()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    }
                    if (!isMobile && detailViewState.selectedContact != null) {
                        IconButton(
                            onClick = {
                                sharedViewModel.resetSelectedContact()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = ""
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isMobile){
                                val isCreate = false
                                navController.navigate("add_contact/$isCreate")
                            }

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = ""
                        )
                    }
                    IconButton(
                        onClick = {
                            sharedViewModel.onDetailAction(
                                DetailAction.DeleteContact(
                                    detailViewState.selectedContact
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = ""
                        )
                    }

                },
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = { },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isMobile && detailViewState.selectedContact == null && detailViewState.selectedPhoneContact == null) {
                Text(
                    text = "Select a contact",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        model = if (detailViewState.isPhoneContact == true) detailViewState.selectedPhoneContact?.picture
                            ?: R.drawable.placeholder_contact else detailViewState.selectedContact?.picture
                            ?: R.drawable.placeholder_contact,
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(150.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        fontSize = 22.sp,
                        text = if (detailViewState.isPhoneContact == true)
                            "${detailViewState.selectedPhoneContact?.displayName}"
                        else
                            "${detailViewState.selectedContact?.firstName} ${detailViewState.selectedContact?.secondName}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Column {
                            Text(
                                text = "Contact info",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(12.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            ContactInfoRow(
                                icon = Icons.Filled.Call,
                                text = (if (detailViewState.isPhoneContact == true) detailViewState.selectedPhoneContact?.phoneNumber else detailViewState.selectedContact?.cell)
                                    ?: "-",
                                description = "mobile"
                            )
                            if (detailViewState.isPhoneContact == false) {
                                ContactInfoRow(
                                    icon = Icons.Filled.Email,
                                    text = detailViewState.selectedContact?.email
                                        ?: "-",
                                    description = "email"
                                )
                                ContactInfoRow(
                                    icon = Icons.Filled.Person,
                                    text = detailViewState.selectedContact?.gender
                                        ?: "-",
                                    description = "gender"
                                )
                            }
                        }
                    }
                    //TODO
                }
            }
        }
    }
}

@Composable
fun ContactInfoRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = "")
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = description,
                fontWeight = FontWeight.W300
            )
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
}
