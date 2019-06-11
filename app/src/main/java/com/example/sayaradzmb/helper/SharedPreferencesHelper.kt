package com.example.sayaradzmb.helper

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.example.sayaradzmb.model.Automobiliste
import java.math.BigInteger


class SharedPreferencesHelper(internal var context: Context,nom_fichier : String) {
    internal var sharedPreferences: SharedPreferences = context.getSharedPreferences(nom_fichier, Context.MODE_PRIVATE)

    /**
     * To set login details
     * @param userName : username to set
     * @param password : password to set
     */
    fun setLoginDetails(idUser : String,userNom: String, userPrenom: String) {
        val editor = sharedPreferences.edit()
        editor.putString("idUser",idUser)
        editor.putString("userNom", userNom)
        editor.putString("userPrenom", userPrenom)
        editor.apply()
    }

    fun getAutomobilste() : Automobiliste{
        val pref = sharedPreferences
        val id = pref.getString("idUser",null)
        val nom = pref.getString("userName",null)
        val prenom = pref.getString("userPrenom",null)
        return Automobiliste(id,nom,prenom)
    }

    fun avoirIdUser() : BigInteger{
        val pref = sharedPreferences
        val id = pref.getString("idUser",null)
        return id!!.toBigInteger()
    }

    /**
     * To check and get login details
     * @param userName : name to validate
     * @param password : password to validate
     * @return true : if valid user
     * false : if valid password
     */
    fun isValidUser(userName: String, password: String): Boolean {
        // to get username
        Log.d(TAG, "username = " + sharedPreferences.getString("userName", null)!!)
        Log.d(TAG, "password = " + sharedPreferences.getString("password", null)!!)

        return this.sharedPreferences.getString("userName", null) == userName && sharedPreferences.getString("password", null) == password
    }


    companion object {
        private val TAG = "SharedPreferencesHelper"
    }

}