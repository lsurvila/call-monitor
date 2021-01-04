package com.lsurvila.callmonitortask.service.callmonitor.screening

import android.app.role.RoleManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

@RequiresApi(Build.VERSION_CODES.Q)
class CallMonitorScreeningService(private val roleManager: RoleManager?) : CallMonitorService() {

    override fun isAvailable(): Boolean {
        return roleManager?.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) == true
    }
}