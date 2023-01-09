package com.example.bpdapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.bpdapp.adapter.CriminalAdapter
import com.example.bpdapp.adapter.CriminalClicked
import com.example.bpdapp.dao.CriminalDao
import com.example.bpdapp.dao.PoliceDao
import com.example.bpdapp.databinding.FragmentManualSearchBinding
import com.example.bpdapp.models.Criminal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

        binding.manualHamburger.setOnMenuItemClickListener {
            if (it.itemId == R.id.log_out_button) {
                MaterialAlertDialogBuilder(this.requireContext())
                    .setTitle(resources.getString(R.string.log_out))
                    .setMessage(resources.getString(R.string.log_out_confirmation))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.log_out)) { dialog, which ->
                        // Respond to positive button press
                        Firebase.auth.signOut()
                        val loginActivityIntent = Intent(this.requireContext(), LoginActivity::class.java)
                        startActivity(loginActivityIntent)
                        requireActivity().finish()
                    }
                    .show()
                true
            }
            else if(it.itemId == R.id.dashboard_button)
            {
                val action = ManualSearchFragmentDirections.actionManualSearchFragmentToDashboardFragment()
                view.findNavController().navigate(action)
                true
            }
            else if(it.itemId == R.id.contact_us_button)
            {
                val dialogBuilder = AlertDialog.Builder(this.requireActivity())
                val inflater = this.layoutInflater
                val dialogView = inflater.inflate(R.layout.contact_us_dialog, null)
                dialogBuilder.setView(dialogView)

                dialogBuilder.setTitle("CONTACT US VIA")

                dialogView.findViewById<Button>(R.id.compose_email_button).setOnClickListener{
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("govindyadav2058@gmail.com") )
                    intent.type = "message/rfc822"
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"))
                }
                dialogView.findViewById<Button>(R.id.dial_number_button).setOnClickListener{
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:08460379804")
                    startActivity(intent)
                }
                val b = dialogBuilder.create()
                b.show()
                true
            }
            else if(it.itemId==R.id.about_us_button)
            {
                MaterialAlertDialogBuilder(this.requireContext())
                    .setTitle("ABOUT US")
                    .setMessage(resources.getString(R.string.about_tripura_police))
                    .show()
                true
            }
            else
                false
        }

        val listener = this
        binding.searchButton.setOnClickListener {
            val name = binding.criminalNameEditText.text.toString()
            val phone = binding.contactNumberEditText.text.toString()
            val aadhaar = binding.aadhaarNumberEditText.text.toString()

            if (name == "" && phone == "" && aadhaar == "") {
                val data = mutableListOf<Criminal>()
                adapter = CriminalAdapter(data, listener)
                binding.criminalList.adapter = adapter
                Toast.makeText(requireActivity(), "Please enter details.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.searchButton.visibility = View.GONE
            binding.firebaseProgressBar.visibility = View.VISIBLE

            GlobalScope.launch {
                val criminalDao = CriminalDao()
                val data = criminalDao.checkCriminal(name, phone, aadhaar)
                withContext(Dispatchers.Main) {
                    if (data.isEmpty())
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                    adapter = CriminalAdapter(data, listener)
                    binding.criminalList.adapter = adapter
                    binding.searchButton.visibility = View.VISIBLE
                    binding.firebaseProgressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onCriminalClicked() {
        policeDao.updateCount()
        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
    }

}