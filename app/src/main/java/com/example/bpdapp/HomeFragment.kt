package com.example.bpdapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.bpdapp.databinding.FragmentHomeBinding
import com.example.bpdapp.models.Police
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? =null
    private val binding get() = _binding!!
    private val REQUEST_VIDEO_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.homeHamburger.setOnMenuItemClickListener {
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

        binding.searchButton.setOnClickListener{

            val action= HomeFragmentDirections.actionHomeFragmentToSearchActivityFragment2()
            view.findNavController().navigate(action)

        }

        binding.oneTapActionButton.setOnClickListener{
            startRecording()

        }
    }


    private fun startRecording() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            val videoUri = data?.data
            // You can do something with the videoUri, such as uploading it to Firebase
            if (videoUri != null) {
                uploadToFirebase(videoUri)
            }
        }
    }

    private fun uploadToFirebase(uri: Uri) {
        binding.oneTapActionButton.visibility = View.GONE
        binding.videoProgressBar.visibility = View.VISIBLE
        binding.videoProgressBar.progress = 0
        binding.textViewOneTapActive.text = "Uploading..."
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val videoFileName = "VID_$timeStamp"
        val storage = Firebase.storage
        val storageRef = storage.reference
        val videoRef = storageRef.child("videos/$videoFileName")
        val uploadTask = videoRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            // Upload succeeded
            videoRef.downloadUrl.addOnSuccessListener {
                // Get the download URL
                val downloadUrl = it.toString()
                // Do something with the download URL

                binding.videoProgressBar.visibility = View.GONE
                binding.oneTapActionButton.visibility = View.VISIBLE
                binding.textViewOneTapActive.text = "One Tap Active"
                Toast.makeText(requireActivity(), "Video is uploaded successfully.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            // Upload failed
            binding.videoProgressBar.visibility = View.GONE
            binding.oneTapActionButton.visibility = View.VISIBLE
            binding.textViewOneTapActive.text = "One Tap Active"
            Toast.makeText(requireActivity(), "Video uploading has failed!", Toast.LENGTH_SHORT).show()
        }.addOnProgressListener { taskSnapshot ->
            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            binding.videoProgressBar.progress = progress.toInt()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}