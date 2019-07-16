package com.example.sayaradzmb.ui.activities.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R
import com.example.sayaradzmb.ui.adapter.MarqueAdapter
import com.example.sayaradzmb.helper.*
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import android.support.v7.widget.SearchView
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.servics.MarqueService


@Suppress("UNCHECKED_CAST")
class NouveauRechercheCars: Fragment(),RecycleViewHelper,SearchViewInterface,SharedPreferenceInterface {
    private var onSearchPressed : OnSearchPressed? = null
    override var itemRecycleView : RecyclerView? = null
    private var marqueList = ArrayList<Marque>()
    private var marqueAdapter : MarqueAdapter? = null
    private var searchView : SearchView? = null

    /**
     * la foncion onCreat
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragement_neuf,container,false)
        Log.i("user info ",avoirInfoUser(this.context!!).toString())
        init(v)
        requeteMarque()
        //essai
        initSearchView(activity!!,v,marqueAdapter!!,R.id.search_bar_marque)
        // Associate searchable configuration with the SearchView
        return v
    }

    /**
     * l'interface qui aide a envoyer des donnee d'un fragment a l'activity
     */
    interface OnSearchPressed{
        fun envoyerFragment(int : Int,version : Version)
    }

    /**
     * onAttach methode overriding
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
        val vService = ServiceBuilder.buildService(MarqueService::class.java)
        val requeteAppel = vService.getMarques()
        requeteAppel.enqueue(object : Callback<List<Marque>> {
            override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    var lesMarque = response.body()!!
                    lesMarque.forEach{
                        e->marqueList.add(e)
                    }
                    //marqueAdapter!!.addAllwithclear(marqueList)
                }else{

                }
            override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
            }
        })
    }
    /**
     * les fonction d'essai
     */

    private fun init(v : View){
        marqueAdapter = MarqueAdapter(marqueList,v.context,v,marqueList,activity!!,onSearchPressed)
        initLineaire(v,R.id.imd_rv_marque,LinearLayoutManager.VERTICAL,marqueAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }



}



