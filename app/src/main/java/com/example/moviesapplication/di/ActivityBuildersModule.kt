package com.example.moviesapplication.di

import com.example.movies.nowplaying.di.NowPlayingModule
import com.example.movies_details.di.MovieDetailsPresentationModule
import com.example.moviesapplication.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(
        modules = [NowPlayingModule::class, MovieDetailsPresentationModule::class] // если нужно
    )
    abstract fun bindMainActivity(): MainActivity
}
