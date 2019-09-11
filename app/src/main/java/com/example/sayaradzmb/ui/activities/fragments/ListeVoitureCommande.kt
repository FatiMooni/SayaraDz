package com.example.sayaradzmb.ui.activities.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.Repository.servics.StockService
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.model.VoitureCommande
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.ui.adapter.ListeVoitureCommandeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListeVoitureCommande : Fragment(),RecycleViewHelper {

    /**
     * Les attribut
     */
    private var nomVersion : TextView? = null
    private var listVoitureAdapter : ListeVoitureCommandeAdapter? = null
    private var listVoiture = ArrayList<VoitureCommande>()
    private var version : Version? = null
    private var commande : Button? = null

    /**
     * On create
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_liste_voiture_commande, container, false)
        listVoiture.clear()
        /**
         *
         */
        /**
         * Avoir la version
         */
        version = arguments!!.getParcelable("command") as Version

        /**
         * Avoir les composant
         */
        nomVersion = v.findViewById(R.id.nom_version_voiture)
        commande = v.findViewById(R.id.neuf_tech_card_commande_button)


        /**
         * la requete d'avoir les voiture
         */
        avoirListVoiture(version!!.CodeVersion,v)
        /**
         * Initialiser les composant
         */

        /**
         * Initialiser les recycle view
         */
        Log.i("on create : ","in on create")
        return v
    }

    /**
     * la requte d'avoir les differnet vehiule
     */
    fun avoirListVoiture(id : Int?,v : View){
        var progress = ProgressDialog(context,android.R.style.Theme_DeviceDefault_Dialog)
        progress.setCancelable(false)
        progress.setTitle("charger la liste des Voitures")
        progress.show()
        val vService = ServiceBuilder.buildService(StockService::class.java)
        val requeteAppel = vService.avoirVoitureStock(version!!.CodeVersion!!)
        requeteAppel.enqueue(object : Callback<List<VoitureCommande>> {
            override fun onResponse(call: Call<List<VoitureCommande>>, response: Response<List<VoitureCommande>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    var listStock = response.body()!!
                    listStock.forEach {
                        e->
                        Log.i("version",version.toString())
                        e.codeMarque = version!!.CodeMarque
                        e.nomVersion = version!!.NomVersion
                        e.Image = version!!.couleurs?.get(0)!!.CheminImage
                        listVoiture.add(e)
                    }
                    //errore
                    Log.i("list Voiture", listVoiture!!.size.toString())
                    if (listVoiture.size == 0)  nomVersion!!.text = "Pas de voiture ${version!!.NomVersion} Disponible"
                    else nomVersion!!.text = "Voiture Disponible : ${version!!.NomVersion}"
                    InitialiserListeVoiture(v)
                    progress.dismiss()
                }else{

                }
            override fun onFailure(call: Call<List<VoitureCommande>>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
                progress.dismiss()
                avoirListVoiture(id,v)
            }
        })
    }

    /**
     * Initialiser la liste des vihecule
     */
    private fun InitialiserListeVoiture(v : View){
        listVoitureAdapter = ListeVoitureCommandeAdapter(listVoiture,context!!)
        initLineaire(v,R.id.liste_voiture_commande,LinearLayoutManager.VERTICAL,listVoitureAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }
}