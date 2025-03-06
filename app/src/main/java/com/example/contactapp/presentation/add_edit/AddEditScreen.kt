package com.example.contactapp.presentation.add_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contactapp.presentation.SharedViewModel

@Composable
fun AddScreen(modifier: Modifier = Modifier, sharedViewModel: SharedViewModel) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.inverseOnSurface)
    ){
        Text("Hello adding screen")
    }
}