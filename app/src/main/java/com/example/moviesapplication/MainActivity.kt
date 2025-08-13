package com.example.moviesapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
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
import com.example.movies.toprated.ui.TopRatedMoviesList
import com.example.movies.toprated.ui.TopRatedViewModel
import com.example.movies.toprated.ui.TopRatedViewModelFactory
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
import com.example.movies_popular.ui.PopularMoviesList
import com.example.movies_popular.ui.PopularViewModel
import com.example.movies_popular.ui.PopularViewModelFactory
import com.example.movies_upcoming.ui.UpcomingMoviesList
import com.example.movies_upcoming.ui.UpcomingViewModel
import com.example.movies_upcoming.ui.UpcomingViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var nowPlayingViewModelFactory: NowPlayingViewModelFactory

    @Inject
    lateinit var popularViewModelFactory: PopularViewModelFactory

    @Inject
    lateinit var topRatedViewModelFactory: TopRatedViewModelFactory

    @Inject
    lateinit var upcomingViewModelFactory: UpcomingViewModelFactory

    @Inject
    lateinit var actorMovieCreditsViewModelFactory: ActorMovieCreditsViewModelFactory

    @Inject
    lateinit var movieDetailsViewModelFactory: MovieDetailsViewModelFactoryImpl

    private lateinit var nowPlayingViewModel: NowPlayingViewModel
    private lateinit var popularViewModel: PopularViewModel
    private lateinit var topRatedViewModel: TopRatedViewModel
    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var actorMovieCreditsViewModel: ActorMovieCreditsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars = false
            controller.hide(WindowInsetsCompat.Type.statusBars())
        }

        // Инициализируем ViewModel через фабрики
        nowPlayingViewModel =
            ViewModelProvider(this, nowPlayingViewModelFactory)[NowPlayingViewModel::class.java]
        popularViewModel =
            ViewModelProvider(this, popularViewModelFactory)[PopularViewModel::class.java]
        topRatedViewModel =
            ViewModelProvider(this, topRatedViewModelFactory)[TopRatedViewModel::class.java]
        upcomingViewModel =
            ViewModelProvider(this, upcomingViewModelFactory)[UpcomingViewModel::class.java]
        actorMovieCreditsViewModel = ViewModelProvider(
            this,
            actorMovieCreditsViewModelFactory
        )[ActorMovieCreditsViewModel::class.java]

        setContent {
            MoviesTheme {
                val navController = rememberNavController().also {
                    Log.d("NavControllerCheck", "NavController created: $it")
                }
                LaunchedEffect(Unit) {
                    Log.d("NavControllerCheck", "LaunchedEffect: NavController is $navController")
                }

                DisposableEffect(Unit) {
                    Log.d("NavControllerCheck", "DisposableEffect: NavController is $navController")

                    onDispose {
                        Log.d("NavControllerCheck", "NavController disposed: $navController")
                    }
                }
                val saveableStateHolder = rememberSaveableStateHolder()
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            if (currentRoute in listOf(
                                    BottomNavItem.NowPlaying.route,
                                    BottomNavItem.Popular.route,
                                    BottomNavItem.TopRated.route,
                                    BottomNavItem.Upcoming.route
                                )
                            ) {
                                BottomNavBar(navController)
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = BottomNavItem.NowPlaying.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // Bottom Nav Items
                            composable(NOW_PLAYING_ROUTE) {
                                saveableStateHolder.SaveableStateProvider(NOW_PLAYING_ROUTE) {
                                    NowPlayingMoviesList(
                                        viewModel = nowPlayingViewModel,
                                        navController = navController,

                                    )
                                }
                            }
                            composable(POPULAR_ROUTE) {
                                saveableStateHolder.SaveableStateProvider(POPULAR_ROUTE) {
                                    PopularMoviesList(
                                        popularViewModel = popularViewModel,
                                        navController = navController,
                                    )
                                }
                            }
                            composable(TOP_RATED_ROUTE) {
                                saveableStateHolder.SaveableStateProvider(TOP_RATED_ROUTE) {
                                    TopRatedMoviesList(
                                        topRatedViewModel = topRatedViewModel,
                                        navController = navController,
                                    )
                                }
                            }
                            composable(UPCOMING_ROUTE) {
                                saveableStateHolder.SaveableStateProvider(UPCOMING_ROUTE) {
                                    UpcomingMoviesList(
                                        upcomingViewModel = upcomingViewModel,
                                        navController = navController,
                                    )
                                }
                            }

                            // Movie Details
                            composable(
                                route = "$MOVIE_DETAILS_ROUTE/{$MOVIE_ID_ARG}",
                                arguments = listOf(navArgument(MOVIE_ID_ARG) { type = NavType.IntType })
                            ) { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getInt(MOVIE_ID_ARG)
                                val viewModel: MovieDetailsViewModel = viewModel(
                                    factory = movieDetailsViewModelFactory.provideFactory(backStackEntry),
                                    viewModelStoreOwner = backStackEntry
                                )
                                LaunchedEffect(movieId) {
                                    viewModel.loadMovieDetails(movieId)
                                    viewModel.loadMovieActors(movieId)
                                }
                                MovieDetailsScreen(
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }

                            // Actor Movie Credits
                            composable(
                                route = "$ACTOR_MOVIE_CREDITS_ROUTE/{$ACTOR_ID_ARG}",
                                arguments = listOf(navArgument(ACTOR_ID_ARG) { type = NavType.IntType })
                            ) { backStackEntry ->
                                val actorId = backStackEntry.arguments?.getInt(ACTOR_ID_ARG) ?: 0
                                val viewModel = ViewModelProvider(
                                    backStackEntry,
                                    actorMovieCreditsViewModelFactory
                                )[ActorMovieCreditsViewModel::class.java]
                                LaunchedEffect(actorId) {
                                    viewModel.loadActorMovieCredits(actorId)
                                }
                                ActorMovieCreditsScreen(
                                    viewModel = viewModel,
                                    onActorClick = {
                                        navController.navigate(actorMovieCreditsRoute(it))
                                    },
                                    navController = navController
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
fun rememberSaveableLazyGridState(key: String): LazyGridState {
    Log.d("LazyGridState", "Creating LazyGridState for key: $key") // <-- Лог при создании
    return rememberSaveable(key, saver = LazyGridState.Saver) {
        Log.d("LazyGridState", "Inside rememberSaveable initializer for key: $key")
        LazyGridState()
    }
}

@Composable
fun BottomNavBar(
    navController: NavController
) {
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
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = {
                    Text(
                        item.label,
                        color = if (currentRoute == item.route) Color.Cyan else Color.Gray
                    )
                }
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




