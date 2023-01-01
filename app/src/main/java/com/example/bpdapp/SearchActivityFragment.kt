package com.example.bpdapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.bpdapp.databinding.FragmentHomeBinding
import com.example.bpdapp.databinding.FragmentSearchActivityBinding


class SearchActivityFragment : Fragment() {
    private var _binding:FragmentSearchActivityBinding?=null
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentSearchActivityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.manuallySearchButton.setOnClickListener{
            val action=SearchActivityFragmentDirections.actionSearchActivityFragmentToManualSearchFragment()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}