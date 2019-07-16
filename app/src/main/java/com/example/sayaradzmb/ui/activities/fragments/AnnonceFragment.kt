package com.example.sayaradzmb.ui.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.ui.activities.AjouterAnnonceActivity
import com.example.sayaradzmb.ui.adapter.AnnonceCardAdapter
import com.example.sayaradzmb.repository.servics.AnnonceService
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnnonceFragment : Fragment(),SharedPreferenceInterface {

     /****
     *** This fragment to display the different
     ***/
    private var annoncesList = ArrayList<Annonce>()
    private lateinit var customAdapter: AnnonceCardAdapter
    private lateinit var activityView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activityView = inflater.inflate(
            R.layout.fragement_annonce,
            container,
            false
        )

        // preparer Recycler view
        intialiserRecyclerView()

        val idUser = avoirIdUser(activityView.context).toString()
        //Recuperer les annonces
        recupereAnnonce(idUser)

        //pour passer à une autre vue : ajouter une nouvelle annonce
        val addBtn = activityView.findViewById<FloatingActionButton>(R.id.ajouter_annonce_button)

        addBtn.setOnClickListener {
            // preparé l'activité d'ajout
            val intent = Intent(activityView.context, AjouterAnnonceActivity::class.java)
            // lancer l'activité
            startActivity(intent)
        }

        return activityView
    }

    private fun intialiserRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_annonce)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layout
        customAdapter =
            AnnonceCardAdapter(activityView.context!!, annoncesList)
        rv.adapter = customAdapter

    }

    private fun recupereAnnonce(id: String) {
        val service = ServiceBuilder.buildService(AnnonceService::class.java)
        val requestCall = service.GetAnnouncement(id)

        requestCall.enqueue(object : Callback<List<Annonce>> {

            override fun onFailure(call: Call<List<Annonce>>, t: Throwable) {
                Log.e("get", " Could't get the data ", t)
            }

            override fun onResponse(call: Call<List<Annonce>>, response: Response<List<Annonce>>) {
                if (response.isSuccessful) {
                    annoncesList.addAll(response.body()!!)
                    customAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(view!!.context, response.message(), Toast.LENGTH_LONG).show()
                    Log.w("bind into list", "there is an error")
                }
            }

        })
    }

}