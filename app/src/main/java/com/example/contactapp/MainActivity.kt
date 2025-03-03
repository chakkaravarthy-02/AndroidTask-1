package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.compose.ContactAppTheme
import com.example.contactapp.data.contactdb.getDatabase
import com.example.contactapp.data.network.ContactNetwork.contactNet
import com.example.contactapp.domain.ContactRepository
import com.example.contactapp.presentation.SharedViewModel
import com.example.contactapp.presentation.detail.DetailScreen
import com.example.contactapp.presentation.listing.ContactListScreen
import com.example.contactapp.presentation.listing.ContactViewModelFactory
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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
                val windowInfoTracker =
                    remember { (WindowInfoTracker.getOrCreate(this@MainActivity)) }
                val foldingFeature = remember { mutableStateOf<FoldingFeature?>(null) }

                LaunchedEffect(Unit) {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        windowInfoTracker.windowLayoutInfo(this@MainActivity)
                            .collect { newLayoutInfo ->
                                foldingFeature.value = newLayoutInfo.displayFeatures
                                    .filterIsInstance<FoldingFeature>()
                                    .firstOrNull()
                            }
                    }
                }

                val sharedViewModel: SharedViewModel = viewModel(
                    factory = ContactViewModelFactory(repository)
                )

                val navController = rememberNavController()

                val foldFeature = isBookPosture(foldingFeature.value)
                val foldingFeatureLandscape = isFlatLandscape(foldingFeature.value)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (foldFeature) {
                        ExpandedScreen(
                            navController,
                            sharedViewModel,
                            1f,
                            1f,
                        )
                    } else if (foldingFeatureLandscape) {
                        ExpandedScreen(
                            navController,
                            sharedViewModel,
                            1f,
                            2f
                        )
                    } else {
                        CompactScreen(
                            navController,
                            sharedViewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalContracts::class)
private fun isBookPosture(foldingFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldingFeature != null) }
    return foldingFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldingFeature.orientation == FoldingFeature.Orientation.VERTICAL || foldingFeature?.orientation == FoldingFeature.Orientation.HORIZONTAL
}

@OptIn(ExperimentalContracts::class)
private fun isFlatLandscape(foldingFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldingFeature != null) }
    return foldingFeature?.state == FoldingFeature.State.FLAT &&
            foldingFeature.orientation == FoldingFeature.Orientation.HORIZONTAL
}

@Composable
fun CompactScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = "contact_list_screen") {
        composable("contact_list_screen") {
            ContactListScreen(
                sharedViewModel = sharedViewModel,
                onAction = sharedViewModel::onAction,
                navController = navController,
                isMobile = true,
            )
        }
        composable(
            "contact_detail"
        ) {
            DetailScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                isMobile = true
            )
        }
    }
}

@Composable
fun ExpandedScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    firstScreenFloat: Float,
    secondScreenFloat: Float,
    modifier: Modifier = Modifier,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(firstScreenFloat)) {
            ContactListScreen(
                sharedViewModel = sharedViewModel,
                onAction = sharedViewModel::onAction,
                navController = navController,
                isMobile = false
            )
        }
        VerticalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 2.dp)
        Box(modifier = Modifier.weight(secondScreenFloat)) {
            DetailScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                isMobile = false
            )
        }
    }
}
