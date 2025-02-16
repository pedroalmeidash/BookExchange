package com.pedro.bookexchange.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.common.Title

@Composable
internal fun SearchView(
    state: SearchViewState,
    onSearchChanged: (String) -> Unit,
    onBookClick: (String) -> Unit,
) {
    Scaffold(
        content = { paddingValues ->
            var currentSearch by remember { mutableStateOf("") }
            var loaded by remember { mutableStateOf(false) }
            if (state.results.isNotEmpty()) loaded = true
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Title("Buscar")

                SearchBar(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    query = currentSearch,
                    onQueryChange = {
                        currentSearch = it
                        onSearchChanged(it)
                    },
                )

                if (state.results.isEmpty() && loaded) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Essa busca não encontrou resultados",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize(),
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(
                            count = state.results.size,
                        ) { index ->
                            val result = state.results[index]
                            BookCard(
                                image = result.bookImage,
                                title = result.bookTitle,
                                { onBookClick(result.bookId) },
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Busca...") },
                textStyle = TextStyle(fontSize = 16.sp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
            )

            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Composable
private fun BookCard(
    image: String,
    title: String,
    onBookClick: () -> Unit,
) {
    Column(modifier = Modifier.clickable { onBookClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .aspectRatio(0.7f),
            model = ImageRequest.Builder(LocalContext.current)
                .setHeader("User-Agent", "Mozilla/5.0")
                .diskCacheKey(image)
                .memoryCacheKey(image)
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
fun SearchViewPreview() {
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
    SearchView(
        state = SearchViewState(
            results = books,
        ), {}, {}
    )
}