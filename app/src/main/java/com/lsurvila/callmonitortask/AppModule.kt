package com.lsurvila.callmonitortask

import android.app.role.RoleManager
import android.content.ContentResolver
import android.net.ConnectivityManager
import android.telecom.TelecomManager
import androidx.core.content.getSystemService
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.callmonitor.incall.CallMonitorServiceImpl
import com.lsurvila.callmonitortask.service.network.AndroidNetworkService
import com.lsurvila.callmonitortask.service.network.NetworkService
import com.lsurvila.callmonitortask.ui.CallMonitorViewModel
import com.lsurvila.callmonitortask.ui.ViewStateMapper
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ContentResolver> { androidContext().contentResolver }
    single { CallEntityMapper() }

    single<RoleManager?> { androidContext().getSystemService() }
    single<TelecomManager?> {androidContext().getSystemService() }
    single<CallMonitorService> { CallMonitorServiceImpl(get(), get()) }
    single<ConnectivityManager?> { androidApplication().getSystemService() }
    single<NetworkService> { AndroidNetworkService(get()) }

    factory { GetCallMonitorStateUseCase(get()) }
    factory { StartCallMonitorUseCase(get()) }
    factory { StopCallMonitorUseCase(get()) }
    factory { ViewStateMapper() }
    viewModel { CallMonitorViewModel(get(), get(), get(), get()) }

    factory { LogOngoingCallUseCase(get()) }
}