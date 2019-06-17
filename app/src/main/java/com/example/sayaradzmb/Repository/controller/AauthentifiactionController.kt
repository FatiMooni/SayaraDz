package com.example.sayaradzmb.Repository.controller

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.sayaradzmb.LETTRE_FACEBOOK_AUTH
import com.example.sayaradzmb.LETTRE_GOOGLE_AUTH
import com.example.sayaradzmb.NOM_INIT_AUTH
import com.example.sayaradzmb.model.Automobiliste
import com.example.sayaradzmb.servics.AuthService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.facebook.AccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AauthentifiactionController {

    var authService : AuthService? = null
    init {
        authService = ServiceBuilder.buildService(AuthService::class.java)
    }

    fun googleConnexion(token : String?,automobiliste: Automobiliste,context: Context) : Boolean{
        var connected = false
        val requestCall = authService!!.setToken("$NOM_INIT_AUTH $LETTRE_GOOGLE_AUTH ${token}",automobiliste)
        requestCall.enqueue(object : Callback<Automobiliste> {
            override fun onResponse(call: Call<Automobiliste>, response: Response<Automobiliste>) {
                if(response.isSuccessful){
                    //dejaConnecte()
                    connected = true
                }else{
                    Toast.makeText(context,"Failed to connect", Toast.LENGTH_LONG)
                }

            }
            override fun onFailure(call: Call<Automobiliste>, t: Throwable) {
                Toast.makeText(context,"Failed", Toast.LENGTH_LONG)
            }
        })
        return connected
    }


    fun facebookConnexion(token: AccessToken,automobiliste: Automobiliste,context: Context):Boolean{
        var connected = false
        val requestCall = authService!!.setToken("$NOM_INIT_AUTH $LETTRE_FACEBOOK_AUTH ${token.token}",automobiliste)
        requestCall.enqueue(object : Callback<Automobiliste> {
            override fun onResponse(call: Call<Automobiliste>, response: Response<Automobiliste>) {
                if(response.isSuccessful){

                    connected=true
                }else{
                    Toast.makeText(context,"Failed to connect",Toast.LENGTH_LONG)
                    Log.w("response !success","la connexion echouee"+response)
                }

            }
            override fun onFailure(call: Call<Automobiliste>, t: Throwable) {
                Toast.makeText(context,"Failed",Toast.LENGTH_LONG)
                Log.w("facebook failure",t.message)
            }
        })
        return connected
    }
}