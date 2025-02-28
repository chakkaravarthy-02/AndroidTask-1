package com.example.contactapp.presentation.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.contactapp.domain.Contact

@Composable
fun ContactItemUI(
    contact: Contact?,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = contact?.picture,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(50.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = "${contact?.firstName} ${contact?.secondName}",
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactUIPreview() {
    ContactItemUI(contact = Contact(
        1, "saran", "sarath", "male", "987654321", "","", "",
        picture = ""
    ))
}