package com.example.movies.actorfilms.di

import dagger.Subcomponent

@Subcomponent(modules = [ActorMovieCreditsModule::class])
interface ActorMovieCreditsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActorMovieCreditsComponent
    }
}
