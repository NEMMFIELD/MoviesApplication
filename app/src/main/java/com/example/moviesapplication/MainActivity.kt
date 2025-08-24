package com.example.moviesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.Screen
import com.example.core_ui.MoviesTheme
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModelFactory
import com.example.movies.auth.ui.MoviesAuthViewModelFactory
import com.example.movies.nowplaying.ui.NowPlayingViewModelFactory
import com.example.movies.toprated.ui.TopRatedViewModelFactory
import com.example.movies_details.ui.MovieDetailsViewModelFactoryImpl
import com.example.movies_popular.ui.PopularViewModelFactory
import com.example.movies_rating.ui.MoviesRatingViewModelFactory
import com.example.movies_upcoming.ui.UpcomingViewModelFactory
import com.example.moviesapplication.ui.set.BottomNavBar
import com.example.moviesapplication.ui.set.MoviesNavGraph
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
    lateinit var moviesRatingViewModelFactory: MoviesRatingViewModelFactory

    @Inject
    lateinit var moviesAuthViewModelFactory: MoviesAuthViewModelFactory

    @Inject
    lateinit var actorMovieCreditsViewModelFactory: ActorMovieCreditsViewModelFactory

    @Inject
    lateinit var movieDetailsViewModelFactory: MovieDetailsViewModelFactoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MoviesTheme {
                val navController = rememberNavController()
                val saveableStateHolder = rememberSaveableStateHolder()

                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            if (currentRoute in listOf(
                                    Screen.NowPlaying.route,
                                    Screen.Popular.route,
                                    Screen.TopRated.route,
                                    Screen.Upcoming.route
                                )
                            ) {
                                BottomNavBar(navController)
                            }
                        }
                    ) { innerPadding ->
                        MoviesNavGraph(
                            navController = navController,
                            saveableStateHolder = saveableStateHolder,
                            nowPlayingFactory = nowPlayingViewModelFactory,
                            popularFactory = popularViewModelFactory,
                            topRatedFactory = topRatedViewModelFactory,
                            upcomingFactory = upcomingViewModelFactory,
                            moviesRatingFactory = moviesRatingViewModelFactory,
                            moviesAuthFactory = moviesAuthViewModelFactory,
                            actorMovieCreditsFactory = actorMovieCreditsViewModelFactory,
                            movieDetailsFactory = movieDetailsViewModelFactory,
                            activityIntent = intent,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}







