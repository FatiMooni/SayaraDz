package com.example.sayaradzmb.activities.Fragments

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
import com.example.sayaradzmb.adapter.MarqueAdapter
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.helper.SharedPreferencesHelper
import com.example.sayaradzmb.model.Marque
import com.example.sayaradzmb.model.version
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.ViheculeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OccasionFragment : Fragment(),RecycleViewHelper {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var myView = inflater.inflate(R.layout.fragement_occasion,null)

        //prepare the recycle view + marque list
        init(myView)
        getMarque()

        //retourner le fragment
        return myView
    }

    override var itemRecycleView : RecyclerView? = null
    private var marqueList = ArrayList<Marque>()
    private var marqueAdapter : MarqueAdapter? = null

    /**
     * l'interface qui aide a envoyer des donnee d'un fragment a l'activity
     */
    interface OnSearchPressed{
        fun envoyerFragment(int : Int,version : version)
    }

    /**
     * onAttach methode overriding
     */
   /* override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = context as Activity
        onSearchPressed = activity as OnSearchPressed
    }*/



    /**
     * la fonctin qui va faire l'appel a lapi pour avoir les marque
     */
    private fun getMarque(){
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

    private fun init(maView : View){
        marqueAdapter = MarqueAdapter(marqueList,maView.context,maView)
        initLineaire(maView,R.id.imd_rv_marque,
            LinearLayoutManager.VERTICAL,marqueAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }



}