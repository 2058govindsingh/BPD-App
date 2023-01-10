package com.example.bpdapp.models

data class Police (
    val email: String = "",
    val name: String = "",
    val designation: String = "",
    val policeStation: String = "",
    var cases: Int = 0,
    val imageUrl: String = "",
    val mobileNumber :String =""

)