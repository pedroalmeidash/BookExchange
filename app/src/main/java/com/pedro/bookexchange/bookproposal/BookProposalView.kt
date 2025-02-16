package com.pedro.bookexchange.bookproposal

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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pedro.bookexchange.R
import com.pedro.bookexchange.common.AddBookItemView
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.common.Title

@Composable
internal fun BookProposalView(
    bookProposalViewState: BookProposalViewState,
    onAdd: () -> Unit,
    onButtonTapped: () -> Unit,
    onUser: (String) -> Unit,
) {
    Scaffold(
        content = { paddingValues ->
            val modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(paddingValues)
            BookProposalView(
                modifier = modifier,
                bookProposalViewState = bookProposalViewState,
                onAdd = onAdd,
                onUser = onUser,
            )
        },
        bottomBar = {
            Button(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                onClick = { onButtonTapped() },
                enabled = bookProposalViewState.isButtonEnabled,
            ) {
                Text("Enviar proposta")
            }
        }
    )
}

@Composable
private fun BookProposalView(
    modifier: Modifier,
    bookProposalViewState: BookProposalViewState,
    onAdd: () -> Unit,
    onUser: (String) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title("Proposta")

        bookProposalViewState.bookItems.forEach { bookItem ->
            when (bookItem) {
                is BookItemViewState.Book -> BookView(bookItem.viewState, onUser = onUser)
                is BookItemViewState.AddBook -> AddBookItemView(onAdd)
            }
        }
    }
}

@Composable
private fun BookView(bookViewState: BookViewState, onUser: (String) -> Unit,) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        Text(
            modifier = Modifier.clickable { onUser(bookViewState.userId) },
            text = bookViewState.userName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(0.7f)
                    .border(width = 1.dp, color = Color.Black),
                model = ImageRequest.Builder(LocalContext.current)
                    .setHeader("User-Agent", "Mozilla/5.0")
                    .diskCacheKey(bookViewState.bookImage)
                    .memoryCacheKey(bookViewState.bookImage)
                    .data(bookViewState.bookImage)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = bookViewState.bookTitle,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bookViewState.bookAuthor,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun BookProposalViewPreview() {
    val books = listOf(
        BookItemViewState.Book(
            BookViewState(
                userName = "Pedro oferece",
                bookTitle = "Orgulho e preconceito",
                bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                bookAuthor = "Jane Austen",
                bookId = "id",
                userId = "a",
                availableForProposal = false,
            )
        ),
        BookItemViewState.AddBook,
    )
    BookProposalView(
        bookProposalViewState = BookProposalViewState(
            bookItems = books,
            isButtonEnabled = true,
        ),
        onAdd = {},
        {},{}
    )
}