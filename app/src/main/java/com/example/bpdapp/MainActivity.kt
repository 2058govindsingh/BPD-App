package com.example.bpdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.bpdapp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.cancel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    lateinit var actionBarDrawerToggle :ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hamburger.setOnMenuItemClickListener {
            if (it.itemId == R.id.logOutButton) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.log_out))
                    .setMessage(resources.getString(R.string.log_out_confirmation))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.log_out)) { dialog, which ->
                        // Respond to positive button press
                        Firebase.auth.signOut()
                        val loginActivityIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginActivityIntent)
                        finish()
                    }
                    .show()
                true
            } else
                false
        }
    }

//    fun setUpViews(){
//        setUpDrawerLayout()
//    }
//    fun setUpDrawerLayout(){
//        setSupportActionBar(findViewById(R.id.hamburger))
//        actionBarDrawerToogle=
//            ActionBarDrawerToggle(this,findViewById(R.id.mainDrawer),R.string.app_name,R.string.app_name)
//        actionBarDrawerToogle.syncState()
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(actionBarDrawerToogle.onOptionsItemSelected(item)){
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}