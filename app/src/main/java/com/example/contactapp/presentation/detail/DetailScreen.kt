package com.example.contactapp.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.contactapp.domain.Contact
import com.example.contactapp.presentation.listing.ContactListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    contact: Contact?,
    detailViewModel: DetailViewModel,
    navController: NavController,
    contactListViewModel: ContactListViewModel,
    isMobile: Boolean
) {

    //val state by detailViewModel.state.collectAsState()
    val selectedContact by contactListViewModel.selectedContactForExpandableScreen.collectAsState()
    val scrollState = rememberScrollState()


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (isMobile) {
                        IconButton(
                            onClick = {
                                navController.navigate("contact_list_screen") {
                                    popUpTo("contact_list_screen") {
                                        inclusive = true
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    }
                },
                title = {
                },
                modifier = Modifier,
                actions = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )
        },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {},
        snackbarHost = {},
        floatingActionButton = {}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isMobile && selectedContact == null) {
                // Show "Select a contact" message when no contact is selected
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
                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        model = if (isMobile) contact?.picture else selectedContact?.picture,
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(150.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        fontSize = 22.sp,
                        text = if (isMobile) "${contact?.firstName} ${contact?.secondName}" else "${selectedContact?.firstName} ${selectedContact?.secondName}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceDim,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
                                text = (if (isMobile) contact?.cell else selectedContact?.cell)
                                    ?: ""
                            )
                            ContactInfoRow(
                                icon = Icons.Filled.Email,
                                text = (if (isMobile) contact?.email else selectedContact?.email)
                                    ?: ""
                            )
                            ContactInfoRow(
                                icon = Icons.Filled.Person,
                                text = (if (isMobile) contact?.gender else selectedContact?.gender)
                                    ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactInfoRow(modifier: Modifier = Modifier, icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = "")
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Normal
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
}
