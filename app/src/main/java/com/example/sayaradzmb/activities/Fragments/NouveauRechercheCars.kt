package com.example.sayaradzmb.activities.Fragments

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.sayaradzmb.R
import com.example.sayaradzmb.adapter.MarqueAdapter
import com.example.sayaradzmb.helper.*
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.Modele
import com.example.sayaradzmb.model.version
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.ViheculeService
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import android.support.v4.view.MenuItemCompat.getActionView
import android.support.v4.content.ContextCompat.getSystemService
import android.app.SearchManager
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager


class NouveauRechercheCars: Fragment(),RecycleViewHelper {
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
        val pref = SharedPreferencesHelper(this.context!!,"facebook")
        //println("islem1 "+pref.sharedPreferences.getString("idUser",null))
        init(v)
        requeteMarque()

        //essai
        // Associate searchable configuration with the SearchView
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager?
        searchView = v.findViewById<SearchView>(R.id.search_bar_marque)
        searchView!!.setSearchableInfo(
            searchManager!!
                .getSearchableInfo(activity!!.getComponentName())
        )
        searchView!!.setMaxWidth(Integer.MAX_VALUE)

        // listening to search query text change
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                marqueAdapter!!.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                marqueAdapter!!.getFilter().filter(query)
                return false
            }
        })
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
        val vService = ServiceBuilder.buildService(ViheculeService::class.java)
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
        marqueAdapter = MarqueAdapter(marqueList,v.context,v,onSearchPressed,marqueList)
        initLineaire(v,R.id.imd_rv_marque,LinearLayoutManager.VERTICAL,marqueAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }



}



