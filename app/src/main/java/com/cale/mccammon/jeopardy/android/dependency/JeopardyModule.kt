package com.cale.mccammon.jeopardy.android.dependency

import com.cale.mccammon.jeopardy.android.BuildConfig
import com.cale.mccammon.jeopardy.module.data.JeopardyRepository
import com.cale.mccammon.jeopardy.module.data.JeopardyRepositoryImpl
import com.cale.mccammon.jeopardy.module.data.JeopardyNetwork
import com.cale.mccammon.jeopardy.module.data.JeopardyNetworkImpl
import com.cale.mccammon.jeopardy.module.data.JeopardyNetworkConfig
import com.cale.mccammon.jeopardy.module.domain.JeopardyComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.migration.DisableInstallInCheck
import okhttp3.logging.HttpLoggingInterceptor

@Module
@DisableInstallInCheck
class ProvidesJeopardyModule {
    @Provides
    fun provideNetwork(): JeopardyNetwork = JeopardyNetworkImpl(
        JeopardyNetworkConfig(
            "http://jservice.io/api/",
            if (BuildConfig.DEBUG) {
                listOf(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            } else {
                emptyList()
            }
        )
    )

    @Provides
    fun provideRepository(network: JeopardyNetwork): JeopardyRepository =
        JeopardyRepositoryImpl(network)
}

@Module(includes = [ProvidesJeopardyModule::class])
@InstallIn(ViewModelComponent::class)
@Suppress("unused")
abstract class JeopardyModule {
    @Binds
    abstract fun bindJeopardyComponent(jeopardyComponentImpl: JeopardyComponentImpl): JeopardyComponent
}