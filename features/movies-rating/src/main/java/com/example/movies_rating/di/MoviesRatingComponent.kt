package com.example.movies_rating.di

import com.example.movies_rating.ui.MoviesRatingViewModelFactory
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MoviesRatingModule::class
    ]
)
interface MoviesRatingComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesRatingComponent
    }
}
