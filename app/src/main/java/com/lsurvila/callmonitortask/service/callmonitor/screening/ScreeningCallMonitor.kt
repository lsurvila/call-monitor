package com.lsurvila.callmonitortask.service.callmonitor.screening

import android.app.role.RoleManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitor

@RequiresApi(Build.VERSION_CODES.Q)
class ScreeningCallMonitor(private val roleManager: RoleManager?) : CallMonitor() {

    override fun isAvailable(): Boolean {
        return roleManager?.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) == true
    }
}