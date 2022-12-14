package com.example.bpdapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.example.bpdapp.databinding.ActivityLoginBinding
import com.example.bpdapp.databinding.ActivityOneTapBinding
import kotlin.math.roundToInt

class OneTapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOneTapBinding
    private var timerStarted=false
    private lateinit var serviceIntent:Intent
    private var time=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOneTapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startStopButton.setOnClickListener{ startStopTimer() }
        binding.saveButton.setOnClickListener{ saveTimer()}
        serviceIntent=Intent(this,TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }

    private fun saveTimer() {
        stopTimer()
        time=0.0
        binding.timer.text=getTimeStingFromDouble(time)
        Toast.makeText(this,"Activity stored in Database Successfully!!",Toast.LENGTH_LONG).show()
    }

    private fun startStopTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }
    private fun startTimer()
    {
        Toast.makeText(this,"this is start timer",Toast.LENGTH_SHORT).show()
        serviceIntent.putExtra(TimerService.TIME_EXTRA,time)
        startService(serviceIntent)
        binding.startStopButton.text="Stop"
        binding.startStopButton.icon=AppCompatResources.getDrawable(this,R.drawable.ic_start_button)
        timerStarted=true
    }
    private fun stopTimer()
    {
        stopService(serviceIntent)
        binding.startStopButton.text="Start"
        binding.startStopButton.icon=AppCompatResources.getDrawable(this,R.drawable.ic_stop_button)
        timerStarted=false
    }
    private val updateTime: BroadcastReceiver= object :BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent) {
            time =intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
            binding.timer.text=getTimeStingFromDouble(time)
        }
    }
    private fun getTimeStingFromDouble(time:Double):String
    {
        val resultInt=time.roundToInt()
        val hours=resultInt%86400/3600
        val minutes=resultInt%86400%3600/60
        val seconds=resultInt%86400%3600%60
        return makeTimeString(hours,minutes,seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d",hour,min,sec)

}