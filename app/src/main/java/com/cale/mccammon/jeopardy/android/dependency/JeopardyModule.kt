package com.cale.mccammon.jeopardy.android.dependency

import com.cale.mccammon.jeopardy.module.data.JeopardyRepository
import com.cale.mccammon.jeopardy.module.data.JeopardyRepositoryImpl
import com.cale.mccammon.jeopardy.module.data.Network
import com.cale.mccammon.jeopardy.module.data.NetworkImpl
import com.cale.mccammon.jeopardy.module.domain.JeopardyComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.migration.DisableInstallInCheck

@Module
@DisableInstallInCheck
class ProvidesJeopardyModule {
    @Provides
    fun provideNetwork(): Network = NetworkImpl()

    @Provides
    fun provideRepository(network: Network): JeopardyRepository =
        JeopardyRepositoryImpl(network)
}

@Module(includes = [ProvidesJeopardyModule::class])
@InstallIn(ActivityComponent::class)
abstract class JeopardyModule {
    @Binds
    abstract fun bindJeopardyComponent(jeopardyComponentImpl: JeopardyComponentImpl): JeopardyComponent
}