package com.example.sayaradzmb.ui.activities.Fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R
import com.example.sayaradzmb.Repository.controller.MarqueController
import com.example.sayaradzmb.ui.adapter.MarqueAdapter
import com.example.sayaradzmb.helper.*
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.version


class NouveauRechercheCars: Fragment(),RecycleViewHelper,SearchViewInterface,SharedPreferenceInterface {
    private var onSearchPressed : OnSearchPressed? = null
    override var itemRecycleView : RecyclerView? = null
    private var marqueList = ArrayList<Marque>()
    private var marqueAdapter : MarqueAdapter? = null

    /**
     * la foncion onCreat
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragement_neuf,container,false)
        init(v)
        requeteMarque()
        initSearchView(activity!!,v,marqueAdapter!!,R.id.search_bar_marque)
        return v
    }
    /**
     * l'interface qui aide a envoyer des donnee d'un fragment a l'activity
     */
    interface OnSearchPressed{
        public fun envoyerFragment(int : Int,version : version)
    }
    /**
     * onAttach methode overrdin
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as Activity
        onSearchPressed = activity as OnSearchPressed
    }
    /**
     * la fonctin qui va faire l'appel a lapi pour avoir les marque
     */
    private fun requeteMarque(){
        marqueList.addAll(MarqueController.getAllMarque())
    }
    /**
     * les fonction d'essai
     */
    private fun init(v : View){
        marqueAdapter = MarqueAdapter(marqueList,v.context,v,onSearchPressed,marqueList,activity!!)
        initLineaire(v,R.id.imd_rv_marque,LinearLayoutManager.VERTICAL,marqueAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }



}



