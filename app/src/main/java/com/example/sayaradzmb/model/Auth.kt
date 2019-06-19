package com.example.sayaradzmb.model

data class AuthGoogle (
    var token :String? = null,
    var signe:String = "G",
    var idAutomobiliste : String? =null
)
data class Authfacebook(
    var token : String? = null,
    var signe : String = "F",
    var idAutomobiliste : String? = null
)