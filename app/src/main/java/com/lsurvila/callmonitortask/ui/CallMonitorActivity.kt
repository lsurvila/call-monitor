package com.lsurvila.callmonitortask.ui

import android.Manifest
import android.app.role.RoleManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import pub.devrel.easypermissions.EasyPermissions


private const val REQUEST_PHONE_PERMISSION = 1
private const val REQUEST_CONTACTS_PERMISSION = 2

class CallMonitorActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val viewModel: CallMonitorViewModel by viewModel()
    private val roleManager: RoleManager? by inject()

    private var serviceToggle: SwitchCompat? = null

    private var currentIsCallButtonEnabled: Boolean? = null
    private var currentIsRejectButtonEnabled: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCallMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenResumed {
            viewModel.phone().collect { viewState ->
                Log.d("Liu", "phone updated $viewState")
                currentIsCallButtonEnabled = viewState.isAnswerButtonEnabled
                currentIsRejectButtonEnabled = viewState.isRejectButtonEnabled
                invalidateOptionsMenu()
                viewState.consoleMessage?.let { Console.writeLine(it) }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.service().collect { viewState ->
                Log.d("Liu", "service updated $viewState")
                viewState.serviceSwitchChecked?.let { serviceToggle?.isChecked = it }
                viewState.consoleMessage?.let { Console.writeLine(it) }
                viewState.askForPhonePermission?.let { askForPhonePermission() }
                viewState.checkForContactsPermission?.let { checkForContactsPermission() }
                viewState.askForContactsPermission?.let { askForContactsPermission() }
            }
        }
    }

    private fun checkForContactsPermission() {
        viewModel.onContactsPermissionGranted(EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS))
    }

    private fun askForContactsPermission() {
        EasyPermissions.requestPermissions(this, getString(R.string.contacts_rationale),
            REQUEST_CONTACTS_PERMISSION, Manifest.permission.READ_CONTACTS);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CONTACTS_PERMISSION && perms[0] == Manifest.permission.READ_CONTACTS) {
            viewModel.onContactsPermissionGranted(true)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CONTACTS_PERMISSION && perms[0] == Manifest.permission.READ_CONTACTS) {
            viewModel.onContactsPermissionDenied()
        }
    }

    private fun askForPhonePermission() {
        if (VersionUtil.isQ()) {
            val intent = roleManager?.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(intent, REQUEST_PHONE_PERMISSION)
        } else {
            // TODO
        }

        //        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
//            startActivityForResult(Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
//                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName), REQUEST_START_IN_CALL_SERVICE)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHONE_PERMISSION) {
            viewModel.onPhonePermissionGranted(resultCode == RESULT_OK)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        currentIsCallButtonEnabled?.let { menu.findItem(R.id.answer_item).isEnabled = it }
        currentIsRejectButtonEnabled?.let { menu.findItem(R.id.reject_item).isEnabled = it }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_call_monitor, menu)
        serviceToggle =
            menu.findItem(R.id.switch_item).actionView.findViewById(R.id.callMonitorSwitch)

        //viewModel.onStart()
        serviceToggle?.setOnCheckedChangeListener { button, isChecked ->
            viewModel.onServiceSwitched(button.isPressed, isChecked)
        }
        return true
    }

    private fun showSetPhonePermission() {
        if (VersionUtil.isN()) {
            openDefaultAppsSettings()
        } else {
            openAppSettings()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun openDefaultAppsSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
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
            R.id.phone_permission_item -> {
                showSetPhonePermission()
                true
            }
            R.id.contacts_permission_item -> {
                openAppSettings()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}