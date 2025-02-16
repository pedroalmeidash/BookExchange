package com.pedro.bookexchange.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.pedro.bookexchange.addbook.ToggleTradeButton
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.common.Title

@Composable
fun ProfileView(
    viewState: ProfileViewState,
    onBookClick: (String) -> Unit,
    logout: () -> Unit,
) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                Title(
                    if (viewState.showOptions) {
                        "Meu Perfil"
                    } else {
                        "Perfil de ${viewState.userName}"
                    }
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    text = "Livros trocados: ${viewState.exchangedBooks}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                    text = "Livros na biblioteca",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(16.dp))
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        count = viewState.booksInLibrary.size,
                    ) { index ->
                        val result = viewState.booksInLibrary[index]
                        Book(result, { onBookClick(result.bookId) },)
                    }
                    item(span = { GridItemSpan(3) }) { Modifier.height(16.dp) }
                }
            }
        },
        bottomBar = {
            var enabled by remember { mutableStateOf(true) }
            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                ToggleTradeButton(
                    availableForProposal = true,
                    onToggle = {
                        enabled = false
                        logout()
                    },
                    enabled = enabled,
                    text = "Sair",
                )
            }
        }
    )
}

@Composable
fun Book(bookViewState: BookViewState, onBookClick: (String) -> Unit) {
    Column(
        modifier = Modifier.clickable {
            onBookClick(bookViewState.bookId)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .aspectRatio(0.7f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(bookViewState.bookImage)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            onError = {
                println(it)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = bookViewState.bookTitle,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}