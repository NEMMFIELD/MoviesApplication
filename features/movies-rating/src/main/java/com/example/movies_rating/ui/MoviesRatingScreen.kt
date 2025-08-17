package com.example.movies_rating.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movies_details.navigation.NOW_PLAYING_ROUTE
import com.example.movies_details.navigation.RATING_ROUTE
import com.example.movies_rating.R
import com.example.movies_rating.data.MoviesRatingUiState
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesRatingScreen(
    movieTitle: String,
    movieId: Int,
    viewModel: MoviesRatingViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var rating by remember { mutableStateOf(5) } // int для IMDbRatingBar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movieTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Your rate",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // IMDbRatingBar вместо обычного RatingBar
            IMDbRatingBar(
                rating = rating,
                maxRating = 10,
                onRatingChanged = { rating = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$rating / 10",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.rateMovie(movieId, rating.toDouble()) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Send", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (uiState) {
                is MoviesRatingUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is MoviesRatingUiState.MovieRated -> {
                    Text(
                        "Rating is sent! Thanks.",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                    LaunchedEffect(Unit)
                    {
                        delay(1500)
                        navController.navigate(NOW_PLAYING_ROUTE) {
                            popUpTo(RATING_ROUTE) { inclusive = true }
                        }
                    }
                }

                is MoviesRatingUiState.Error -> Text(
                    "Error: ${parseErrorMessage((uiState as MoviesRatingUiState.Error).throwable)}",
                    color = Color.Red
                )

                else -> Unit
            }
        }
    }
}

@Composable
fun IMDbRatingBar(
    rating: Int,
    maxRating: Int = 10,
    onRatingChanged: (Int) -> Unit
) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                painter = painterResource(
                    id = if (i <= rating) R.drawable.ic_star_filled else R.drawable.ic_star_outline
                ),
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}

private fun parseErrorMessage(e: Throwable): String {
    return when (e) {
        is java.net.UnknownHostException -> "No internet connection."
        is java.net.SocketTimeoutException -> "Request timed out. Please try again."
        is retrofit2.HttpException -> "Server error: ${e.code()}"
        else -> e.message ?: "Unknown error occurred"
    }
}

