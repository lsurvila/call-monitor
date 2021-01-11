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
import com.lsurvila.callmonitortask.service.http.mapper.CallMapper
import com.lsurvila.callmonitortask.service.http.mapper.ServicesMapper
import com.lsurvila.callmonitortask.service.http.mapper.UriMapper
import com.lsurvila.callmonitortask.service.network.AndroidNetworkService
import com.lsurvila.callmonitortask.service.network.NetworkService
import com.lsurvila.callmonitortask.ui.CallMonitorViewModel
import com.lsurvila.callmonitortask.ui.ViewStateMapper
import com.lsurvila.callmonitortask.ui.DateTimeMapper
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Use Cases (by App UI user)
    factory { ViewCallMonitorStateUseCase(get()) }
    factory { StartCallMonitorUseCase(get(), get(), get()) }
    factory { StopCallMonitorUseCase(get(), get()) }
    factory { ViewPhoneStateUseCase(get()) }
    factory { AnswerPhoneCallUseCase(get()) }
    factory { RejectPhoneCallUseCase(get()) }
    // Use Cases (by HTTP API user)
    factory { ViewServicesStatusUseCase(get()) }
    factory { ViewOngoingCallUseCase(get(), get(), get()) }

    // Entity to Domain mappers
    factory { ContactEntityMapper() }
    factory { CallEntityMapper() }
    // Domain to UI/HTTP mappers
    factory { UriMapper() }
    factory { DateTimeMapper() }
    factory { ViewStateMapper(get()) }
    factory { ServicesMapper(get(), get()) }
    factory { CallMapper() }

    // App UI
    viewModel { CallMonitorViewModel(get(), get(), get(), get(), get(), get(), get()) }
    // HTTP API
    single<HttpService> { KtorHttpService() }

    // Repositories and Services
    single<ContactRepository> { ResolverContactRepository(get(), get()) }
    single {
        if (VersionUtil.isQOrLater()) {
            RoleCallMonitorService(get(), get())
        } else {
            PackageCallMonitorService(get(), get())
        }
    }
    single<NetworkService> { AndroidNetworkService(get(), get(), get()) }

    // Android Infrastructure
    single<ContentResolver> { androidContext().contentResolver }
    if (VersionUtil.isQOrLater()) {
        single<RoleManager?> { androidContext().getSystemService() }
    }
    single<TelecomManager?> {androidContext().getSystemService() }
    single<ConnectivityManager?> { androidApplication().getSystemService() }
    single<WifiManager?> { androidApplication().getSystemService() }
}