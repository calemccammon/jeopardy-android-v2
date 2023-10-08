package com.cale.mccammon.jeopardy.android.dependency

import com.cale.mccammon.jeopardy.android.BuildConfig
import com.cale.mccammon.jeopardy.feature.data.JeopardyRepository
import com.cale.mccammon.jeopardy.feature.data.JeopardyRepositoryImpl
import com.cale.mccammon.jeopardy.feature.data.JeopardyNetwork
import com.cale.mccammon.jeopardy.feature.data.JeopardyNetworkImpl
import com.cale.mccammon.jeopardy.feature.data.JeopardyNetworkConfig
import com.cale.mccammon.jeopardy.feature.domain.JeopardyComponent
import com.cale.mccammon.jeopardy.feature.domain.JeopardyLogger
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.migration.DisableInstallInCheck
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

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

    @Provides
    fun provideLogger() = object : JeopardyLogger {
        override fun d(message: String) {
            Timber.d(message)
        }

        override fun e(ex: Throwable) {
            Timber.e(ex)
        }
    }
}

@Module(includes = [ProvidesJeopardyModule::class])
@InstallIn(ViewModelComponent::class)
@Suppress("unused")
abstract class JeopardyModule {
    @Binds
    abstract fun bindJeopardyComponent(jeopardyComponentImpl: JeopardyComponentImpl): JeopardyComponent
}