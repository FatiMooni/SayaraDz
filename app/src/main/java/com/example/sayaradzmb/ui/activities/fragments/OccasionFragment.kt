package com.example.sayaradzmb.ui.activities.fragments

import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.VehiculeRechFilters



class OccasionFragment : CustomOccasionFragment(), SharedPreferenceInterface {
    private val filter = VehiculeRechFilters(null, null, null, null, null, null, null)
    var maView: View? = null
    var idUser: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        maView = inflater.inflate(R.layout.fragment_occasion, container, false)

        idUser = avoirIdUser(maView!!.context).toString()
        prepareRecyclerView(maView!!)
        getAnnonceList(idUser!!, filter)

        //passer au fragment suivant : i.e. fragment de recherche avancée
        maView!!.findViewById<AppCompatButton>(R.id.btn_rech).setOnClickListener {
            val nextFrag = OccasionRechFragment()
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_id, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }

        return maView

    }


}