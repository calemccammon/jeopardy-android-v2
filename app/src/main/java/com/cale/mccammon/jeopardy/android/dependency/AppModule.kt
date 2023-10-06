package com.cale.mccammon.jeopardy.android.dependency

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [JeopardyModule::class])
@InstallIn(SingletonComponent::class)
@Suppress("unused")
class AppModule