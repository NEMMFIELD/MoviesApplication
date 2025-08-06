package com.example.movies.nowplaying.di

import dagger.Subcomponent

@Subcomponent(modules = [NowPlayingModule::class])
interface NowPlayingComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): NowPlayingComponent
    }
}

