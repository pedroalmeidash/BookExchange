package com.pedro.bookexchange.proposallist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pedro.bookexchange.R
import com.pedro.bookexchange.common.BookViewState
import com.pedro.bookexchange.common.Title

@Composable
fun ProposalListView(
    proposalListViewState: ProposalListViewState,
    onAccept: (String) -> Unit = {},
    onReject: (String) -> Unit = {},
) {
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        top = 24.dp,
                        start = 24.dp,
                        end = 24.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Title("Propostas")
                if (proposalListViewState.proposals.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Você ainda não recebeu nenhuma proposta",
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Que tal fazer uma?",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(proposalListViewState.proposals.size) { index ->
                            val proposal = proposalListViewState.proposals[index]
                            ProposalView(proposal, proposalListViewState, onAccept, onReject)
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun ProposalView(
    proposalViewState: ProposalViewState,
    proposalListViewState: ProposalListViewState,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, color = proposalViewState.state.getColor())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = proposalViewState.state.getText(),
            fontSize = 20.sp,
            color = proposalViewState.state.getColor(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BookView(proposalViewState, bookViewState = proposalViewState.userBook)
            Image(
                painter = painterResource(R.drawable.two_arrows),
                modifier = Modifier.size(40.dp),
                contentDescription = null,
                colorFilter = ColorFilter.tint(proposalViewState.state.getColor())
            )
            BookView(proposalViewState, bookViewState = proposalViewState.proposedBook)
        }
        if (proposalViewState.state is ProposalState.Accepted) {
            Spacer(Modifier.height(16.dp))
            val email = if (proposalViewState.userBook.userId == proposalListViewState.userId) {
                proposalListViewState.usersList.first { it.id == proposalViewState.proposedBook.userId }.email
            } else {
                proposalListViewState.usersList.first { it.id == proposalViewState.userBook.userId }.email
            }
            Text("Entrar em contato com o usuário pelo seu email: $email")
        }
        if (proposalViewState.state is ProposalState.Pending) {
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .weight(1f),
                    onClick = { onAccept(proposalViewState.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Aceitar")
                }
                Button(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .weight(1f),
                    onClick = { onReject(proposalViewState.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Recusar")
                }
            }
        }
    }
}

@Composable
private fun RowScope.BookView(proposalViewState: ProposalViewState, bookViewState: BookViewState) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
    ) {
        Text(
            text = bookViewState.userName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .border(width = 1.dp, color = proposalViewState.state.getColor()),
            model = ImageRequest.Builder(LocalContext.current)
                .setHeader("User-Agent", "Mozilla/5.0")
                .diskCacheKey(bookViewState.bookImage)
                .memoryCacheKey(bookViewState.bookImage)
                .data(bookViewState.bookImage)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = bookViewState.bookTitle,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = bookViewState.bookAuthor,
            fontSize = 12.sp,
            color = Color.Gray,
        )
    }
}

private fun ProposalState.getText(): String {
    return when (this) {
        is ProposalState.Accepted -> "Aceito"
        is ProposalState.Rejected -> "Rejeitado"
        is ProposalState.Waiting -> "Aguardando"
        is ProposalState.Pending -> "Pendente"
    }
}

private fun ProposalState.getColor(): Color {
    return when (this) {
        is ProposalState.Accepted -> Color(0xFF4CAF50)
        is ProposalState.Rejected -> Color(0xFFF44336)
        is ProposalState.Waiting -> Color.White
        is ProposalState.Pending -> Color.LightGray
    }
}

@Preview
@Composable
private fun ProposalListViewPreview() {
    ProposalListView(
        proposalListViewState = ProposalListViewState(
            proposals = listOf(
                ProposalViewState(
                    id = "id",
                    userBook = BookViewState(
                        bookId = "bookId",
                        userName = "Pedro",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    proposedBook = BookViewState(
                        bookId = "bookId",
                        userName = "Nath",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    state = ProposalState.Accepted,
                ),
                ProposalViewState(
                    id = "id",
                    userBook = BookViewState(
                        bookId = "bookId",
                        userName = "Pedro",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    proposedBook = BookViewState(
                        bookId = "bookId",
                        userName = "Nath",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    state = ProposalState.Rejected,
                ),
                ProposalViewState(
                    id = "id",
                    userBook = BookViewState(
                        bookId = "bookId",
                        userName = "Nath",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    proposedBook = BookViewState(
                        bookId = "bookId",
                        userName = "Pedro",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    state = ProposalState.Waiting,
                ),
                ProposalViewState(
                    id = "id",
                    userBook = BookViewState(
                        bookId = "bookId",
                        userName = "Nath",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    proposedBook = BookViewState(
                        bookId = "bookId",
                        userName = "Pedro",
                        bookTitle = "Orgulho e preconceito",
                        bookImage = "https://m.media-amazon.com/images/I/81YOuOGFCJL.jpg",
                        bookAuthor = "Jane Austen",
                        userId = "",
                        availableForProposal = false,
                    ),
                    state = ProposalState.Pending,
                ),
            ),
            emptyList(),
            "",
        )
    )
}
