package com.example.bpdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
<<<<<<< HEAD
=======
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
>>>>>>> test1
import com.example.bpdapp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable.cancel

class MainActivity : AppCompatActivity() {
<<<<<<< HEAD

    private lateinit var binding: ActivityMainBinding

=======
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
>>>>>>> test1
//    lateinit var actionBarDrawerToggle :ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
<<<<<<< HEAD

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
        binding.searchButton.setOnClickListener{
            val searchActivityIntent=Intent(this,SearchActivity::class.java)
            startActivity(searchActivityIntent)
        }
        binding.uploadButton.setOnClickListener{
            val uploadActivityIntent=Intent(this,UploadActivity::class.java)
            startActivity(uploadActivityIntent)
        }
        binding.oneTapActionButton.setOnClickListener{
            val oneTapActivityIntent=Intent(this,OneTapActivity::class.java)
            startActivity(oneTapActivityIntent)
        }
=======
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
>>>>>>> test1
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