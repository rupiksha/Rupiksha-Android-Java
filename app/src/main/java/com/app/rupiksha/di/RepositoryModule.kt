package com.app.rupiksha.di

import com.app.rupiksha.data.repository.*
import com.app.rupiksha.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        walletRepositoryImpl: WalletRepositoryImpl
    ): WalletRepository

    @Binds
    @Singleton
    abstract fun bindRechargeRepository(
        rechargeRepositoryImpl: RechargeRepositoryImpl
    ): RechargeRepository

    @Binds
    @Singleton
    abstract fun bindBbpsRepository(
        bbpsRepositoryImpl: BbpsRepositoryImpl
    ): BbpsRepository

    @Binds
    @Singleton
    abstract fun bindAepsRepository(
        aepsRepositoryImpl: AepsRepositoryImpl
    ): AepsRepository

    @Binds
    @Singleton
    abstract fun bindDmtRepository(
        dmtRepositoryImpl: DmtRepositoryImpl
    ): DmtRepository
}