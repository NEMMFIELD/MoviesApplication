package com.example.movies.actorfilms.di

import androidx.lifecycle.ViewModelProvider
import com.example.movies.actorfilms.data.ActorMovieCreditsRepositoryImpl
import com.example.movies.actorfilms.domain.ActorMovieCreditsRepository
import com.example.movies.actorfilms.domain.GetActorMovieCreditsUseCase
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModelFactory
import com.example.movies.api.MoviesApi
import dagger.Module
import dagger.Provides

@Module(includes = [ActorMovieCreditsViewModelModule::class])
 class ActorMovieCreditsModule {
    @Provides
    fun provideNowPlayingViewModelFactory(useCase: GetActorMovieCreditsUseCase): ViewModelProvider.Factory {
        return ActorMovieCreditsViewModelFactory(useCase)
    }

    @Provides
    fun provideNowPlayingRepository(api: MoviesApi): ActorMovieCreditsRepository {
        return ActorMovieCreditsRepositoryImpl(api)
    }
}
