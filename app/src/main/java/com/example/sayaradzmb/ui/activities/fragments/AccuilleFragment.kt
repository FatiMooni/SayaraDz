package com.example.sayaradzmb.ui.activities.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.helper.SharedPreferencesHelper
import com.example.sayaradzmb.model.UserOffre
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.ui.adapter.AcceuilleCardAdapter
import com.example.sayaradzmb.viewmodel.AcceuilleViewModel
import com.example.sayaradzmb.viewmodel.UserOffersViewModel
import kotlinx.android.synthetic.main.fragement_accuille.*
import kotlinx.android.synthetic.main.fragement_accuille.view.*

class AccuilleFragment : Fragment(),SharedPreferenceInterface{
    private var carsList  = ArrayList<Comparable<*>>()
    private var usedList  = ArrayList<Comparable<*>>()
    private var followedList  = ArrayList<Comparable<*>>()
    private lateinit var activityView: View
    private lateinit var customNewAdapter : AcceuilleCardAdapter
    private lateinit var customUsedAdapter : AcceuilleCardAdapter
    private lateinit var customFollowedAdapter : AcceuilleCardAdapter

    private lateinit var model : AcceuilleViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        //retourner la vue pour ce fragment
        activityView = inflater.inflate(R.layout.fragement_accuille,container,false)

        val idUser = avoirIdUser(this.context!!).toString()


        //Initialize the modelView
        model =  ViewModelProviders.of(this).get(AcceuilleViewModel::class.java)
        // Listen for changes on the BooksViewModel
        model.getUsedCars().observe(this, Observer<List<VehiculeOccasion>> {

            customUsedAdapter.swapData(it!!)
            // Toast.makeText(this@UserOffersActivity, it.toString(), Toast.LENGTH_SHORT).show()

        })

        carsList.add(1)
        carsList.add(5)
        carsList.add(5)

        //listeners for the buttons
        activityView.btn_frag_ann.setOnClickListener {
            val nextFrag = AnnonceFragment()
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_id, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }
        activityView.btn_frag_old.setOnClickListener {
            val nextFrag = OccasionFragment()
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_id, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }
        activityView.btn_frag_new.setOnClickListener {
            val nextFrag = NouveauRechercheCars()
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_id, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit()
        }

        // preparer Recycler view
        intialiserNewRecyclerView()
        intialiserUsedRecyclerView()
        customNewAdapter.swapData(carsList)
        customNewAdapter.setOnCardButtonClickListener(object : AcceuilleCardAdapter.OnCardButtonListener{
            override fun OnCardButton(item: Comparable<*>?, itemType: Int) {
                Toast.makeText(context,itemType.toString(),Toast.LENGTH_SHORT).show()
            }

        })
        model.loadUsedCars(idUser)
        if(followedList.isEmpty()){
            activityView.tx_text.visibility = View.GONE
            activityView.tx_title.visibility = View.GONE
            activityView.gerer_btn.visibility = View.GONE
        }
        return  activityView
    }


    private fun intialiserNewRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_new)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layout
        customNewAdapter =
            AcceuilleCardAdapter(activityView.context!!, carsList)
        rv.adapter = customNewAdapter
    }

    private fun intialiserUsedRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_anciens)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layout
        customUsedAdapter =
            AcceuilleCardAdapter(activityView.context!!, usedList)
        rv.adapter = customUsedAdapter
    }

    private fun intialiserFollowedRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_following)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layout
        customFollowedAdapter =
            AcceuilleCardAdapter(activityView.context!!, followedList)
        rv.adapter = customFollowedAdapter
    }



}

