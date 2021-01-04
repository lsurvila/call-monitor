package com.lsurvila.callmonitortask.ui

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.jraska.console.Console
import com.lsurvila.callmonitortask.R
import com.lsurvila.callmonitortask.databinding.ActivityCallMonitorBinding
import com.lsurvila.callmonitortask.util.VersionUtil
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


private const val REQUEST_START_CALL_SCREENING = 1

class CallMonitorActivity : AppCompatActivity() {

    private val viewModel: CallMonitorViewModel by viewModel()
    private val roleManager: RoleManager? by inject()

    private lateinit var serviceToggle: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCallMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.service().observe(this, { viewState ->
            serviceToggle.isChecked = viewState.serviceSwitchChecked
            viewState.consoleMessage?.let { Console.writeLine(it) }
            viewState.toggleService?.let { toggleCallService(it) }
        })
    }

    private fun toggleCallService(start: Boolean) {
        if (start) {
            if (VersionUtil.isQ()) {
                startConnectionService()
            }
        } else {
            if (VersionUtil.isQ()) {
                stopConnectionService()
            }
        }
    }

    private fun stopConnectionService() {
        viewModel.stopService(false)
    }

    private fun startConnectionService() {
        viewModel.startService(true)
//        startActivity(Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS));
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startCallScreeningService() {
        val intent = roleManager?.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
        startActivityForResult(intent, REQUEST_START_CALL_SCREENING)
    }

    private fun stopCallScreeningService() {
        // There doesn't seem to be API to remove call screening role unless uninstalling app
        viewModel.stopService(true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_START_CALL_SCREENING) {
            viewModel.startService(resultCode == RESULT_OK)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_call_monitor, menu)
        serviceToggle = menu.findItem(R.id.switch_item).actionView.findViewById(R.id.callMonitorSwitch)
        viewModel.syncServiceToggle()
        serviceToggle.setOnCheckedChangeListener { button, isChecked ->
            viewModel.toggleService(button.isPressed, isChecked)
        }
        return true
    }
}