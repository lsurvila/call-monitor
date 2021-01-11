package com.lsurvila.callmonitortask.di

import android.app.role.RoleManager
import android.content.ContentResolver
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telecom.TelecomManager
import androidx.core.content.getSystemService
import com.lsurvila.callmonitortask.*
import com.lsurvila.callmonitortask.repository.ContactEntityMapper
import com.lsurvila.callmonitortask.repository.ContactRepository
import com.lsurvila.callmonitortask.repository.ResolverContactRepository
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.service.callmonitor.RoleCallMonitorService
import com.lsurvila.callmonitortask.service.callmonitor.PackageCallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService
import com.lsurvila.callmonitortask.service.http.KtorHttpService
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
    single<ContentResolver> { androidContext().contentResolver }
    single { ContactEntityMapper() }
    single<ContactRepository> { ResolverContactRepository(get(), get()) }

    single { CallEntityMapper() }
    if (VersionUtil.isQOrLater()) {
        single<RoleManager?> { androidContext().getSystemService() }
    }
    single<TelecomManager?> {androidContext().getSystemService() }
    single {
        if (VersionUtil.isQOrLater()) {
            RoleCallMonitorService(get(), get())
        } else {
            PackageCallMonitorService(get(), get())
        }
    }
    single<ConnectivityManager?> { androidApplication().getSystemService() }
    single<WifiManager?> { androidApplication().getSystemService() }
    single<NetworkService> { AndroidNetworkService(get(), get()) }
    single<HttpService> { KtorHttpService() }

    factory { ViewCallMonitorStateUseCase(get()) }
    factory { StartCallMonitorUseCase(get(), get(), get()) }
    factory { StopCallMonitorUseCase(get(), get()) }
    factory { ViewPhoneStateUseCase(get()) }
    factory { AnswerPhoneCallUseCase(get()) }
    factory { RejectPhoneCallUseCase(get()) }
    factory { LogOngoingCallUseCase(get()) }
    factory { ViewStateMapper() }
    viewModel { CallMonitorViewModel(get(), get(), get(), get(), get(), get(), get()) }
}