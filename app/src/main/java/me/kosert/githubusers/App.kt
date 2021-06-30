package me.kosert.githubusers

import android.app.Application
import me.kosert.githubusers.modules.userRepositoryModule
import me.kosert.githubusers.modules.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(
                viewModelsModule,
                userRepositoryModule
            ))
        }

    }

    companion object {
        val isDebug
            get() = BuildConfig.DEBUG
    }
}