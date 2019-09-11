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
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.helper.SharedPreferencesHelper
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.model.VoitureCommande
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.VersionService
import com.example.sayaradzmb.ui.adapter.ListeVoitureCommandeAdapter
import com.example.sayaradzmb.ui.adapter.ListeVoitureFavorisAdapter
import lib.android.paypal.com.magnessdk.network.c.v
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavorisFragment : Fragment(),SharedPreferenceInterface,RecycleViewHelper{

    private var listFavorisAdapter : ListeVoitureFavorisAdapter? = null
    private var listFavoris = ArrayList<Version>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragement_favoris,null)

        listFavoris.clear()

        avoirListeFavoris(v)

        return v
    }

    private fun avoirListeFavoris(v : View) {
        var progress = ProgressDialog(context,android.R.style.Theme_DeviceDefault_Dialog)
        progress.setCancelable(false)
        progress.setTitle("charger la liste des Voitures")
        progress.show()
        val vService = ServiceBuilder.buildService(VersionService::class.java)
        val request = vService.getFavoris(avoirIdUser(this.context!!),0)
        request.enqueue(object : Callback<List<Version>> {
            override fun onResponse(call: Call<List<Version>>, response: Response<List<Version>>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    var listStock = response.body()!!
                    listStock.forEach {

                            e->
                        listFavoris.add(e)
                    }
                    //errore
                    //if (listFavoris.size == 0)  nomVersion!!.text = "Pas de voiture ${version!!.NomVersion} Disponible"
                    //else nomVersion!!.text = "Voiture Disponible : ${version!!.NomVersion}"
                    InitialiserListeVoiture(v)
                    progress.dismiss()
                }else{

                }
            override fun onFailure(call: Call<List<Version>>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
                progress.dismiss()
                avoirListeFavoris(v)
            }
        })

    }

    private fun InitialiserListeVoiture(v: View) {
        listFavorisAdapter = ListeVoitureFavorisAdapter(listFavoris,context!!)
        initLineaire(v,R.id.recyler_view_favoris,
            LinearLayoutManager.VERTICAL,listFavorisAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }
}