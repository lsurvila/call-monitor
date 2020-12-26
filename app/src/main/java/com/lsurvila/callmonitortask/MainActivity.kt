package com.lsurvila.callmonitortask

import android.os.Bundle
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
        val manager = CallMonitorServerManager(server)
        val model: CallMonitorViewModel by viewModels {
            CallMonitorViewModelFactory(manager)
        }
        viewModel = model
        viewModel.serverLiveData.observe(this, { message: String ->
            binding.messageView.text = message
        })

        binding.restartButton.setOnClickListener {
            stopServer()
            startServer()
        }
    }

    override fun onStart() {
        startServer()
        super.onStart()
    }

    private fun startServer() {
        viewModel.startServer(NetworkUtil.getWifiIpAddress(this))
    }

    override fun onStop() {
        stopServer()
        super.onStop()
    }

    private fun stopServer() {
        viewModel.stopServer()
    }
}