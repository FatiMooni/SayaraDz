package com.example.sayaradzmb.ui.activities.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.sayaradzmb.R
import com.example.sayaradzmb.constatnte.CHANELLE_ID
import com.example.sayaradzmb.constatnte.NOTIFICATION_ID
import com.example.sayaradzmb.helper.NotificationHelper


class NouveauCommandeFragment : Fragment(),NotificationHelper{

    /**
     *  declaration component
     */
    private var confirmer : Button? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_neuf_commande,container,false)
        val context = v.context

        /**
         * initialisation
         */
        confirmer = v.findViewById(R.id.commande_conf_button)!!
        confirmer!!.setOnClickListener {
            afficherNotification(this.context!!)
            println("buttin confirmed pressed")

        }
        return v
    }
}