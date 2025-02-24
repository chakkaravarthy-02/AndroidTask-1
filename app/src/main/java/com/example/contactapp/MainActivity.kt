package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.ContactAppTheme
import com.example.contactapp.data.contactdb.getDatabase
import com.example.contactapp.data.network.ContactNetwork.contactNet
import com.example.contactapp.domain.Contact
import com.example.contactapp.domain.ContactRepository
import com.example.contactapp.presentation.detail.DetailScreen
import com.example.contactapp.presentation.detail.DetailViewModel
import com.example.contactapp.presentation.detail.DetailViewModelFactory
import com.example.contactapp.presentation.listing.ContactListScreen
import com.example.contactapp.presentation.listing.ContactListViewModel
import com.example.contactapp.presentation.listing.ContactViewModelFactory
import com.example.contactapp.util.WindowInfo
import com.example.contactapp.util.rememberWindowInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = getDatabase(applicationContext)
        val apiService = contactNet
        val repository = ContactRepository(
            apiService, db
        )
        setContent {
            ContactAppTheme {
                val contactListViewModel: ContactListViewModel = viewModel(
                    factory = ContactViewModelFactory(repository)
                )

                val detailViewModel: DetailViewModel = viewModel(
                    factory = DetailViewModelFactory(repository)
                )

                val navController = rememberNavController()

                val windowInfo = rememberWindowInfo()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact){
                        NavHost(navController, startDestination = "contact_list_screen") {
                            composable("contact_list_screen") {
                                ContactListScreen(
                                    contactListViewModel = contactListViewModel,
                                    onAction = contactListViewModel::onAction,
                                    navController = navController,
                                    isMobile = true
                                )
                            }
                            composable(
                                "contact_detail/{contact}"
                            ) { backStackEntry ->
                                val contactJson = URLDecoder.decode(
                                    backStackEntry.arguments?.getString("contact"),
                                    "utf-8"
                                )
                                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                                val jsonAdapter = moshi.adapter(Contact::class.java).lenient()
                                val contactObject = jsonAdapter.fromJson(contactJson)
                                DetailScreen(
                                    contact = contactObject,
                                    detailViewModel = detailViewModel,
                                    navController = navController,
                                    contactListViewModel = contactListViewModel,
                                    isMobile = true
                                )
                            }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier.weight(1f)) {
                                ContactListScreen(
                                    contactListViewModel = contactListViewModel,
                                    onAction = contactListViewModel::onAction,
                                    navController = navController,
                                    isMobile = false
                                )
                            }
                            Box(modifier = Modifier.weight(2f)) {
                                DetailScreen(
                                    contactListViewModel = contactListViewModel,
                                    contact = null,
                                    detailViewModel = detailViewModel,
                                    navController = navController,
                                    isMobile = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
