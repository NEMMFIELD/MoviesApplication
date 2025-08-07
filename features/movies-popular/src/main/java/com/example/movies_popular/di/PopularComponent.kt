package com.example.movies_popular.di

import dagger.Subcomponent

@Subcomponent(modules = [PopularModule::class])
interface PopularComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): PopularComponent
    }

}
