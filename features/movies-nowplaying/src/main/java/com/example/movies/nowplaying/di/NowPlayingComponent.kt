package com.example.movies.nowplaying.di

import androidx.lifecycle.SavedStateHandle
import com.example.movies.nowplaying.ui.NowPlayingViewModel
import dagger.Subcomponent
import dagger.assisted.AssistedFactory

@Subcomponent(modules = [NowPlayingModule::class])
interface NowPlayingComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): NowPlayingComponent
    }
}

