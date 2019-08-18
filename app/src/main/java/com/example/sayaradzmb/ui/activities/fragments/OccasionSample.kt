package com.example.sayaradzmb.ui.activities.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.model.VehiculeRechFilters
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
import com.example.sayaradzmb.viewmodel.OccasionViewModel


class OccasionSample : Fragment(),SharedPreferenceInterface{
    private var annonceList = ArrayList<Comparable<*>>()
    private var annonceAdapter : CustomCardsAdapter? = null
    val filter = VehiculeRechFilters(null, null, null, null, null, null, null)
    var maView: View? = null
    var idUser: String? = null
    lateinit var  model : OccasionViewModel

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    maView = inflater.inflate(R.layout.fragment_occasion, container, false)

        prepareRecyclerView(maView!!)
        // Load our BooksViewModel or create a new one
        model = ViewModelProviders.of(this).get(OccasionViewModel::class.java)
        // Listen for changes on the BooksViewModel
        model.getUsedCars().observe(this, Observer < List < VehiculeOccasion> >  {

                 annonceList.addAll(it!!)
                annonceAdapter!!.notifyDataSetChanged()

        })

    idUser = avoirIdUser(maView!!.context).toString()
    model.loadUsedCars(idUser!!,filters = filter)

    //passer au fragment suivant : i.e. fragment de recherche avancée
    maView!!.findViewById<AppCompatButton>(R.id.btn_rech).setOnClickListener {
        val nextFrag = OccasionRechFragment()
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_id, nextFrag, "findThisFragment")
            .addToBackStack(null)
            .commit()
    }
    return  maView
  }

    fun prepareRecyclerView(v : View){
        val layout = LinearLayoutManager(v.context)
        layout.orientation = LinearLayoutManager.VERTICAL
        val adapter = v.findViewById<RecyclerView>(R.id.recyler_view_voiture_occasion)
        adapter.layoutManager = layout
        annonceAdapter = CustomCardsAdapter(v.context,annonceList )
        adapter.adapter = annonceAdapter
    }

}