package com.example.moviesapplication

import android.app.Application
import com.example.movies.actorfilms.di.ActorMovieCreditsComponent
import com.example.movies.actorfilms.di.ActorMovieCreditsComponentProvider
import com.example.movies.nowplaying.di.NowPlayingComponent
import com.example.movies.nowplaying.di.NowPlayingComponentProvider
import com.example.movies.toprated.di.TopRatedComponent
import com.example.movies.toprated.di.TopRatedComponentProvider
import com.example.movies_popular.di.PopularComponent
import com.example.movies_popular.di.PopularComponentProvider
import com.example.movies_rating.di.MoviesRatingComponent
import com.example.movies_rating.di.MoviesRatingComponentProvider
import com.example.movies_upcoming.di.UpcomingComponent
import com.example.movies_upcoming.di.UpcomingComponentProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MyApp : Application(), NowPlayingComponentProvider, HasAndroidInjector,
    ActorMovieCreditsComponentProvider, PopularComponentProvider, TopRatedComponentProvider,
    UpcomingComponentProvider, MoviesRatingComponentProvider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
    }

    override fun provideNowPlayingComponent(): NowPlayingComponent {
        return appComponent.nowPlayingComponent().create()
    }

    override fun androidInjector(): AndroidInjector<in Any>? {
        return androidInjector
    }

    override fun ActorMovieCreditsComponent(): ActorMovieCreditsComponent {
        return appComponent.actorMovieCreditsComponent().create()
    }

    override fun providePopularComponent(): PopularComponent {
        return appComponent.popularComponent().create()
    }

    override fun provideTopRatedComponent(): TopRatedComponent {
        return appComponent.topRatedComponent().create()
    }

    override fun provideUpcomingComponent(): UpcomingComponent {
        return appComponent.upcomingComponent().create()
    }


    override fun provideMoviesRatingComponent(): MoviesRatingComponent {
        return appComponent.moviesRatingComponent().create()
    }


}
