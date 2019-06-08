package com.example.sayaradzmb.activities.Fragments

import android.annotation.SuppressLint
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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.sayaradzmb.adapter.CouleurAdapter
import com.example.sayaradzmb.adapter.ImageVoitureAdapter
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.Couleur
import com.example.sayaradzmb.model.Option
import com.example.sayaradzmb.model.cheminImage
import com.example.sayaradzmb.model.version
import com.squareup.picasso.Picasso
import me.relex.circleindicator.CircleIndicator2



@SuppressLint("ValidFragment")
class NouveauAfficheTechnique @SuppressLint("ValidFragment") constructor(
    var version: version
) : Fragment(),RecycleViewHelper{
    override var itemRecycleView: RecyclerView? = null
    private var voitureRecyclerView: RecyclerView? = null
    private var voitureAdapter: ImageVoitureAdapter? = null
    private var couleurRecyclerView: RecyclerView? = null
    private var couleurAdapter: CouleurAdapter? = null
    private var onCommandPressed : NouveauAfficheTechnique.OnCommandPressed? = null
    private var indicator : CircleIndicator2? = null

    /**
     * composant design
     */
    private var nomVoiture : TextView?=null
    private var commanderButton : Button?=null
    private var description :TextView?=null
    private var suiviImage : ImageView?=null
    private var prixVoiture : TextView?= null
    private var imagePhoto = ArrayList<cheminImage>()
    private var couleurs = ArrayList<String>()
    private var options = ArrayList<Option>()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragement_neuf_tech,container,false)
        val context = v.context
        Log.i("version : ",version.toString())
        nomVoiture = v.findViewById(R.id.neuf_tech_card_nom_car)
        commanderButton = v.findViewById(R.id.neuf_tech_card_commande_button)
        description = v.findViewById(R.id.neuf_tech_card_description)
        suiviImage = v.findViewById(R.id.neuf_tech_card_suivi)
        prixVoiture = v.findViewById(R.id.neuf_tech_card_prix)

        /**
         * initialisation
         */
        nomVoiture!!.text = version.NomVersion
        suiviImage!!.setOnClickListener {
            if (suiviImage!!.tag == "nonSuivi"){
                /**
                 * faire l'abonnement
                 */
                Picasso.get().load(R.drawable.heart).into(suiviImage)
                suiviImage!!.tag = "Suivi"
            }else{
                /**
                 * desabonner
                 */
                Picasso.get().load(R.drawable.heart_empty).into(suiviImage)
                suiviImage!!.tag = "nonSuivi"
            }
        }
        if(version.lignetarif != null) prixVoiture!!.text = "${version.lignetarif!!.Prix.toString()!!} DZD"
        else prixVoiture!!.text = "price not defined"

        if (!version.images!!.isEmpty()) imagePhoto.addAll(version.images as ArrayList<cheminImage>)
        if (!version.couleurs!!.isEmpty())couleurs.addAll(version.couleurs as ArrayList<String>)
        if (!version.options!!.isEmpty())options.addAll(version.options as ArrayList<Option>)


        /**
         * fin initialisation des composant
         */
        /**
         * l'initialisation des recycle view
         */
         initVoituresImage(v)
         initCouleurs(v)
            couleurs.add("a")
        couleurAdapter!!.notifyDataSetChanged()
        /**
         * fin initialisation
         */
        //circle indicatore
        //val pagerSnapHelper = PagerSnapHelper()
        //pagerSnapHelper.attachToRecyclerView(voitureRecyclerView)
        //indicator = v.findViewById<CircleIndicator2>(R.id.neuf_tech_indicator)
        //indicator.attachToRecyclerView(voitureRecyclerView,pagerSnapHelper)


        commander(v)
        return v
    }








    private fun commander(v : View){
        val commander = v.findViewById<Button>(R.id.neuf_tech_card_commande_button)
        commander.setOnClickListener {
            onCommandPressed!!.envoyerFragment(2)
        }
    }




    /**
     * l'interface qui aide a envoyer des donnee d'un fragment a l'activity
     */
    interface OnCommandPressed{
        public fun envoyerFragment(int : Int)
    }

    /**
     *
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = context as Activity
        onCommandPressed = activity as OnCommandPressed
    }

    /**
     * initialiser les recycleVIew
     */
    private fun initVoituresImage(v : View){
        voitureAdapter = ImageVoitureAdapter(imagePhoto,v.context)
        initLineaire(v,R.id.neuf_tech_rv,LinearLayoutManager.VERTICAL,voitureAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }
    private fun initCouleurs(v : View){
        couleurAdapter = CouleurAdapter(couleurs,v.context)
        initLineaire(v,R.id.neuf_tech_rv_couleur,LinearLayoutManager.VERTICAL,couleurAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }
}


