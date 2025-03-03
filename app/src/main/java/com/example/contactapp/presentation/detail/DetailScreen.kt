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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.contactapp.presentation.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    isMobile: Boolean
) {
    val selectedContact by sharedViewModel.selectedContactForExpandableScreen.collectAsState()
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
                    if (!isMobile && selectedContact != null) {
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
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                title = {  },
            )
        },
        modifier = Modifier.fillMaxSize(),
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
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        model = if (isMobile) selectedContact?.picture else selectedContact?.picture,
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(150.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        fontSize = 22.sp,
                        text = if (isMobile) "${selectedContact?.firstName} ${selectedContact?.secondName}" else "${selectedContact?.firstName} ${selectedContact?.secondName}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                                text = (if (isMobile) selectedContact?.cell else selectedContact?.cell)
                                    ?: "-",
                                description = "mobile"
                            )
                            ContactInfoRow(
                                icon = Icons.Filled.Email,
                                text = (if (isMobile) selectedContact?.email else selectedContact?.email)
                                    ?: "-",
                                description = "email"
                            )
                            ContactInfoRow(
                                icon = Icons.Filled.Person,
                                text = (if (isMobile) selectedContact?.gender else selectedContact?.gender)
                                    ?: "-",
                                description = "gender"
                            )
                        }
                    }
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
                fontWeight = FontWeight.Thin
            )
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
}
