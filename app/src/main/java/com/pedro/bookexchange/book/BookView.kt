package com.pedro.bookexchange.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pedro.bookexchange.addbook.ToggleTradeButton
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.common.Title
import com.pedro.bookexchange.global.CurrentUser

@Composable
fun BookScreen(
    viewState: BookViewState,
    isSameUser: Boolean,
    onProposalChange: (Boolean) -> Unit,
) {
    Surface {
        var availableForProposal by remember { mutableStateOf(viewState.availableForProposal) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Title(viewState.bookTitle)

            AsyncImage(
                model = viewState.bookImage,
                contentDescription = "Book Cover",
                modifier = Modifier
                    .aspectRatio(1.3f)
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillHeight,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Autor(a): ${viewState.bookAuthor}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Dono: ${viewState.userName}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (isSameUser) {
                ToggleTradeButton(
                    availableForProposal,
                    { availableForProposal = it }
                )
                Button(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onProposalChange(availableForProposal) },
                ) {
                    Text("Atualizar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookScreenPreview() {
    BookScreen(
        viewState = BookViewState(
            bookId = "1",
            userId = "",
            userName = "",
            bookTitle = "Compose for Android",
            bookImage = "https://example.com/book.jpg",
            bookAuthor = "Jane Smith",
            availableForProposal = true,
        ),
        false,
        onProposalChange = {}
    )
}
