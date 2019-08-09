package com.example.sayaradzmb.model

data class Paiment (
    var paiment : String
)

data class PaimentToken(
    var clientToken : String,
    var success : Boolean
)