package com.example.sayaradzmb.helper

import android.content.Context
import com.example.sayaradzmb.constatnte.NOM_FICHER_LOGIN
import com.example.sayaradzmb.model.Automobiliste

interface SharedPreferenceInterface {

    /**
     * retourne une instance de ShaeredPrefernceHelper en entrant le nom de Fichier
     */
    public fun sharedPref(context: Context, nomFichier : String) : SharedPreferencesHelper{
        return SharedPreferencesHelper(context,nomFichier)
    }
    public fun avoirInfoUser(context : Context) : Automobiliste {
        val pref = sharedPref(context, NOM_FICHER_LOGIN)
        return pref.getAutomobilste()
    }
}