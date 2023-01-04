package com.example.bpdapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.registerReceiver
import com.example.bpdapp.databinding.FragmentOneTapBinding
import kotlin.math.roundToInt

class OneTapFragment : Fragment() {
    private var _binding: FragmentOneTapBinding?=null
    private val binding get()=_binding!!
    private var timerStarted=false
    private lateinit var serviceIntent: Intent
    private var time=0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentOneTapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.startStopButton.setOnClickListener{ startStopTimer() }
        binding.saveButton.setOnClickListener{ saveTimer()}
        serviceIntent=Intent(this.requireContext(),TimerService::class.java)
        // bug chances
        requireActivity().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun saveTimer() {
        stopTimer()
        time=0.0
        binding.timer.text=getTimeStingFromDouble(time)
        Toast.makeText(this.requireContext(),"Activity stored in Database Successfully!!", Toast.LENGTH_LONG).show()
    }

    private fun startStopTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }
    private fun startTimer()
    {
        serviceIntent.putExtra(TimerService.TIME_EXTRA,time)
        requireActivity().startService(serviceIntent)
        binding.startStopButton.text="Stop"
        binding.startStopButton.icon= AppCompatResources.getDrawable(this.requireContext(),R.drawable.ic_stop_button)
        timerStarted=true
    }
    private fun stopTimer()
    {
        requireActivity().stopService(serviceIntent)
        binding.startStopButton.text="Start"
        binding.startStopButton.icon= AppCompatResources.getDrawable(this.requireContext(),R.drawable.ic_start_button)
        timerStarted=false
    }
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
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