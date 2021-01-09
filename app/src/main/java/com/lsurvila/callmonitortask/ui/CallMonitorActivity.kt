package com.lsurvila.callmonitortask.ui

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import com.jraska.console.Console
import com.lsurvila.callmonitortask.R
import com.lsurvila.callmonitortask.databinding.ActivityCallMonitorBinding
import com.lsurvila.callmonitortask.util.VersionUtil
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


private const val REQUEST_START_IN_CALL_SERVICE = 1

class CallMonitorActivity : AppCompatActivity() {

    private val viewModel: CallMonitorViewModel by viewModel()
    private val roleManager: RoleManager? by inject()

    private var serviceToggle: SwitchCompat? = null

    private var currentViewState: CallMonitorViewState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCallMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.service().observe(this, { viewState ->
            viewState.serviceSwitchChecked?.let { serviceToggle?.isChecked = it }
            viewState.consoleMessage?.let { Console.writeLine(it) }
            viewState.toggleService?.let { toggleCallService(it) }
        })
        lifecycleScope.launchWhenResumed {
            viewModel.phone().collect { viewState ->
                currentViewState = viewState
                viewState.consoleMessage?.let { Console.writeLine(it) }
                invalidateOptionsMenu()
            }
        }
    }

    private fun toggleCallService(start: Boolean) {
        if (start) {
            if (VersionUtil.isQ()) {
                startInCallService()
            }
        } else {
            if (VersionUtil.isQ()) {
                stopInCallService()
            }
        }
    }

    private fun stopInCallService() {
        viewModel.stopService(true)

//        if (getSystemService(TelecomManager::class.java).defaultDialerPackage == packageName) {
//            startActivityForResult(Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
//                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getSystemService(TelecomManager::class.java).defaultDialerPackage), REQUEST_STOP_IN_CALL_SERVICE)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startInCallService() {
        val intent = roleManager?.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        startActivityForResult(intent, REQUEST_START_IN_CALL_SERVICE)

//        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
//            startActivityForResult(Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
//                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName), REQUEST_START_IN_CALL_SERVICE)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_START_IN_CALL_SERVICE) {
            viewModel.startService(resultCode == RESULT_OK)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        currentViewState?.let { viewState ->
            viewState.isAnswerButtonEnabled?.let { menu.findItem(R.id.answer_item).isEnabled = it }
            viewState.isRejectButtonEnabled?.let { menu.findItem(R.id.reject_item).isEnabled = it }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_call_monitor, menu)
        serviceToggle =
            menu.findItem(R.id.switch_item).actionView.findViewById(R.id.callMonitorSwitch)

        //viewModel.onStart()
        serviceToggle?.setOnCheckedChangeListener { button, isChecked ->
            viewModel.toggleService(button.isPressed, isChecked)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.answer_item -> {
                viewModel.onAnswerClicked()
                true
            }
            R.id.reject_item -> {
                viewModel.onRejectClicked()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}