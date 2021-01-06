package com.lsurvila.callmonitortask.service.callmonitor.incall

import android.app.role.RoleManager
import android.os.Build
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class CallMonitorServiceImpl(private val telecomManager: TelecomManager?, private val roleManager: RoleManager?): CallMonitorService() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun isAvailable(): Boolean {
        return roleManager?.isRoleAvailable(RoleManager.ROLE_DIALER) == true
    }
}