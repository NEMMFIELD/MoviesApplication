package com.example.movies_details.di

import com.example.movies_details.ui.MovieDetailsViewModelFactoryImpl
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MovieDetailsPresentationModule::class
    ]
)
interface MovieDetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MovieDetailsComponent
    }

    fun movieDetailsFactoryImpl():  MovieDetailsViewModelFactoryImpl
}
