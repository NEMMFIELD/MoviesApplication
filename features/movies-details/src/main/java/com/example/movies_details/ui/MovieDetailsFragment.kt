package com.example.movies_details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.core.navigation.Screen
import com.example.movies_details.data.MovieActorsModel
import com.example.movies_details.data.MovieDetailsModel
import com.example.movies_details.navigation.actorMovieCreditsRoute
import com.example.movies_details.navigation.rateMovieRoute
import com.example.state.State
import com.google.accompanist.flowlayout.FlowRow
import kotlin.math.floor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel,
    navController: NavController
) {
    val state by viewModel.movieDetailsValue.collectAsState()
    val actorsState by viewModel.movieDetailsActors.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Movie Details")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val movieState = state) {
            is State.Success -> {
                val movie = movieState.data
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                        .padding(top = innerPadding.calculateTopPadding() / 10)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    MovieTitleWithRateButton(movie, navController)
                    MovieBackdrop(movie.fullbackDropUrl, movie.title)
                    MovieOverview(movie.overview)
                    MovieRatingAndGenres(movie.rating, movie.genres as List<String>?)


                    if (actorsState is State.Success) {
                        val actors = (actorsState as State.Success<List<MovieActorsModel>>).data
                            .filter { !it.profilePath.isNullOrBlank() }
                            .distinctBy { it.id }

                        MovieActorsSection(actors, navController)
                    }
                }
            }

            is State.Failure -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error loading: ${movieState.message.localizedMessage ?: "Unknown error"}")
                }
            }

            null, State.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun MovieTitleWithRateButton(movie: MovieDetailsModel, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Кнопка с увеличенной звездочкой
        IconButton(
            onClick = {  navController.navigate(Screen.Rating.createRoute(movie.id ?: 0, movie.title ?: "null")) },
            modifier = Modifier
                .size(50.dp) // чуть больше кнопка
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rate",
                tint = Color.White,
                modifier = Modifier.size(24.dp) // увеличенная звезда
            )
        }

        Spacer(modifier = Modifier.width(30.dp))

        // Текст занимает всё оставшееся место
        Text(
            text = movie.title ?: "Unknown Title",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun MovieBackdrop(url: String?, contentDesc: String?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDesc,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}

@Composable
fun MovieOverview(overview: String?) {
    Text(
        text = overview ?: "No overview",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun MovieRatingAndGenres(rating: Float?, genres: List<String>?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RatingStars(rating = rating ?: 0f)
        Spacer(modifier = Modifier.width(12.dp))
        GenreTags(genres.orEmpty())
    }
}

@Composable
fun RatingStars(rating: Float) {
    val normalized = rating.coerceIn(0f, 5f)
    val fullStars = floor(normalized).toInt()
    val hasHalfStar = ((normalized * 10).toInt() % 10) in 3..7
    val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Full Star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }
        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Default.StarHalf,
                contentDescription = "Half Star",
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

@Composable
fun GenreTags(genres: List<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        mainAxisSpacing = 4.dp,
        crossAxisSpacing = 5.dp
    ) {
        genres.sorted().forEach { genre ->
            GenreChip(text = genre)
        }
    }
}

@Composable
fun GenreChip(text: String) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MovieActorsSection(actors: List<MovieActorsModel>, navController: NavController) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Actors",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp, start = 3.dp)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(actors) { actor ->
            ActorItem(actor = actor) {
                navController.navigate(actorMovieCreditsRoute(actor.id ?: 0))
            }
        }
    }
}

@Composable
fun ActorItem(
    actor: MovieActorsModel,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = actor.name ?: "No name",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(actor.fullProfilePath)
                    .crossfade(true)
                    .build()
            ),
            contentDescription = actor.name,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )
    }
}

