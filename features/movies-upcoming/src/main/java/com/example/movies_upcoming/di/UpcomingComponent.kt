package com.example.movies_upcoming.di

import dagger.Subcomponent

@Subcomponent(modules = [UpcomingModule::class])
interface UpcomingComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): UpcomingComponent
    }

}
