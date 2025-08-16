package com.example.movies.auth.di

import dagger.Subcomponent

@Subcomponent(modules = [MoviesAuthModule::class])
interface MoviesAuthComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesAuthComponent
    }
}
