package com.example.sayaradzmb.activities.fragments

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionService
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.adapter.OccasionCarsAdapter
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.VehiculeOccasion
import com.example.sayaradzmb.servics.OccasionService
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.ViheculeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v7.widget.AppCompatButton as AppCompatButton


class OccasionFragment : Fragment(),RecycleViewHelper {
    //pour utiliser Linear Helper
    override var itemRecycleView: RecyclerView? = null
    private var annonceList = ArrayList<VehiculeOccasion>()
    private var idUser : String = "44587"
    var maView : View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         maView =inflater.inflate(R.layout.fragment_occasion , container , false)



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


    @Suppress("UNCHECKED_CAST")
    private fun prepareRecyclerView(v : View , id : String){
        val layout = LinearLayoutManager(v.context)
        layout.orientation = LinearLayoutManager.VERTICAL
        val adapter = v.findViewById<RecyclerView>(R.id.recyler_view_voiture_occasion)
        adapter.layoutManager = layout
        val annonceAdapter = OccasionCarsAdapter(v.context,annonceList )
        adapter.adapter = annonceAdapter
        //initLineaire(v , R.id.recyler_view_voiture_occasion, LinearLayoutManager.VERTICAL ,annonceAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun getAnnonceList(id : String){
        val service = prepareService()
        val requestCall = service.GetOccasionAnnouncement(id,null,null,null,null,null,null)

        requestCall.enqueue(object : Callback<List<VehiculeOccasion>> {
            override fun onFailure(call: Call<List<VehiculeOccasion>>, t: Throwable) {
                Log.e("Call response" , "Can't get the data" , t.cause)
            }

            override fun onResponse(call: Call<List<VehiculeOccasion>>, response: Response<List<VehiculeOccasion>>) {
                if(response.isSuccessful){
                    Toast.makeText(this@OccasionFragment.context,response.body().toString(),Toast.LENGTH_LONG).show()
                    for (e in response.body()!!){
                        annonceList.add(e)
                    }
                    prepareRecyclerView(maView!! ,idUser)
                }
                else {
                    Log.i("response assert" , "couldn't get the data correctly")
                }
            }

        })
    }

    private fun prepareService(): OccasionService {
            return ServiceBuilder.buildService(OccasionService::class.java)
    }
}