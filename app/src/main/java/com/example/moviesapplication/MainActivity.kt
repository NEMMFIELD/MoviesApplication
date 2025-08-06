package com.example.moviesapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core_ui.MoviesTheme
import com.example.movies.actorfilms.ui.ActorMovieCreditsScreen
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModel
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModelFactory
import com.example.movies.nowplaying.ui.NowPlayingMoviesList
import com.example.movies.nowplaying.ui.NowPlayingViewModel
import com.example.movies.nowplaying.ui.NowPlayingViewModelFactory
import com.example.movies_details.navigation.ACTOR_ID_ARG
import com.example.movies_details.navigation.ACTOR_MOVIE_CREDITS_ROUTE
import com.example.movies_details.navigation.MOVIE_DETAILS_ROUTE
import com.example.movies_details.navigation.MOVIE_ID_ARG
import com.example.movies_details.navigation.NOW_PLAYING_ROUTE
import com.example.movies_details.navigation.POPULAR_ROUTE
import com.example.movies_details.navigation.TOP_RATED_ROUTE
import com.example.movies_details.navigation.UPCOMING_ROUTE
import com.example.movies_details.navigation.actorMovieCreditsRoute
import com.example.movies_details.ui.MovieDetailsScreen
import com.example.movies_details.ui.MovieDetailsViewModel
import com.example.movies_details.ui.MovieDetailsViewModelFactoryImpl
import dagger.android.AndroidInjection
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var nowPlayingViewModelFactory: NowPlayingViewModelFactory

    private lateinit var nowPlayingViewModel: NowPlayingViewModel

    @Inject
    lateinit var actorMovieCreditsViewModelFactory: ActorMovieCreditsViewModelFactory

    private lateinit var actorMovieCreditsViewModel: ActorMovieCreditsViewModel

    @Inject
    lateinit var movieDetailsViewModelFactory: MovieDetailsViewModelFactoryImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)  // если используешь Dagger Android
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars =
                false // Если нужно сделать контент под темный фон
            controller.hide(WindowInsetsCompat.Type.statusBars())
        }

        nowPlayingViewModel =
            ViewModelProvider(this, nowPlayingViewModelFactory)[NowPlayingViewModel::class.java]

        actorMovieCreditsViewModel = ViewModelProvider(
            this, actorMovieCreditsViewModelFactory
        )[ActorMovieCreditsViewModel::class.java]


        setContent {
            MoviesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val bottomRoutes = listOf(
                        BottomNavItem.NowPlaying.route,
                        BottomNavItem.Popular.route,
                        BottomNavItem.TopRated.route,
                        BottomNavItem.Upcoming.route
                    )
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            if (currentRoute in bottomRoutes) {
                                BottomNavBar(navController)
                            }
                        }
                    ) { innerPading ->
                        NavHost(
                            navController = navController,
                            startDestination = BottomNavItem.NowPlaying.route,
                            modifier = Modifier.padding(innerPading)
                        ) {
                            composable(NOW_PLAYING_ROUTE) {
                                NowPlayingMoviesList(
                                    navController = navController, viewModel = nowPlayingViewModel
                                )
                            }

                            composable(BottomNavItem.Popular.route) {
                                Text("Popular Movies") // Replace with your Popular ViewModel/Screen
                            }

                            composable(BottomNavItem.TopRated.route) {
                                Text("Top Rated Movies") // Replace with your Top Rated ViewModel/Screen
                            }

                            composable(BottomNavItem.Upcoming.route) {
                                Text("Upcoming Movies") // Replace with your Upcoming ViewModel/Screen
                            }

                            composable(
                                route = "$MOVIE_DETAILS_ROUTE/{$MOVIE_ID_ARG}",
                                arguments = listOf(navArgument(MOVIE_ID_ARG) {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getInt(MOVIE_ID_ARG)
                                val viewModel: MovieDetailsViewModel = viewModel(
                                    factory = movieDetailsViewModelFactory.provideFactory(
                                        backStackEntry
                                    ),
                                    viewModelStoreOwner = backStackEntry
                                )
                                LaunchedEffect(movieId) {
                                    Log.d("MovieDetails", "Loading details for movieId=$movieId")
                                    viewModel.loadMovieDetails(movieId)
                                    viewModel.loadMovieActors(movieId)
                                }

                                MovieDetailsScreen(
                                    viewModel = viewModel,
                                    navController
                                )
                            }
                            composable(
                                route = "$ACTOR_MOVIE_CREDITS_ROUTE/{$ACTOR_ID_ARG}",
                                arguments = listOf(navArgument(ACTOR_ID_ARG) {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                val actorId = backStackEntry.arguments?.getInt(ACTOR_ID_ARG) ?: 0
                                val viewModel = ViewModelProvider(
                                    backStackEntry,
                                    actorMovieCreditsViewModelFactory
                                )[ActorMovieCreditsViewModel::class.java]
                                LaunchedEffect(actorId) { viewModel.loadActorMovieCredits(actorId) }

                                ActorMovieCreditsScreen(
                                    viewModel = viewModel,
                                    onActorClick = { actorId ->
                                        navController.navigate(actorMovieCreditsRoute(actorId))
                                    }, navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.NowPlaying,
        BottomNavItem.Popular,
        BottomNavItem.TopRated,
        BottomNavItem.Upcoming
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(BottomNavItem.NowPlaying.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, color = if (currentRoute == item.route) Color.Cyan else Color.Gray) }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object NowPlaying : BottomNavItem(NOW_PLAYING_ROUTE, "Now Playing", Icons.Default.PlayArrow)
    object Popular : BottomNavItem(POPULAR_ROUTE, "Popular", Icons.Default.Star)
    object TopRated : BottomNavItem(TOP_RATED_ROUTE, "Top Rated", Icons.Default.ThumbUp)
    object Upcoming : BottomNavItem(UPCOMING_ROUTE, "Upcoming", Icons.Default.DateRange)
}




