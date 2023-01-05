package com.example.bpdapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.bpdapp.dao.PoliceDao
import com.example.bpdapp.models.Police
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DashboardFragment : Fragment() {

    private var _binding: com.example.bpdapp.databinding.FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = com.example.bpdapp.databinding.FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dashboardTpLogo.visibility = View.GONE
        binding.dashboardInspectorPhoto.visibility = View.GONE
        binding.dashboardName.visibility = View.GONE
        binding.dashboardDesignation.visibility = View.GONE
        binding.dashboardPoliceStation.visibility = View.GONE
        binding.dashboardInspectorRecordsHistory.visibility = View.GONE


        val currentPoliceEmail = auth.currentUser!!.email!!
        GlobalScope.launch {
            val policeDao = PoliceDao()
            val police = policeDao.getPoliceByEmail(currentPoliceEmail).await()
                .toObject(Police::class.java)!!
            withContext(Dispatchers.Main) {
                binding.dashboardName.text = police.name
                binding.dashboardDesignation.text = police.designation
                binding.dashboardPoliceStation.text = police.policeStation
                binding.dashboardTotalRecords.text = police.cases.toString()
                Glide.with(binding.dashboardInspectorPhoto.context).load(police.imageUrl).centerCrop().into(binding.dashboardInspectorPhoto)

                binding.dashboardProgressBar.visibility = View.GONE
                binding.dashboardTpLogo.visibility = View.VISIBLE
                binding.dashboardInspectorPhoto.visibility = View.VISIBLE
                binding.dashboardName.visibility = View.VISIBLE
                binding.dashboardDesignation.visibility = View.VISIBLE
                binding.dashboardPoliceStation.visibility = View.VISIBLE
                binding.dashboardInspectorRecordsHistory.visibility = View.VISIBLE
            }
        }
    }

}