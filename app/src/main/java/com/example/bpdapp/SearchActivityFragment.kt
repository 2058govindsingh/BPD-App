package com.example.bpdapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.bpdapp.databinding.FragmentHomeBinding
import com.example.bpdapp.databinding.FragmentSearchActivityBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


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
        binding.searchHamburger.setOnMenuItemClickListener {
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
                val action = HomeFragmentDirections.actionHomeFragmentToDashboardFragment()
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

        binding.manuallySearchButton.setOnClickListener{
            val action=SearchActivityFragmentDirections.actionSearchActivityFragmentToManualSearchFragment()
            view.findNavController().navigate(action)
        }
        binding.constraintFingerPrint.setOnClickListener {
            Toast.makeText(requireActivity(), "Fingerprint equipment is not connected.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}