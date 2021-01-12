package com.lsurvila.callmonitortask.service.callmonitor

import android.Manifest
import android.content.Context
import android.telecom.TelecomManager
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import pub.devrel.easypermissions.EasyPermissions

class PackageCallMonitorService(private val telecomManager: TelecomManager?, private val context: Context,
                                callLogRepository: CallLogRepository
): CallMonitorService(callLogRepository) {

    override fun isAvailable(): Boolean {
        return telecomManager != null
    }

    override fun hasPhonePermission(): Boolean {
        return telecomManager?.defaultDialerPackage == context.packageName
    }

    override fun hasContactsPermission(): Boolean {
        return EasyPermissions.hasPermissions(context, Manifest.permission.READ_CONTACTS)
    }
}