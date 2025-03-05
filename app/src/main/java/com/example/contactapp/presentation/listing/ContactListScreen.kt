package com.example.contactapp.presentation.listing

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.contactapp.presentation.SharedViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    onAction: (ContactAction) -> Unit,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    isMobile: Boolean
) {
    if (isMobile) {
        sharedViewModel.resetSelectedId()
    } else {
        sharedViewModel.setIndexWithDetailContact()
    }

    val apiContacts = sharedViewModel.contactPagingFlow.collectAsLazyPagingItems()
    val phoneContacts = sharedViewModel.phoneContactPagingFlow.collectAsLazyPagingItems()

    val contactState by sharedViewModel.contactViewState.collectAsStateWithLifecycle()

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
        },
        floatingActionButton = {
            if (selectedIndex == 0) {
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
                            //TODO
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (apiContacts.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
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
                                val sortedContacts =
                                    phoneContacts.itemSnapshotList.items.sortedBy { it.firstName }
                                val groupedContacts = sortedContacts.groupBy {
                                    it.firstName.firstOrNull()?.uppercase() ?: "#"
                                }
                                groupedContacts.forEach { (letter, names) ->
                                    stickyHeader { SectionHeader(letter) }
                                    items(names) { name ->
                                        name.let {
                                            ContactItemUI(
                                                contact = name,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 32.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(if (contactState.selectedContactId != it.id) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer)
                                                    .clickable {
                                                        if (isMobile) navController.navigate("contact_Detail")
                                                        onAction(
                                                            ContactAction.SelectContact(
                                                                name
                                                            )
                                                        )
                                                        if (!isMobile) sharedViewModel.setIndex(it.id)
                                                    },
                                                isSelected = contactState.selectedContactId == it.id
                                            )
                                        }
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }

                            1 -> {
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
                                                    onAction(ContactAction.SelectContact(apiContacts[index]))
                                                    if (!isMobile) sharedViewModel.setIndex(it.id)
                                                },
                                            isSelected = contactState.selectedContactId == it.id
                                        )
                                    }
                                }
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

