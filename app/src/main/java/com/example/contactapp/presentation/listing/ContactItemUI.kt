package com.example.contactapp.presentation.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.contactapp.R
import com.example.contactapp.domain.BaseContact
import com.example.contactapp.domain.Contact
import com.example.contactapp.domain.PhoneContact

@Composable
fun ContactItemUI(
    contact: Contact?,
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    RowUI(modifier,contact,isSelected,false)
}


@Composable
fun PhoneContactItemUI(
    phoneContact: PhoneContact?,
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    RowUI(modifier,phoneContact,isSelected,true)
}

@Composable
fun RowUI(modifier: Modifier, contact: BaseContact?, isSelected: Boolean, isPhoneContact: Boolean) {
    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = contact?.picture ?: R.drawable.placeholder_contact,
            modifier = Modifier
                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background)
                .size(50.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = if(isPhoneContact) "${contact?.displayName}" else "${contact?.firstName} ${contact?.secondName}",
            modifier = Modifier.weight(1f)
        )
    }
}
