package com.example.moviesapplication.ui.set

import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.core.navigation.Screen
import com.example.movies.actorfilms.ui.ActorMovieCreditsScreen
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModel
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModelFactory
import com.example.movies.auth.ui.AuthScreen
import com.example.movies.auth.ui.MoviesAuthViewModel
import com.example.movies.auth.ui.MoviesAuthViewModelFactory
import com.example.movies.nowplaying.ui.NowPlayingMoviesList
import com.example.movies.nowplaying.ui.NowPlayingViewModel
import com.example.movies.nowplaying.ui.NowPlayingViewModelFactory
import com.example.movies.toprated.ui.TopRatedMoviesList
import com.example.movies.toprated.ui.TopRatedViewModel
import com.example.movies.toprated.ui.TopRatedViewModelFactory
import com.example.movies_details.ui.MovieDetailsScreen
import com.example.movies_details.ui.MovieDetailsViewModel
import com.example.movies_details.ui.MovieDetailsViewModelFactoryImpl
import com.example.movies_popular.ui.PopularMoviesList
import com.example.movies_popular.ui.PopularViewModel
import com.example.movies_popular.ui.PopularViewModelFactory
import com.example.movies_rating.ui.MoviesRatingScreen
import com.example.movies_rating.ui.MoviesRatingViewModel
import com.example.movies_rating.ui.MoviesRatingViewModelFactory
import com.example.movies_upcoming.ui.UpcomingMoviesList
import com.example.movies_upcoming.ui.UpcomingViewModel
import com.example.movies_upcoming.ui.UpcomingViewModelFactory

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val items = listOf(
        Screen.NowPlaying,
        Screen.Popular,
        Screen.TopRated,
        Screen.Upcoming
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.NowPlaying -> Icons.Default.PlayArrow
                            Screen.Popular -> Icons.Default.Star
                            Screen.TopRated -> Icons.Default.ThumbUp
                            Screen.Upcoming -> Icons.Default.DateRange
                            else -> Icons.Default.Movie
                        },
                        contentDescription = screen.route
                    )
                },
                label = {
                    Text(
                        text = when (screen) {
                            Screen.NowPlaying -> "Now Playing"
                            Screen.Popular -> "Popular"
                            Screen.TopRated -> "Top Rated"
                            Screen.Upcoming -> "Upcoming"
                            else -> ""
                        },
                        color = if (selected) Color.Cyan else Color.Gray
                    )
                }
            )
        }
    }
}

@Composable
fun MoviesNavGraph(
    navController: NavHostController,
    saveableStateHolder: SaveableStateHolder,
    nowPlayingFactory: NowPlayingViewModelFactory,
    popularFactory: PopularViewModelFactory,
    topRatedFactory: TopRatedViewModelFactory,
    upcomingFactory: UpcomingViewModelFactory,
    moviesRatingFactory: MoviesRatingViewModelFactory,
    moviesAuthFactory: MoviesAuthViewModelFactory,
    actorMovieCreditsFactory: ActorMovieCreditsViewModelFactory,
    movieDetailsFactory: MovieDetailsViewModelFactoryImpl,
    activityIntent: Intent?,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        modifier = modifier
    ) {
        // Auth
        composable(Screen.Auth.route) { backStackEntry ->
            val viewModel: MoviesAuthViewModel = viewModel(
                factory = moviesAuthFactory,
                viewModelStoreOwner = backStackEntry
            )

            LaunchedEffect(Unit) {
                val data: Uri? = activityIntent?.data
                if (data?.host == "auth") {
                    val token = data.getQueryParameter("request_token")
                    if (!token.isNullOrEmpty()) {
                        viewModel.finishAuth(token)
                    }
                }
            }

            AuthScreen(factory = moviesAuthFactory, navController = navController)
        }

        // Now Playing
        composable(Screen.NowPlaying.route) { backStackEntry ->
            val viewModel: NowPlayingViewModel = viewModel(
                factory = nowPlayingFactory,
                viewModelStoreOwner = backStackEntry
            )
            saveableStateHolder.SaveableStateProvider(Screen.NowPlaying.route) {
                NowPlayingMoviesList(viewModel = viewModel, navController = navController)
            }
        }

        // Popular
        composable(Screen.Popular.route) { backStackEntry ->
            val viewModel: PopularViewModel = viewModel(
                factory = popularFactory,
                viewModelStoreOwner = backStackEntry
            )
            saveableStateHolder.SaveableStateProvider(Screen.Popular.route) {
                PopularMoviesList(popularViewModel = viewModel, navController = navController)
            }
        }

        // Top Rated
        composable(Screen.TopRated.route) { backStackEntry ->
            val viewModel: TopRatedViewModel = viewModel(
                factory = topRatedFactory,
                viewModelStoreOwner = backStackEntry
            )
            saveableStateHolder.SaveableStateProvider(Screen.TopRated.route) {
                TopRatedMoviesList(topRatedViewModel = viewModel, navController = navController)
            }
        }

        // Upcoming
        composable(Screen.Upcoming.route) { backStackEntry ->
            val viewModel: UpcomingViewModel = viewModel(
                factory = upcomingFactory,
                viewModelStoreOwner = backStackEntry
            )
            saveableStateHolder.SaveableStateProvider(Screen.Upcoming.route) {
                UpcomingMoviesList(upcomingViewModel = viewModel, navController = navController)
            }
        }

        // Movie Details
        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            val viewModel: MovieDetailsViewModel = viewModel(
                factory = movieDetailsFactory.provideFactory(backStackEntry),
                viewModelStoreOwner = backStackEntry
            )
            LaunchedEffect(movieId) {
                viewModel.loadMovieDetails(movieId)
                viewModel.loadMovieActors(movieId)
            }
            MovieDetailsScreen(viewModel = viewModel, navController = navController)
        }

        // Rating
        composable(
            route = "rating/{movieId}/{movieTitle}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType },
                navArgument("movieTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val movieTitle =
                backStackEntry.arguments?.getString("movieTitle")?.let { Uri.decode(it) }

            val viewModel: MoviesRatingViewModel = viewModel(
                factory = moviesRatingFactory,
                viewModelStoreOwner = backStackEntry
            )

            MoviesRatingScreen(
                movieId = movieId,
                movieTitle = movieTitle.orEmpty(),
                viewModel = viewModel,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // Actor Movie Credits
        composable(
            route = Screen.ActorMovieCredits.route,
            arguments = listOf(navArgument("actorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val actorId = backStackEntry.arguments?.getInt("actorId") ?: 0
            val viewModel: ActorMovieCreditsViewModel = viewModel(
                factory = actorMovieCreditsFactory,
                viewModelStoreOwner = backStackEntry
            )
            LaunchedEffect(actorId) {
                viewModel.loadActorMovieCredits(actorId)
            }
            ActorMovieCreditsScreen(
                viewModel = viewModel,
                onActorClick = { navController.navigate(Screen.ActorMovieCredits.createRoute(it)) },
                navController = navController
            )
        }
    }
}
