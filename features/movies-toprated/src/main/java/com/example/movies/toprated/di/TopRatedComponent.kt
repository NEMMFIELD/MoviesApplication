package com.example.movies.toprated.di

import dagger.Subcomponent

@Subcomponent(modules = [TopRatedModule::class])
interface TopRatedComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TopRatedComponent
    }
}
