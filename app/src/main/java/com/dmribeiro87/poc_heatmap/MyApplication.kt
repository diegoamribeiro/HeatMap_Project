package com.dmribeiro87.poc_heatmap

import android.app.Application
import com.dmribeiro87.poc_heatmap.ui.viewmodel.HeatMapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

    private val appModule = module {
        factory { Repository(get())}
        viewModel { HeatMapViewModel(get()) }

    }
}