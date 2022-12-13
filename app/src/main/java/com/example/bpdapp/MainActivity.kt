package com.example.bpdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle

class MainActivity : AppCompatActivity() {
    lateinit var actionBarDrawerToogle :ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
    }
    fun setUpViews(){
//        setUpDrawerLayout()
    }
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