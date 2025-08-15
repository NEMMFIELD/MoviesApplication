package com.example.movies_rating.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies_rating.ui.MoviesRatingViewModel
import com.example.movies_rating.ui.MoviesRatingViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class MoviesRatingViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesRatingViewModel::class)
    abstract fun bindMoviesRatingViewModel(viewModel: MoviesRatingViewModel): ViewModel
}
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
