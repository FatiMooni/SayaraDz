package com.example.sayaradzmb.ui.activities.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.sayaradzmb.R
import com.example.sayaradzmb.Repository.servics.CommandeService
import com.example.sayaradzmb.Repository.servics.StockService
import com.example.sayaradzmb.constatnte.CHANELLE_ID
import com.example.sayaradzmb.constatnte.NOTIFICATION_ID
import com.example.sayaradzmb.helper.NotificationHelper
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.model.VoitureCommande
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NouveauCommandeFragment : Fragment(),NotificationHelper{

    /**
     *  declaration component
     */
    private var confirmer : Button? = null
    private var voiture : VoitureCommande? = null
    private var versement : EditText?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_neuf_commande,container,false)
        val context = v.context
        /**
         * Avoir la version choisie
         */
        voiture = arguments!!.getParcelable("voitureCommande") as VoitureCommande
        Log.i("voiture : ",voiture.toString())
        /**
         * initialisation
         */
        confirmer = v.findViewById(R.id.commande_conf_button)!!
        versement = v.findViewById(R.id.edit_text_versement)

        /**
         * Confirmer la commande
         */
        confirmer!!.setOnClickListener {
            Log.i("versement",versement!!.text.toString())
            if (versement!!.text.toString() == ""){
                val vService = ServiceBuilder.buildService(CommandeService::class.java)
                val requeteAppel = vService.creeCommande("199542354","110623972598370355824","33003","1")
                requeteAppel.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) =
                        if(response.isSuccessful){
                            print(response.body()!!)
                        }else{

                        }
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.w("failConnexion","la liste marue non reconnue")
                    }
                })
            }else{
                Log.i("versement",versement!!.text.toString())
            }

        }
        return v
    }
}