package com.example.sayaradzmb.ui.activities.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sayaradzmb.R
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.sayaradzmb.ui.activities.CompositionActivity
import com.example.sayaradzmb.helper.*
import com.example.sayaradzmb.ui.adapter.CouleurAdapter
import com.example.sayaradzmb.ui.adapter.ImageVoitureAdapter
import com.example.sayaradzmb.ui.adapter.OptionAdapter
import com.example.sayaradzmb.model.Couleur
import com.example.sayaradzmb.model.Option
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.servics.VersionService
import com.squareup.picasso.Picasso
import me.relex.circleindicator.CircleIndicator2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("UNCHECKED_CAST")
@SuppressLint("ValidFragment")
class NouveauAfficheTechnique @SuppressLint("ValidFragment") constructor(
) : Fragment(),RecycleViewHelper,SuiviVoitureHelper,SharedPreferenceInterface{



    private var voitureAdapter: ImageVoitureAdapter? = null
    private var couleurAdapter: CouleurAdapter? = null
    private var optionAdapter: OptionAdapter? = null
    private var indicator : CircleIndicator2? = null
    val vService =  ServiceBuilder.buildService(VersionService::class.java)

    /**
     * composant design
     */
    private var nomVoiture : TextView?=null
    private var commanderButton : Button?=null
    private var description :TextView?=null
    private var suiviImage : ImageView?=null
    private var prixVoiture : TextView?= null
    private var composerButton : Button? = null
    private var imagePhoto = ArrayList<String>()
    private var couleurs = ArrayList<Couleur>()
    private var options = ArrayList<Option>()
    private var version :Version?=null
    @SuppressLint("SetTextI18n")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_neuf_tech,container,false)
        val context = v.context
        /**
         * Avoir la version choisie
         */
        version = arguments!!.getParcelable("affiche") as Version
        Log.i("version : ",version.toString())

        /**
         * Avoir les component from layout
         */
        nomVoiture = v.findViewById(R.id.neuf_tech_card_nom_car)
        commanderButton = v.findViewById(R.id.neuf_tech_card_commande_button)
        composerButton =v.findViewById(R.id.neuf_tech_card_composer_button)
        description = v.findViewById(R.id.neuf_tech_card_description)
        suiviImage = v.findViewById(R.id.neuf_tech_card_suivi)
        prixVoiture = v.findViewById(R.id.neuf_tech_card_prix)

        /**
         * initialisation
         */
        toggleSuivi(version!!.suivie,suiviImage!!,R.drawable.heart,R.drawable.heart_empty)
        nomVoiture!!.text = version!!.NomVersion
        /**
         * gestion de l'abbonnement
         */
        suiviImage!!.setOnClickListener {
            if (suiviImage!!.tag == "nonSuivi"){
                /**
                 * faire l'abonnement
                 */

                val requeteAppel = vService.suivreVersion(version!!.CodeVersion!!,avoirInfoUser(this.context!!))
                requeteAppel.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                        if(response.isSuccessful){
                            println(response.body().toString())
                        }else{
                            println("la liste version non reconnue ${response}")

                        }
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.w("failConnexion","la liste modele non reconnue ${t.message}")
                    }
                })
                Picasso.get().load(R.drawable.heart).into(suiviImage)
                suiviImage!!.tag = "Suivi"
            }else{
                /**
                 * desabonner
                 */
                val requeteAppel = vService.desuivreVersion(version!!.CodeVersion!!,avoirIdUser(this.context!!))
                requeteAppel.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>): Unit =
                        if(response.isSuccessful){
                            println(response.body().toString())
                        }else{
                            println("la liste version non reconnue ${response}")

                        }
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.w("failConnexion","la liste modele non reconnue ${t.message}")
                    }
                })
                Picasso.get().load(R.drawable.heart_empty).into(suiviImage)
                suiviImage!!.tag = "nonSuivi"
            }
        }

        /**
         * gestion du prix
         */
        if(version!!.lignetarif != null) prixVoiture!!.text = "${version!!.lignetarif!!.Prix.toString()} DA"
        else prixVoiture!!.text = "price not defined"

        // var j'ai pas une liste de voiture mais pour chaque couleur j'ai une voiture
        //if (version!!.images!!.isNotEmpty()) imagePhoto.addAll(version!!.images as ArrayList<CheminImage>)
        if (version!!.couleurs!!.isNotEmpty())couleurs.addAll(version!!.couleurs as ArrayList<Couleur>)
        if (version!!.options!!.isNotEmpty())options.addAll(version!!.options as ArrayList<Option>)


        /**
         * fin initialisation des composant
         */
        /**
         * l'initialisation des recycle view
         */

         initCouleurs(v)
         initVoituresImage(v)
         initOption(v)

        /**
         * fin initialisation
         */
        /**
         * Composer
         */
        composer(v)
        /**
         * Commander
         */
        commander(v)
        return v
    }

    private fun commander(v : View){

        commanderButton!!.setOnClickListener {
            val bundel = Bundle()
            bundel.putParcelable("command",version)
            val commandeVoitureFragment = ListeVoitureCommande()
            commandeVoitureFragment.arguments = bundel
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_id,commandeVoitureFragment,"toCommandeVoiture")
                .addToBackStack(null)
                .commit()
        }
    }

    private fun composer(v : View){
       composerButton!!.setOnClickListener{
            FragmentHelper.switchActivityExtra(this.context!!,
                CompositionActivity::class.java,this.activity!!,"version",version!! as Version)
       }
    }

    /**
     * initialiser les recycleVIew
     */
    private fun initVoituresImage(v : View){
        voitureAdapter = ImageVoitureAdapter(imagePhoto,v.context,couleurAdapter!!)
        initLineaire(v,R.id.neuf_tech_rv,LinearLayoutManager.VERTICAL, adapter = voitureAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }
    private fun initCouleurs(v : View){
        couleurAdapter = CouleurAdapter(couleurs,v.context)
        initLineaire(v,R.id.neuf_tech_rv_couleur,LinearLayoutManager.HORIZONTAL,couleurAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }
    private fun initOption(v: View?) {
        optionAdapter = OptionAdapter(options,v!!.context,1)
        initLineaire(v,R.id.io_rv_option,LinearLayoutManager.VERTICAL,optionAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)

    }

    /**
     * on stop clear the list of couleurs
     */
    override fun onStop() {
        super.onStop()
        couleurs.clear()
    }

}


