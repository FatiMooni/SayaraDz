package com.example.sayaradzmb.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.*
import com.example.sayaradzmb.R
import com.example.sayaradzmb.repository.servics.AnnonceService
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.servics.ServiceBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.annonce_view.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Cette claase herite recycle viewer adapter , pour binder les
 * informations de chaque élement ( annonce) à une cardview
 **/

class AnnonceCardAdapter(val context : Context, val annonces : ArrayList<Annonce>, val info : ArrayList<Version>) : RecyclerView.Adapter<AnnonceCardAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return annonces.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        // Pour passer ou bien binder les données dans la vue ( card view ) créé
        // p1 pour la vue ascendente ( parent )
        // p0 pour la position

        val annonce = annonces[p1]
        val version = info[p1]
        p0.bindInfo(annonce,version)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        // pour retourner le fichier xml annonce_view sous
        // forme d'une vue pour chaque élément de la liste
        val annonceView = LayoutInflater.from(context).inflate(R.layout.annonce_view, p0,false)
        return ViewHolder(annonceView)
    }



    inner class ViewHolder (private val objet : View) : RecyclerView.ViewHolder(objet){

        //variables
        var véhicule : String = ""
        lateinit var mDialogView : View
        lateinit var pAdapter : VehiculeImageAdapter
        @SuppressLint("NewApi")
        var popup = PopupMenu(objet.context,objet.delete_icon_btn)



        @SuppressLint("SetTextI18n")
         fun bindInfo(annonce : Annonce, version : Version) {
            véhicule = version.modele!!.marque.NomMarque + " " + version.modele!!.NomModele + " " + version.NomVersion
            objet.annonce_info.text = véhicule
            objet.annonce_price_info.text = annonce.Prix
            objet.offer_num.text = annonce.NombreOffres.toString()
            if (!annonce.images!!.isEmpty()) {
                Picasso.get().load(annonce.images!![0].CheminImage).into(objet.annonce_image)
            }

             objet.button_apercu.setOnClickListener {
                //Inflate the dialog --> ajouter le contenu en xml au dialog
                //val mDialogView = LayoutInflater.from(objet.context).inflate(R.layout.apercu_annonce_contenu, null)

                //AlertDialogBuilder
                val mBuilder = AlertDialog.Builder(objet.context)
                    .setView(mDialogView)
                    .setTitle("Aperçu de l'annonce")

                //init the adapter
                pAdapter = VehiculeImageAdapter(mBuilder.context, annonce.images!!)

                //Setting the data
                mDialogView.findViewById<TextView>(R.id.marque_title).text = véhicule
                mDialogView.findViewById<TextView>(R.id.prix_specification).text = annonce.Prix
                mDialogView.findViewById<TextView>(R.id.KM_title).text = annonce.Km
                mDialogView.findViewById<TextView>(R.id.color_title).text = annonce.Couleur
                mDialogView.findViewById<TextView>(R.id.year_title).text = "jan 2018"
                mDialogView.findViewById<TextView>(R.id.type_title).text = annonce.Carburant
                mDialogView.findViewById<TextView>(R.id.text_description).text = annonce.Description
                mDialogView.findViewById<ViewPager>(R.id.images_viewer).adapter = pAdapter


                //afficher le dialog
                mBuilder.show()
            }

            val inflater = popup.menuInflater
            inflater.inflate(R.menu.card_menu,popup.menu)

            objet.delete_icon_btn.setOnClickListener { popup.show() }

            /* objet.delete_icon_btn.setOnClickListener {
                 //Inflate the dialog --> ajouter le contenu en xml au dialog
                 val mDialogConfirm = LayoutInflater.from(objet.context).inflate(R.layout.confirm_dialog, null)

                 //AlertDialogBuilder
                 val mBuilder = AlertDialog.Builder(objet.context)
                     .setView(mDialogConfirm)
                     .setTitle("Confirmation")

                 mDialogConfirm.findViewById<Button>(R.id.dialogConfirmBtn).setOnClickListener {
                     supprimerAnnonce(annonce.idAnnonce!!)
                 }
                 mBuilder.show()
             }*/


        }

        fun supprimerAnnonce(id :Int){
            val service = ServiceBuilder.buildService(AnnonceService::class.java)
            val deleteReq = service.DeleteAnnouncement(id)

            deleteReq.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("delete annonce","Something went wrong", t)
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful){
                        //annonces.remove()
                        //this@AnnonceCardAdapter.notifyItemRemoved()
                        Toast.makeText(objet.context,response.message(),Toast.LENGTH_LONG).show()
                    }
                    else {
                        Log.w("delete annonce","the req passed nut Something went wrong")

                    }
                }

            })

        }
    }


}

