package com.example.movies.nowplaying.di

import androidx.lifecycle.ViewModelProvider
import com.example.movies.api.MoviesApi
import com.example.movies.nowplaying.data.NowPlayingRepositoryImpl
import com.example.movies.nowplaying.domain.NowPlayingMoviesUseCase
import com.example.movies.nowplaying.domain.NowPlayingRepository
import com.example.movies.nowplaying.ui.NowPlayingViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [NowPlayingViewModelModule::class])
class NowPlayingModule {

    @Provides
    fun provideNowPlayingViewModelFactory(useCase: NowPlayingMoviesUseCase): ViewModelProvider.Factory {
        return NowPlayingViewModelFactory(useCase)
    }

    @Provides
    fun provideNowPlayingRepository(api: MoviesApi): NowPlayingRepository {
        return NowPlayingRepositoryImpl(api)
    }
}
