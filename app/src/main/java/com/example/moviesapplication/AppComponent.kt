package com.example.moviesapplication

import android.app.Application
import com.example.core.di.AppModule
import com.example.core.di.NetworkModule
import com.example.movies.nowplaying.di.NowPlayingComponent
import com.example.movies.nowplaying.di.NowPlayingModule
import com.example.movies_details.di.MovieDetailsComponent
import com.example.movies_details.di.MovieDetailsModule
import com.example.movies_details.di.MovieDetailsPresentationModule
import com.example.movies_details.ui.MovieDetailsViewModelFactoryImpl
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjection
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class,
    NetworkModule::class,
    MovieDetailsModule::class,
    MovieDetailsPresentationModule::class,
    NowPlayingModule::class,
    ActivityBuildersModule::class,
    AndroidInjectionModule::class])
interface AppComponent: AndroidInjector<MyApp> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }


    override fun inject(instance: MyApp)

    fun nowPlayingComponent(): NowPlayingComponent.Factory

    fun movideDetailsFactoryImpl(): MovieDetailsViewModelFactoryImpl
    fun movieDetailsComponent(): MovieDetailsComponent.Factory
}
