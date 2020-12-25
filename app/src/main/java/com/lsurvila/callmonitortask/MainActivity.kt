package com.lsurvila.callmonitortask

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lsurvila.callmonitortask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val httpServer = HttpServer()
        val server = CallMonitorServer(httpServer)
        val model: CallMonitorViewModel by viewModels {
            CallMonitorViewModelFactory(server)
        }
        model.serverLiveData.observe(this, { message: String ->
            binding.messageView.text = message
        })

        restartServer(model)
        binding.restartButton.setOnClickListener {
            restartServer(model)
        }
    }

    private fun restartServer(model: CallMonitorViewModel) {
        model.restartServer(NetworkUtil.getWifiIpAddress(this))
    }
}