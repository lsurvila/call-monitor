package com.lsurvila.callmonitortask.service.callmonitor.incall

import android.app.role.RoleManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

@RequiresApi(Build.VERSION_CODES.Q)
class CallMonitorServiceImpl(private val roleManager: RoleManager?): CallMonitorService() {

    override fun isAvailable(): Boolean {
        return roleManager?.isRoleAvailable(RoleManager.ROLE_DIALER) == true
    }

    override fun hasPhonePermission(): Boolean {
        return roleManager?.isRoleHeld(RoleManager.ROLE_DIALER) == true
    }
}