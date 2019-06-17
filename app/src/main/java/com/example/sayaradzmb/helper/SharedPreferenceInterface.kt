package com.example.sayaradzmb.helper

import android.content.Context
import com.example.sayaradzmb.NOM_FICHER_LOGIN
import com.example.sayaradzmb.model.Automobiliste
import java.math.BigInteger

interface SharedPreferenceInterface {

    /**
     * retourne une instance de ShaeredPrefernceHelper en entrant le nom de Fichier
     */
    public fun sharedPref(context: Context, nomFichier : String) : SharedPreferencesHelper{
        return SharedPreferencesHelper(context,nomFichier)
    }

    /**
     * avoir le sinfo sous forme de Automobolisite
     */
    public fun avoirInfoUser(context : Context) : Automobiliste {
        val pref = sharedPref(context, NOM_FICHER_LOGIN)
        return pref.getAutomobilste()
    }


    public fun avoirIdUser(context: Context) : BigInteger{
        val pref = sharedPref(context, NOM_FICHER_LOGIN)
        return pref.avoirIdUser()
    }
}