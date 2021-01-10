package com.lsurvila.callmonitortask

import android.app.role.RoleManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telecom.TelecomManager
import androidx.core.content.getSystemService
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.service.callmonitor.incall.RoleCallMonitorService
import com.lsurvila.callmonitortask.service.callmonitor.incall.PackageCallMonitorService
import com.lsurvila.callmonitortask.service.network.AndroidNetworkService
import com.lsurvila.callmonitortask.service.network.NetworkService
import com.lsurvila.callmonitortask.ui.CallMonitorViewModel
import com.lsurvila.callmonitortask.ui.ViewStateMapper
import com.lsurvila.callmonitortask.util.VersionUtil
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { CallEntityMapper() }
    if (VersionUtil.isQ()) {
        single<RoleManager?> { androidContext().getSystemService() }
    }
    single<TelecomManager?> {androidContext().getSystemService() }
    single {
        if (VersionUtil.isQ()) {
            RoleCallMonitorService(get(), get())
        } else {
            PackageCallMonitorService(get(), get())
        }
    }
    single<ConnectivityManager?> { androidApplication().getSystemService() }
    single<WifiManager?> { androidApplication().getSystemService() }
    single<NetworkService> { AndroidNetworkService(get(), get()) }

    factory { ViewCallMonitorStateUseCase(get()) }
    factory { StartCallMonitorUseCase(get(), get()) }
    factory { StopCallMonitorUseCase(get()) }
    factory { ViewPhoneStateUseCase(get()) }
    factory { AnswerPhoneCallUseCase(get()) }
    factory { RejectPhoneCallUseCase(get()) }
    factory { ViewStateMapper() }
    viewModel { CallMonitorViewModel(get(), get(), get(), get(), get(), get(), get()) }
}