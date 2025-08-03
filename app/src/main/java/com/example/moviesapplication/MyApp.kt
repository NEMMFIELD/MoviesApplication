package com.example.moviesapplication

import android.app.Application
import com.example.movies.nowplaying.di.NowPlayingComponentProvider
import com.example.movies.nowplaying.di.NowPlayingComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MyApp : Application(), NowPlayingComponentProvider, HasAndroidInjector{

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
    }

    override fun provideNowPlayingComponent(): NowPlayingComponent {
        return appComponent.nowPlayingComponent().create()
    }

    override fun androidInjector(): AndroidInjector<in Any>? {
        return androidInjector
    }

}
