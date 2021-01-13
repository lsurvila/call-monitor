package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService
import com.lsurvila.callmonitortask.service.network.NetworkService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.URI

class StartCallMonitorUseCaseTest {

    private val callLogRepository = mockk<CallLogRepository>(relaxed = true)
    private val callMonitorService = mockk<CallMonitorService>(relaxed = true)
    private val networkService = mockk<NetworkService>(relaxed = true)
    private val httpService = mockk<HttpService>(relaxed = true)
    private val useCase = StartCallMonitorUseCase(callLogRepository, callMonitorService, networkService, httpService)

    @Test
    fun `checkIfAvailable not available`() {
        every { callMonitorService.isAvailable() } returns false

        runBlocking {
            useCase.checkIfAvailable()
        }

        verify { callMonitorService.setServiceState(State.PHONE_NOT_AVAILABLE) }
        verify { callMonitorService.setServiceState(State.NOT_STARTED) }
    }

    @Test
    fun `checkIfAvailable phone permission needed`() {
        every { callMonitorService.isAvailable() } returns true
        every { callMonitorService.hasPhonePermission() } returns false

        runBlocking {
            useCase.checkIfAvailable()
        }

        verify { callMonitorService.setServiceState(State.PHONE_PERMISSION_NEEDED) }
    }

    @Test
    fun `checkIfAvailable contacts permission needed`() {
        every { callMonitorService.isAvailable() } returns true
        every { callMonitorService.hasPhonePermission() } returns true
        every { callMonitorService.hasContactsPermission() } returns false

        runBlocking {
            useCase.checkIfAvailable()
        }

        verify { callMonitorService.setServiceState(State.CONTACTS_PERMISSION_NEEDED) }
    }

    @Test
    fun `checkIfAvailable wifi disconnected`() {
        every { callMonitorService.isAvailable() } returns true
        every { callMonitorService.hasPhonePermission() } returns true
        every { callMonitorService.hasContactsPermission() } returns true
        every { networkService.isWifiConnected() } returns false

        runBlocking {
            useCase.checkIfAvailable()
        }

        verify { callMonitorService.setServiceState(State.WIFI_DISCONNECTED) }
        verify { callMonitorService.setServiceState(State.NOT_STARTED) }
    }

    @Test
    fun `checkIfAvailable wifi ip failed to resolve`() {
        every { callMonitorService.isAvailable() } returns true
        every { callMonitorService.hasPhonePermission() } returns true
        every { callMonitorService.hasContactsPermission() } returns true
        every { networkService.isWifiConnected() } returns true
        every { runBlocking { networkService.getWifiAddress(HttpService.PORT) }} returns null

        runBlocking {
            useCase.checkIfAvailable()
        }

        verify { callMonitorService.setServiceState(State.WIFI_IP_FAILED_TO_RESOLVE) }
        verify { callMonitorService.setServiceState(State.NOT_STARTED) }
    }

    @Test
    fun `checkIfAvailable all conditions met start server`() {
        val address = URI.create("http://localhost:12345")
        every { callMonitorService.isAvailable() } returns true
        every { callMonitorService.hasPhonePermission() } returns true
        every { callMonitorService.hasContactsPermission() } returns true
        every { networkService.isWifiConnected() } returns true
        every { runBlocking { networkService.getWifiAddress(HttpService.PORT) }} returns address
        every { runBlocking { httpService.start(address) }} returns CallMonitorState(State.STARTED, address)

        runBlocking {
            useCase.checkIfAvailable()
        }

        verify { callMonitorService.setServiceState(CallMonitorState(State.STARTED, address)) }
        verify { callLogRepository.delete() }
    }

    @Test
    fun `handlePhonePermission permission denied`() {
        useCase.handlePhonePermission(false)

        verify { callMonitorService.setServiceState(State.PHONE_PERMISSION_DENIED) }
        verify { callMonitorService.setServiceState(State.NOT_STARTED) }
    }

    @Test
    fun `handlePhonePermission granted ask for contacts permission`() {
        useCase.handlePhonePermission(true)

        verify { callMonitorService.setServiceState(State.CONTACTS_PERMISSION_NEEDED) }
    }

    @Test
    fun `handleContactPermission permission denied`() {
        runBlocking {
            useCase.handleContactPermission(false)
        }

        verify { callMonitorService.setServiceState(State.CONTACTS_PERMISSION_DENIED) }
        verify { callMonitorService.setServiceState(State.NOT_STARTED) }
    }

    @Test
    fun `handleContactPermission conditions met start server`() {
        val address = URI.create("http://localhost:12345")
        every { networkService.isWifiConnected() } returns true
        every { runBlocking { networkService.getWifiAddress(HttpService.PORT) }} returns address
        every { runBlocking { httpService.start(address) }} returns CallMonitorState(State.STARTED, address)

        runBlocking {
            useCase.handleContactPermission(true)
        }

        verify { callMonitorService.setServiceState(CallMonitorState(State.STARTED, address)) }
        verify { callLogRepository.delete() }
    }
}