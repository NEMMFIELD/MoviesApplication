package com.example.movies.actorfilms.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.movies.actorfilms.data.ActorMovieCreditsModel
import com.example.movies_details.navigation.movieDetailsRoute
import com.example.state.State

@Composable
fun ActorMovieCreditsScreen(
    viewModel: ActorMovieCreditsViewModel,
    onActorClick: (Int) -> Unit,
    navController: NavController
) {
    val state by viewModel.actorMovieCreditsValue.collectAsState()

    when (val result = state) {

        is State.Success -> {
            val movies = result.data
                .orEmpty()
                .filter { !it.posterPath.isNullOrBlank() && it.id != null }
                .distinctBy { it.id }

            if (movies.isEmpty()) {
                EmptyScreen()
            } else {
                MoviePosterGrid(
                    movies = movies,
                    onMovieClick = { movieId ->
                        navController.navigate(movieDetailsRoute(movieId))
                    }
                )
            }
        }

        is State.Failure -> ErrorScreen(message = result.message.message ?: "Unknown error")

        is State.Empty -> EmptyScreen()

        null -> {}
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $message", color = Color.Red)
    }
}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviePosterGrid(
    movies: List<ActorMovieCreditsModel>,
    onMovieClick: (Int) -> Unit // передаём ID фильма
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MoviePosterItem(movie = movie, onClick = { onMovieClick(movie.id ?: 0) })
        }
    }
}

@Composable
fun MoviePosterItem(
    movie: ActorMovieCreditsModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(0.67f) // Примерно соотношение постера
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = movie.fullPosterPath,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
