package com.example.moviesapplication

import com.example.movies.nowplaying.di.NowPlayingModule
import com.example.movies_details.di.MovieDetailsPresentationModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(
        modules = [NowPlayingModule::class, MovieDetailsPresentationModule::class] // если нужно
    )
    abstract fun bindMainActivity(): MainActivity
}
