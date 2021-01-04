package com.lsurvila.callmonitortask

import android.app.role.RoleManager
import android.content.ContentResolver
import android.net.ConnectivityManager
import android.telecom.TelecomManager
import androidx.core.content.getSystemService
import com.lsurvila.callmonitortask.repository.ContactEntityMapper
import com.lsurvila.callmonitortask.repository.ContactRepository
import com.lsurvila.callmonitortask.repository.ContactResolverRepository
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.service.callmonitor.connection.ConnectionCallMonitor
import com.lsurvila.callmonitortask.service.callmonitor.receiver.BroadcastCallMonitor
import com.lsurvila.callmonitortask.service.callmonitor.screening.ScreeningCallMonitor
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
    single { CallEntityMapper() }
    single<ContactRepository> { ContactResolverRepository(get(), get()) }

    single<RoleManager?> { androidContext().getSystemService() }
    single<TelecomManager?> {androidContext().getSystemService() }
    single {
        if (VersionUtil.isQ()) {
            ConnectionCallMonitor(get(), androidContext())
        } else {
            BroadcastCallMonitor()
        }
    }
    single<ConnectivityManager?> { androidApplication().getSystemService() }
    single<NetworkService> { AndroidNetworkService(get()) }

    factory { GetCallMonitorStateUseCase(get()) }
    factory { StartCallMonitorUseCase(get()) }
    factory { StopCallMonitorUseCase(get()) }
    factory { ViewStateMapper() }
    viewModel { CallMonitorViewModel(get(), get(), get(), get()) }

    factory { LogOngoingCallUseCase(get(), get()) }

}