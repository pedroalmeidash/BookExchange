package com.pedro.bookexchange.library

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.pedro.bookexchange.R
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.common.Title
import java.io.File

@Composable
internal fun LibraryView(
    state: LibraryViewState,
    onAddBook: () -> Unit,
    onBookClick: (String) -> Unit,
) {
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Title("Biblioteca")

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (state.toSelect.not()) item { AddBook(onAddBook) }
                    items(
                        count = state.books.size,
                    ) { index ->
                        val result = state.books[index]
                        BookCard(
                            image = result.bookImage,
                            title = result.bookTitle,
                            { onBookClick(result.bookId) },
                        )
                    }
                    item(span = { GridItemSpan(3) }) { Modifier.height(16.dp) }
                }
            }
        }
    )
}

@Composable
private fun AddBook(onAddBook: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        Box(
            modifier = Modifier
                .background(Color(0x1A000000))
                .aspectRatio(0.7f)
                .border(2.dp, color = Color(0xFFFA8C16))
                .clickable { onAddBook() },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_add_24),
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color(0xFFFA8C16)),
                contentDescription = null,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Adicionar livro",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BookCard(
    image: String,
    title: String,
    onBookClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable {
            onBookClick()
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .aspectRatio(0.7f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            onError = {
                println(it)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}


@Preview
@Composable
fun LibraryViewPreview() {
    val books = listOf(
        BookViewState(
            bookId = "1",
            bookImage = "https://m.media-amazon.com/images/I/81iqZ2HHD-L.jpg",
            bookTitle = "Harry Potter e a Pedra Filosofal",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "2",
            bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
            bookTitle = "O Senhor dos Anéis: A Sociedade do Anel",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "3",
            bookImage = "https://m.media-amazon.com/images/I/91bYsX41DVL.jpg",
            bookTitle = "1984",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "4",
            bookImage = "https://m.media-amazon.com/images/I/91SZSW8qSsL.jpg",
            bookTitle = "A Revolução dos Bichos",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "5",
            bookImage = "https://m.media-amazon.com/images/I/81WcnNQ-TBL.jpg",
            bookTitle = "O Pequeno Príncipe",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "6",
            bookImage = "https://m.media-amazon.com/images/I/81h2gWPTYJL.jpg",
            bookTitle = "Dom Quixote",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "7",
            bookImage = "https://m.media-amazon.com/images/I/71aFt4+OTOL.jpg",
            bookTitle = "O Código Da Vinci",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "8",
            bookImage = "https://m.media-amazon.com/images/I/71KilybDOoL.jpg",
            bookTitle = "O Hobbit",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
        BookViewState(
            bookId = "9",
            bookImage = "https://m.media-amazon.com/images/I/81eB+7+CkUL.jpg",
            bookTitle = "Quem Mexeu no Meu Queijo?",
            bookAuthor = "Author",
            userName = "name",
            userId = "",
            availableForProposal = false,
        ),
    )
    LibraryView(
        state = LibraryViewState(
            books = books,
            false,
        ),
        onAddBook = {},
        onBookClick = {},
    )
}