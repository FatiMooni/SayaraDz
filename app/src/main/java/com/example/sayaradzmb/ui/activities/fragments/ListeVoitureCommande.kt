package com.example.sayaradzmb.ui.activities.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.Couleur
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.model.VoitureCommande
import com.example.sayaradzmb.ui.adapter.ListeVoitureCommandeAdapter

class ListeVoitureCommande : Fragment(),RecycleViewHelper {
    private var nomVersion : TextView? = null
    private var listVoitureAdapter : ListeVoitureCommandeAdapter? = null
    private var listVoiture = ArrayList<VoitureCommande>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_liste_voiture_commande, container, false)
        val version = arguments!!.getParcelable("command") as Version
        nomVersion = v.findViewById(R.id.nom_version_voiture)
        nomVersion!!.text = version.NomVersion
        listVoitureAdapter = ListeVoitureCommandeAdapter(listVoiture,context!!)
        initLineaire(v,R.id.liste_voiture_commande,LinearLayoutManager.VERTICAL,listVoitureAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)


        listVoiture.add(VoitureCommande(Couleur(123,"red","12368",null), arrayListOf(),4,123654))
        listVoiture.add(VoitureCommande(Couleur(123,"red","12368",null), arrayListOf(),4,123654))
        listVoiture.add(VoitureCommande(Couleur(123,"red","12368",null), arrayListOf(),4,123654))
        listVoiture.add(VoitureCommande(Couleur(123,"red","12368",null), arrayListOf(),4,123654))
        listVoitureAdapter!!.notifyDataSetChanged()


        return v
    }


}