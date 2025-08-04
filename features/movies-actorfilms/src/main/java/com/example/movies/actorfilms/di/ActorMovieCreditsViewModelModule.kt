package com.example.movies.actorfilms.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModel
import com.example.movies.actorfilms.ui.ActorMovieCreditsViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ActorMovieCreditsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActorMovieCreditsViewModel::class)
    abstract fun bindActorMovieCreditsViewModel(viewModel: ActorMovieCreditsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ActorMovieCreditsViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
