package com.lsurvila.callmonitortask

import android.app.role.RoleManager
import android.net.ConnectivityManager
import android.os.Build
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
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
    single<NetworkService> { AndroidNetworkService(get()) }

    factory { GetCallMonitorStateUseCase(get()) }
    factory { StartCallMonitorUseCase(get()) }
    factory { StopCallMonitorUseCase(get()) }
    factory { GetPhoneStateUseCase(get()) }
    factory { AnswerPhoneCallUseCase(get()) }
    factory { RejectPhoneCallUseCase(get()) }
    factory { ViewStateMapper() }
    viewModel { CallMonitorViewModel(get(), get(), get(), get(), get(), get(), get()) }
}