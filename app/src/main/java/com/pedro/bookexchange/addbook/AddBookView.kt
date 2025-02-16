package com.pedro.bookexchange.addbook

import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.pedro.bookexchange.R
import com.pedro.bookexchange.common.Title

@Composable
fun AddBookView(
    onBookAdded: (String, String, Uri, Boolean) -> Unit
) {
    Surface {
        var title by remember { mutableStateOf("") }
        var availableForProposal by remember { mutableStateOf(false) }
        var author by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var isLoading by remember { mutableStateOf(false) }

        val context = LocalContext.current

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            imageUri = uri
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Title("Adicionar Livro")

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(0.7f)
                    .border(
                        2.dp, color = if (imageUri == null) {
                            Color(0xFFFA8C16)
                        } else MaterialTheme.colorScheme.surface
                    )
                    .let {
                        if (imageUri == null) {
                            it.clickable {imagePickerLauncher.launch("image/*") }
                        } else {
                            it
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.baseline_add_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(Color(0xFFFA8C16))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                label = { Text("Título do Livro") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                label = { Text("Autor do Livro") },
            )
            Spacer(modifier = Modifier.height(16.dp))
            ToggleTradeButton(
                availableForProposal,
                { availableForProposal = it },
                !isLoading,
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                enabled = author.isNotBlank() && title.isNotBlank() && imageUri != null && !isLoading,
                onClick = {
                    isLoading = true
                    onBookAdded(title, author, imageUri!!, availableForProposal) }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Adicionar livro")
                }
            }
        }
    }
}

@Composable
fun ToggleTradeButton(
    availableForProposal: Boolean,
    onToggle: (Boolean) -> Unit,
    enabled: Boolean = true,
    text: String = "",
) {
    val texts = if (text.isNotBlank()) {
        text
    } else if (availableForProposal) "Remover da troca" else "Disponibilizar para troca"
    Button(
        enabled = enabled,
        onClick = { onToggle(!availableForProposal) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (availableForProposal) Color(0xFFF44336) else Color(0xFF4CAF50),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = texts,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun AddBookViewPreview() {
    AddBookView({_, _, _, _ ->})
}