package com.lsurvila.callmonitortask.service.callmonitor

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import pub.devrel.easypermissions.EasyPermissions

@RequiresApi(Build.VERSION_CODES.Q)
class RoleCallMonitorService(private val roleManager: RoleManager?, private val context: Context,
                             callLogRepository: CallLogRepository
): CallMonitorService(callLogRepository) {

    override fun isAvailable(): Boolean {
        return roleManager?.isRoleAvailable(RoleManager.ROLE_DIALER) == true
    }

    override fun hasPhonePermission(): Boolean {
        return roleManager?.isRoleHeld(RoleManager.ROLE_DIALER) == true
    }

    override fun hasContactsPermission(): Boolean {
        return EasyPermissions.hasPermissions(context, Manifest.permission.READ_CONTACTS)
    }
}