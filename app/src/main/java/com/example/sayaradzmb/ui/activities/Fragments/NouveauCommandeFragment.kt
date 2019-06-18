package com.example.sayaradzmb.ui.activities.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.NotificationHelper


class NouveauCommandeFragment : Fragment(),NotificationHelper{

    /**
     *  declaration component
     */
    private var confirmer : Button? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragement_neuf_commande,container,false)
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