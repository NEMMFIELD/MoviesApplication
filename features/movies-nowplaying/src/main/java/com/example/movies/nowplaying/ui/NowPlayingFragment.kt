@file:OptIn(FlowPreview::class)

package com.example.movies.nowplaying.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.core.navigation.Screen
import com.example.core_model.MovieModel
import com.example.movies_details.navigation.movieDetailsRoute
import com.example.state.State
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.floor


@Composable
fun NowPlayingMoviesList(
    viewModel: NowPlayingViewModel,
    navController: NavController
) {
    val state by viewModel.nowPlayingMoviesValue.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val gridState = rememberLazyGridState()
    var hasScrolled by remember { mutableStateOf(false) }

    // Прокручиваем к сохранённой позиции после загрузки первой страницы
    if (state is State.Success && !hasScrolled) {
        val data = (state as State.Success<List<MovieModel>?>).data.orEmpty()
        if (data.isNotEmpty()) {
            LaunchedEffect(data) {
                val (index, offset) = viewModel.getScrollPosition()
                gridState.scrollToItem(index.coerceIn(0, data.size - 1), offset)
                hasScrolled = true
            }
        }
    }

    // Сохраняем позицию скролла, но с debounce
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset }
            .debounce(150)
            .collect { (index, offset) ->
                viewModel.saveScrollPosition(index, offset)
            }
    }

    // Пагинация — реагируем только на индекс последнего видимого элемента
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleItem ->
                val totalItems = gridState.layoutInfo.totalItemsCount
                if (lastVisibleItem != null &&
                    lastVisibleItem >= totalItems - 3 &&
                    !isLoading &&
                    !viewModel.isLastPage
                ) {
                    viewModel.loadNowPlayingMovies()
                }
            }
    }

    when (state) {
        is State.Success -> {
            NowPlayingGrid(
                movies = (state as State.Success<List<MovieModel>?>).data.orEmpty(),
                isLoading = isLoading,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                },
                gridState = gridState,
                animateItems = viewModel.isFirstLoad
            )
        }

        is State.Failure -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Error: ${(state as State.Failure).message.message ?: "Unknown error"}",
                    color = Color.Red
                )
            }
        }

        State.Empty, null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun NowPlayingGrid(
    movies: List<MovieModel>,
    isLoading: Boolean,
    onMovieClick: (Int) -> Unit,
    gridState: LazyGridState,
    animateItems: Boolean
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val columns = if (isPortrait) 2 else 4
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            AnimatedMovieItem(
                movie = movie,
                onClick = { onMovieClick(movie.id ?: 0) },
                animate = animateItems
            )
        }

        if (isLoading) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun AnimatedMovieItem(
    movie: MovieModel,
    onClick: () -> Unit,
    animate: Boolean,
    modifier: Modifier = Modifier
) {
    if (animate) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                initialOffsetY = { it / 4 },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        ) {
            MovieItem(movie = movie, onClick = onClick)
        }
    } else {
        MovieItem(movie = movie, onClick = onClick)
    }
}

@Composable
fun MovieItem(movie: MovieModel, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val horizontalPadding = if (isPortrait) 12.dp else 4.dp
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 6.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = movie.title ?: "unknown",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            AsyncImage(
                model = movie.fullPosterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .size(160.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            MovieRatingStars(rating = movie.rating ?: 0f)
        }
    }
}

@Composable
fun MovieRatingStars(rating: Float) {
    val safeRating = rating.coerceIn(0f, 5f)
    val fullStars = floor(safeRating).toInt()
    val hasHalfStar = ((safeRating * 10).toInt() % 10) in 3..7
    val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

    Row {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }

        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Default.StarHalf,
                contentDescription = "Half star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }

        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarBorder,
                contentDescription = "Empty Star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


