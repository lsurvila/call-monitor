package com.lsurvila.callmonitortask

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lsurvila.callmonitortask.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val server = CallMonitorServer(NetworkUtil.getWifiIpAddress(this))
        val model: CallMonitorViewModel by viewModels {
            CallMonitorViewModelFactory(server)
        }
        model.serverLiveData.observe(this, { message: String ->
            binding.messageView.text = message
        })
    }
}