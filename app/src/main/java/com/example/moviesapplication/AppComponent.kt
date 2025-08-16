package com.example.moviesapplication

import android.app.Application
import com.example.core.di.AppModule
import com.example.core.di.NetworkModule
import com.example.movies.actorfilms.di.ActorMovieCreditsComponent
import com.example.movies.actorfilms.di.ActorMovieCreditsModule
import com.example.movies.auth.di.MoviesAuthComponent
import com.example.movies.auth.di.MoviesAuthModule
import com.example.movies.nowplaying.di.NowPlayingComponent
import com.example.movies.nowplaying.di.NowPlayingModule
import com.example.movies.toprated.di.TopRatedComponent
import com.example.movies.toprated.di.TopRatedModule
import com.example.movies_details.di.MovieDetailsComponent
import com.example.movies_details.di.MovieDetailsModule
import com.example.movies_details.di.MovieDetailsPresentationModule
import com.example.movies_details.ui.MovieDetailsViewModelFactoryImpl
import com.example.movies_popular.di.PopularComponent
import com.example.movies_popular.di.PopularModule
import com.example.movies_rating.di.MoviesRatingComponent
import com.example.movies_rating.di.MoviesRatingModule
import com.example.movies_upcoming.di.UpcomingComponent
import com.example.movies_upcoming.di.UpcomingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AppModule::class,
        NetworkModule::class,
        MovieDetailsModule::class,
        MovieDetailsPresentationModule::class,
        NowPlayingModule::class,
        PopularModule::class,
        TopRatedModule::class,
        UpcomingModule::class,
        MoviesRatingModule::class,
        MoviesAuthModule::class,
        ActorMovieCreditsModule::class,
        ActivityBuildersModule::class,
        AndroidInjectionModule::class]
)
interface AppComponent : AndroidInjector<MyApp> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }


    override fun inject(instance: MyApp)

    fun nowPlayingComponent(): NowPlayingComponent.Factory
    fun actorMovieCreditsComponent(): ActorMovieCreditsComponent.Factory
    fun popularComponent(): PopularComponent.Factory
    fun topRatedComponent(): TopRatedComponent.Factory
    fun upcomingComponent(): UpcomingComponent.Factory
    fun movieDetailsFactoryImpl(): MovieDetailsViewModelFactoryImpl
    fun movieDetailsComponent(): MovieDetailsComponent.Factory
    fun moviesRatingComponent(): MoviesRatingComponent.Factory
    fun moviesAuthComponent(): MoviesAuthComponent.Factory
}
