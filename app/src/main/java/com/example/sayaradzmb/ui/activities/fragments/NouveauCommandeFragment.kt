package com.example.sayaradzmb.ui.activities.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.Repository.servics.CommandeService
import com.example.sayaradzmb.Repository.servics.PaimentService
import com.example.sayaradzmb.Repository.servics.StockService
import com.example.sayaradzmb.constatnte.CHANELLE_ID
import com.example.sayaradzmb.constatnte.NOTIFICATION_ID
import com.example.sayaradzmb.helper.NotificationHelper
import com.example.sayaradzmb.servics.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent.getIntent

import com.example.sayaradzmb.constatnte.REQUEST_CODE
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInResult
import android.content.Intent

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.widget.*
import com.braintreepayments.api.dropin.DropInRequest
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.FragmentHelper
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.confirm_dialog.*
import kotlinx.android.synthetic.main.nav_header_side_menu.*
import org.w3c.dom.Text


class NouveauCommandeFragment : Fragment(),NotificationHelper, SharedPreferenceInterface {

    /**
     *  declaration component
     */
    private var confirmer : Button? = null
    private var voiture : VoitureCommande? = null
    private var versement : EditText?=null
    val vService = ServiceBuilder.buildService(CommandeService::class.java)
    val pService = ServiceBuilder.buildService(PaimentService::class.java)
    private var commandeCurrente : Commande? = null
    private var nomCommande : TextView? = null
    private var prixCommande : TextView?= null
    private var profil : TextView?=null
    private var image : ImageView?=null

    /**
     * on Create
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_neuf_commande,container,false)

        val context = v.context
        val user = avoirInfoUser(context)
        /**
         * Avoir la version choisie
         */
        voiture = arguments!!.getParcelable("voitureCommande") as VoitureCommande
        Log.i("voiture : ",voiture.toString())

        /**
         * initialisation
         */
        confirmer = v.findViewById(R.id.commande_conf_button)!!
        versement = v.findViewById(R.id.edit_text_versement)
        nomCommande = v.findViewById(R.id.commande_nom_voiture)
        prixCommande = v.findViewById(R.id.commande_prix)
        profil = v.findViewById(R.id.commande_nom_profil)
        image = v.findViewById(R.id.commande_prix_car)
        /**
         * Confirmer la commande
         */
        nomCommande?.text = voiture?.nomVersion
        prixCommande?.text = voiture?.prixTotal.toString()+" DZA"
        profil?.text = user.Nom + " "+ user.Prenom
        if(voiture?.Image != null)Picasso.get().load(voiture!!.Image).into(image)

        confirmer!!.setOnClickListener {
            var builder : AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage("Effecter cette commande ?")
            builder.setPositiveButton("OUI"){dialog, which ->
                commandeSansReservation(voiture!!.Montant.toString(),avoirIdUser(this.context!!).toString(),voiture!!.vehicules[0].NumChassis.toString(),voiture!!.codeMarque.toString())

            }
            builder.setNegativeButton("NON"){dialog,which ->
                var toast = Toast.makeText(context,"commande Annulee",Toast.LENGTH_SHORT)
                toast.show()
            }
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }
        return v
    }


    /**
     * commande sans rservation
     */
    private fun commandeSansReservation(montant : String,idAutomobiliste: String,chassis : String,fabricant : String){
        var progress = ProgressDialog(context,android.R.style.Theme_DeviceDefault_Dialog)
        progress.setCancelable(false)
        progress.setTitle("effectuer la commande")
        progress.show()
        val requeteAppel = vService.creeCommande(montant,idAutomobiliste,chassis,fabricant)
        requeteAppel.enqueue(object : Callback<Commande> {
            override fun onResponse(call: Call<Commande>, response: Response<Commande>) =
                if(response.isSuccessful){
                    print(response.body()!!)
                    commandeCurrente = response.body()
                    progress.dismiss()
                    creeResrvation()

                }else{
                    var toast : Toast = Toast.makeText(context,"La commande n'a pas été effecuer",Toast.LENGTH_LONG)
                    toast.show()
                    progress.dismiss()
                }
            override fun onFailure(call: Call<Commande>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
                progress.dismiss()
                commandeSansReservation(montant,idAutomobiliste,chassis,fabricant)

            }
        })
    }

    /**
     * get token client
     */
    fun getClient() : String{
        var token :String = ""
        val requestAppel = pService.getBraintreeToken()
        requestAppel.enqueue(object : Callback<PaimentToken> {
            override fun onResponse(call: Call<PaimentToken>, response: Response<PaimentToken>) =
                if(response.isSuccessful){
                    token = response.body()!!.clientToken
                    Log.i("token",token)
                    onBraintreeSubmit(token)
                }else{
                    getClient()
                    var toast : Toast = Toast.makeText(context,"Reservation échouee",Toast.LENGTH_LONG)
                    toast.show()

                }
            override fun onFailure(call: Call<PaimentToken>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
                getClient()
            }
        })
        return token
    }
    /**
     * cree resrvation a une commande
     */
    fun creeResrvation(){
        if (versement!!.text.toString() != ""){
            Log.i("versement",versement!!.text.toString())
            val token = getClient()
        }else{
            var toast : Toast = Toast.makeText(context,"La commande est a été effecuer sans Reservation",Toast.LENGTH_LONG)
            toast.show()
            FragmentHelper.changeFragmentWithoutData(activity!!,NouveauRechercheCars(),"toRechercheCars",R.id.fragment_id)
        }
    }

    /**
     *
     */
    fun onBraintreeSubmit(token :String) {
        val dropInRequest = DropInRequest()
            .clientToken(token)
        startActivityForResult(dropInRequest.getIntent(context), REQUEST_CODE)
    }

    /**
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result = data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                // use the result to update your UI and send the payment method nonce to your server
                val nonce = result.paymentMethodNonce
                Log.i("nonce",nonce!!.nonce)
                paiment(nonce.nonce,versement!!.text.toString(),commandeCurrente!!.idCommande.toString())

            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
                var toast : Toast = Toast.makeText(context," paiment non effectuee",Toast.LENGTH_LONG)
                toast.show()
                FragmentHelper.changeFragmentWithoutData(activity!!,NouveauRechercheCars(),"toRechercheCars",R.id.fragment_id)

            } else {
                // handle errors here, an exception may be available in
                val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
            }
        }
    }

    /**
     * paiment
     */
    private fun paiment(paiment : String,montant : String,commande : String){
        var progress = ProgressDialog(context,android.R.style.Theme_DeviceDefault_Dialog)
        progress.setCancelable(false)
        progress.setTitle("attender pour le paiment")
        progress.show()
        val requestAppel = pService.postNonceToServer(paiment,montant,commande)
        requestAppel.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) =
                if(response.isSuccessful){
                    progress.dismiss()
                    var toast : Toast = Toast.makeText(context,"La commande est a été effecuer avec Reservation ${montant} DA",Toast.LENGTH_LONG)
                    toast.show()
                    println(response.body()!!)
                    FragmentHelper.changeFragmentWithoutData(activity!!,NouveauRechercheCars(),"toRechercheCars",R.id.fragment_id)
                  }else{
                    paiment(paiment,montant, commande)
                    var toast : Toast = Toast.makeText(context,"paiment non effectue",Toast.LENGTH_LONG)
                    toast.show()
                }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
                progress.dismiss()
                paiment(paiment,montant,commande)
            }
        })
    }
}