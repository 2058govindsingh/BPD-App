package com.example.bpdapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bpdapp.adapter.CriminalAdapter
import com.example.bpdapp.adapter.CriminalClicked
import com.example.bpdapp.dao.CriminalDao
import com.example.bpdapp.dao.PoliceDao
import com.example.bpdapp.databinding.FragmentManualSearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ManualSearchFragment : Fragment(), CriminalClicked {

    private var _binding: FragmentManualSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CriminalAdapter
    private val policeDao = PoliceDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentManualSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener = this
        binding.manualSearchSearch.setOnClickListener {
            val name = binding.criminalNameInput.text.toString()
            val phone = binding.contactNumberCriminalInput.text.toString()
            val aadhaar = binding.aadhaarNumberCriminalInput.text.toString()

            GlobalScope.launch {
                val criminalDao = CriminalDao()
                val data = criminalDao.checkCriminal(name, phone, aadhaar)
                withContext(Dispatchers.Main) {
                    if (data.isEmpty())
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                    else {
                        adapter = CriminalAdapter(data, listener)
                        binding.criminalList.adapter = adapter
                    }
                }
            }
        }
    }

    override fun onCriminalClicked() {
        policeDao.updateCount()
        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
    }

}