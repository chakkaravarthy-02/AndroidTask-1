package com.example.contactapp.presentation.listing

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.contactapp.domain.PhoneContact
import com.example.contactapp.presentation.SharedViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    onAction: (ContactAction) -> Unit,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    isMobile: Boolean,
    onChangeScreenToAdd: () -> Unit,
) {
    if (isMobile) {
        sharedViewModel.resetSelectedId()
    } else {
        sharedViewModel.setIndexWithDetailContact()
        sharedViewModel.setIndexForPhoneWithDetailContact()
    }

    val permissionGranted = rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readGranted = permissions[Manifest.permission.READ_CONTACTS] ?: false
        val writeGranted = permissions[Manifest.permission.WRITE_CONTACTS] ?: false

        if (readGranted && writeGranted) {
            permissionGranted.value = true
        } else {
            permissionGranted.value = false
        }
    }

    val focusRequester = remember {
        FocusRequester()
    }
    //search variables
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchList by rememberSaveable { mutableStateOf(emptyList<PhoneContact?>()) }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            delay(200)
            focusRequester.requestFocus()
        }
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            )
        )
        delay(500)
        if (permissionGranted.value) {
            sharedViewModel.loadPhoneContacts()
        }
    }


    val apiContacts = sharedViewModel.contactPagingFlow.collectAsLazyPagingItems()
    val contactState by sharedViewModel.contactViewState.collectAsStateWithLifecycle()
    val groupedContacts by remember {
        derivedStateOf {
            contactState.phoneContactsList.groupBy {
                it.displayName.firstOrNull()?.uppercase() ?: "#"
            }
        }
    }

    //tab row variables
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    var showTabRow by remember {
        mutableStateOf(true)
    }
    val tabList = listOf("Personal Contacts", "Api Contacts")

    //scrolling position variables
    val listState = rememberLazyListState()
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val firstVisibleItemOffset by remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }

    // Track scroll direction
    var prevIndex by remember { mutableIntStateOf(0) }
    var prevOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(firstVisibleItemIndex, firstVisibleItemOffset) {
        val currentIndex = listState.firstVisibleItemIndex
        val currentOffset = listState.firstVisibleItemScrollOffset

        showTabRow = if (currentIndex > prevIndex) {
            false
        } else if (currentIndex == prevIndex) {
            currentOffset <= prevOffset
        } else {
            true
        }

        prevIndex = currentIndex
        prevOffset = currentOffset
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching) {
                        showTabRow = false
                        TextField(
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search Contacts") },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 18.sp)
                        )
                    } else {
                        Text(
                            text = "My Contacts",
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    if (isSearching) {
                        IconButton(onClick = {
                            isSearching = false
                            searchQuery = ""
                            showTabRow = true
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (!isSearching && selectedIndex == 0) {
                        IconButton(
                            onClick = {
                                //Search
                                isSearching = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showTabRow,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if (isMobile) {
                            val isCreate = true
                            navController.navigate("add_contact/$isCreate")
                        } else {
                            onChangeScreenToAdd()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (apiContacts.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = showTabRow,
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it })
                    ) {
                        TabRow(
                            selectedTabIndex = selectedIndex,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            tabList.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedIndex == index,
                                    onClick = { selectedIndex = index },
                                    text = { Text(title) }
                                )
                            }
                        }
                    }
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (selectedIndex) {
                            0 -> {
                                sharedViewModel.isPhoneContactsOrApi(true)
                                if (permissionGranted.value) {
                                    if (!isSearching) {
                                        groupedContacts.forEach { (letter, names) ->
                                            stickyHeader { SectionHeader(letter) }
                                            items(names) { name ->
                                                name.let {
                                                    PhoneContactItemUI(
                                                        phoneContact = name,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 32.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(if (contactState.selectedPhoneContactId != it.id) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer)
                                                            .clickable {
                                                                if (isMobile) navController.navigate(
                                                                    "contact_Detail"
                                                                )
                                                                onAction(
                                                                    ContactAction.SelectContactInPhone(
                                                                        name
                                                                    )
                                                                )
                                                                if (!isMobile) sharedViewModel.setIndexForPhone(
                                                                    it.id
                                                                )
                                                            },
                                                        isSelected = contactState.selectedPhoneContactId == it.id
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        searchList = contactState.phoneContactsList.filter {
                                            it.displayName.contains(
                                                searchQuery,
                                                ignoreCase = true
                                            ) ||
                                                    it.phoneNumber?.contains(searchQuery) == true
                                        }
                                        items(searchList) {
                                            if (it != null) {
                                                PhoneContactItemUI(
                                                    phoneContact = it,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 32.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(if (contactState.selectedPhoneContactId != it.id) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer)
                                                        .clickable {
                                                            if (isMobile) navController.navigate(
                                                                "contact_Detail"
                                                            )
                                                            onAction(
                                                                ContactAction.SelectContactInPhone(
                                                                    it
                                                                )
                                                            )
                                                            if (!isMobile) sharedViewModel.setIndexForPhone(
                                                                it.id
                                                            )
                                                        },
                                                    isSelected = contactState.selectedPhoneContactId == it.id
                                                )
                                            } else {
                                                Text(
                                                    text = "Not found",
                                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                                )
                                            }
                                        }
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                } else {
                                    item {
                                        Box(modifier = Modifier.fillParentMaxSize()) {
                                            Text(
                                                text = "Please allow phone permission to \n     access contacts in \"Setting\"",
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }

                            1 -> {
                                sharedViewModel.isPhoneContactsOrApi(false)
                                items(apiContacts.itemCount) { index ->
                                    val contact = apiContacts[index]
                                    contact?.let {
                                        ContactItemUI(
                                            contact = apiContacts[index],
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (contactState.selectedContactId != it.id) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer)
                                                .clickable {
                                                    if (isMobile) navController.navigate("contact_Detail")
                                                    onAction(
                                                        ContactAction.SelectContactInApi(
                                                            apiContacts[index]
                                                        )
                                                    )
                                                    if (!isMobile) sharedViewModel.setIndex(it.id)
                                                },
                                            isSelected = contactState.selectedContactId == it.id
                                        )
                                    }
                                }
                                item {
                                    if (apiContacts.loadState.append is LoadState.Loading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(vertical = 8.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(letter: String) {
    Text(
        text = letter,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}


