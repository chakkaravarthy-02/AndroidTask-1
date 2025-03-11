package com.example.contactapp.presentation.add_edit

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.contactapp.R
import com.example.contactapp.presentation.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    sharedViewModel: SharedViewModel,
    navController: NavController,
    isCreate: Boolean,
    onChangeScreenToDetail: () -> Unit,
    isMobile: Boolean
) {

    val context = LocalContext.current
    val nameText = rememberSaveable {
        mutableStateOf(if (isCreate) "" else sharedViewModel.detailState.value.selectedPhoneContact?.displayName)
    }
    val surnameText = rememberSaveable {
        mutableStateOf("")
    }
    val phoneText = rememberSaveable {
        mutableStateOf(if (isCreate) "" else sharedViewModel.detailState.value.selectedPhoneContact?.phoneNumber)
    }
    val imageUri = rememberSaveable {
        mutableStateOf(if (isCreate) null else sharedViewModel.detailState.value.selectedPhoneContact?.picture?.let {
            Uri.parse(it)
        })
    }
    val tempUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri.value = tempUri.value
        }
    }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri.value = uri
        }
    }
    val cameraPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getTempImageUri(context)?.let {
                tempUri.value = it
                takePictureLauncher.launch(it)
            }
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                title = {
                    Text(text = if (isCreate) "Create Contact" else "Edit Contact")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isMobile) {
                                navController.popBackStack()
                            } else {
                                onChangeScreenToDetail()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            val message =
                                if (isCreate) {
                                    sharedViewModel.onAddEditAction(
                                        AddEditAction.SaveContact(
                                            nameText.value,
                                            phoneText.value,
                                            surnameText.value,
                                            imageUri.value
                                        )
                                    )
                                    if (isMobile) {
                                        navController.popBackStack()
                                    } else {
                                        onChangeScreenToDetail()
                                    }
                                    "Contact Saved successfully"
                                } else {
                                    sharedViewModel.onAddEditAction(
                                        AddEditAction.EditContact(
                                            nameText.value,
                                            phoneText.value,
                                            surnameText.value,
                                            imageUri.value
                                        )
                                    )
                                    if (isMobile) {
                                        navController.navigate(
                                            "contact_list_Screen"
                                        ) {
                                            popUpTo("contact_detail") {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        onChangeScreenToDetail()
                                    }
                                    "Edited"
                                }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = if (isCreate) "Save" else "Edit")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable {
                        showImagePickerDialog(context, cameraPermission, pickImageLauncher)
                    },
                model = imageUri.value ?: R.drawable.images_upload,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(modifier = Modifier.clickable {
                showImagePickerDialog(context, cameraPermission, pickImageLauncher)
            }, text = "Add photo", color = Color(0xFF3299EC))
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                maxLines = 1,
                value = nameText.value ?: "",
                onValueChange = { new ->
                    nameText.value = new
                },
                label = { Text("First Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            )

            OutlinedTextField(
                maxLines = 1,
                value = surnameText.value,
                onValueChange = { new ->
                    surnameText.value = new
                },
                label = { Text("Surname") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            OutlinedTextField(
                maxLines = 1,
                value = phoneText.value ?: "",
                onValueChange = { new ->
                    phoneText.value = new
                },
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            )
        }
    }
}

fun getTempImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "temp_image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
}

fun showImagePickerDialog(
    context: Context,
    cameraPermission: ManagedActivityResultLauncher<String, Boolean>,
    pickImageLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    AlertDialog.Builder(context)
        .setTitle("Select Image")
        .setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
            if (which == 0) {
                cameraPermission.launch(Manifest.permission.CAMERA)
            } else {
                pickImageLauncher.launch("image/*")
            }
        }
        .show()
}


