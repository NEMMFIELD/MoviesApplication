package com.example.movies.auth.di

import androidx.lifecycle.ViewModel
import com.example.movies.auth.ui.MoviesAuthViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class MoviesAuthViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesAuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: MoviesAuthViewModel): ViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
