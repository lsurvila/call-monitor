package com.lsurvila.callmonitortask

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lsurvila.callmonitortask.databinding.ActivityMainBinding
import com.lsurvila.callmonitortask.util.NetworkUtil


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CallMonitorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val server = CallMonitorServer()
        val serverManager = CallMonitorServerManager(server)
        val telephonyManager = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val model: CallMonitorViewModel by viewModels {
            CallMonitorViewModelFactory(serverManager, telephonyManager)
        }
        viewModel = model
        viewModel.serverLiveData.observe(this, { message: String ->
            binding.messageView.text = message
        })
        viewModel.isOngoingCall().observe(this, { isOngoingCall: Boolean ->
            server.setIsOngoingCall(isOngoingCall)
        })

        binding.restartButton.setOnClickListener {
            stopServer()
            startServer()
        }
    }

    override fun onStart() {
        startServer()
        startCallMonitor()
        super.onStart()
    }

    private fun startServer() {
        viewModel.startServer(NetworkUtil.getWifiIpAddress(this))
    }

    private fun startCallMonitor() {
        viewModel.startCallMonitor()
    }

    override fun onStop() {
        stopServer()
        stopCallMonitor()
        super.onStop()
    }

    private fun stopServer() {
        viewModel.stopServer()
    }

    private fun stopCallMonitor() {
        viewModel.stopCallMonitor()
    }
}