package com.example.sayaradzmb.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.RecycleViewHelper
import com.example.sayaradzmb.model.Couleur
import com.example.sayaradzmb.model.Option
import com.example.sayaradzmb.model.Version
import com.example.sayaradzmb.model.VoitureCommande
import com.example.sayaradzmb.servics.ServiceBuilder
import com.example.sayaradzmb.ui.adapter.CouleurAdapter
import com.example.sayaradzmb.ui.adapter.ImageVoitureAdapter
import com.example.sayaradzmb.ui.adapter.OptionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import com.example.sayaradzmb.Repository.servics.StockService


class CompositionActivity : AppCompatActivity(),RecycleViewHelper {

    /**
     * varibale prvate
     */
    private var version: Version? = null
    private var voitureAdapter: ImageVoitureAdapter? = null
    private var couleurAdapter: CouleurAdapter? = null
    private var optionAdapter: OptionAdapter? = null
    private var imagePhoto = ArrayList<String>()
    private var couleurs = ArrayList<Couleur>()
    private var options = ArrayList<Option>()
    private var view :View?=null
    /**
     * Composant design
     */
    private var disponibilite: TextView? = null
    private var prixVoiture: TextView? = null
    private var commanderButton: Button? = null
    private var listVoiture = ArrayList<VoitureCommande>()
    private var voitureSelectionne : VoitureCommande? = null
    private var priceContainer : LinearLayout?=null

    /**
     * Comparison test
     */
    private var currentColor :String? = null
    private var currentOptions = ArrayList<Int>()
    /**
     * On create
     */
    init {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_composition)
        /**
         * avoir la version
         */
        version = intent.getParcelableExtra("version")
        Log.i("version : ",version.toString())
        avoirListVoiture()
        /**
         * initialiser les composant
         */
        view = findViewById(android.R.id.content)
        commanderButton = findViewById(R.id.compose_button)
        disponibilite = findViewById(R.id.compose_text_dispo)
        prixVoiture = findViewById(R.id.compose_text_prix)
        priceContainer = findViewById(R.id.compose_text_dispo_container)

        /**
         * remplire les componnet
         */
        if (version!!.couleurs!!.isNotEmpty())couleurs.addAll(version!!.couleurs as ArrayList<Couleur>)
        if (version!!.options!!.isNotEmpty())options.addAll(version!!.options as ArrayList<Option>)
        /**
         * initialisr current color and current option
         */
        currentColor = couleurs[0].CodeHexa
        /**
         * initier les recycle view
         */
        initCouleurs(view!!)
        initVoituresImage(view!!)
        initOption(view!!)
        /**
         * attach composition
         */
        couleurAdapter!!.attachComposition(this@CompositionActivity)
        optionAdapter!!.attachComposition(this@CompositionActivity)
        /**
         * Commander
         */

        commander(view!!)
    }

    /**
     * fonction commander
     */
    private fun commander(v: View) {
        commanderButton!!.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("voitureCommande", voitureSelectionne)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    /**
     * initialiser les recycleVIew
     */
    private fun initVoituresImage(v: View) {
        voitureAdapter = ImageVoitureAdapter(imagePhoto, v.context, couleurAdapter!!)
        initLineaire(v, R.id.compose_image_rv, LinearLayoutManager.VERTICAL, adapter = voitureAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun initCouleurs(v: View) {
        couleurAdapter = CouleurAdapter(couleurs, v.context)
        initLineaire(v, R.id.compose_color_rv, LinearLayoutManager.HORIZONTAL, couleurAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    private fun initOption(v: View?) {
        optionAdapter = OptionAdapter(options, v!!.context, 0)
        initLineaire(v, R.id.compose_rv_option, LinearLayoutManager.VERTICAL, optionAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
    }

    /**
     * on stop clear the list of couleurs
     */
    override fun onStop() {
        super.onStop()
        couleurs.clear()
    }

    /**
     * la requte d'avoir les differnet vehiule
     */
    fun avoirListVoiture(){
        var progress = ProgressDialog(this@CompositionActivity,android.R.style.Theme_DeviceDefault_Dialog)
        progress.setCancelable(false)
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
                        e.nomVersion = version!!.NomVersion
                        e.codeMarque = version!!.CodeMarque
                        listVoiture.add(e)
                    }
                    Log.i("list Voiture",listVoiture.toString())
                    progress.dismiss()
                    isDisponible()
                }else{

                }
            override fun onFailure(call: Call<List<VoitureCommande>>, t: Throwable) {
                Log.w("failConnexion","la liste marue non reconnue")
                progress.dismiss()
                avoirListVoiture()
            }
        })
    }


    /**
     * update couleur
     */
    fun update(couleur: String){
        currentColor  = couleur
        println("current Color"+currentColor)
        isDisponible()
    }

    fun updateOption(options : ArrayList<Int>){
        currentOptions = options
        println("current options"+currentOptions)
        isDisponible()
    }


    /**
     * test Disponibilite
     */
    private fun isDisponible(){
        // vas : current option / currentCouleur / version
        // savoir si il existe cette couleur
        println("dans is Disponible")
        listVoiture.forEach { voiture ->
            var voitureOption = ArrayList<Int>()
            // price options
            var priceOptions = 0
            //price Color
            var priceCouleur = voiture.Couleur!!.tarifCouleur!!.Prix
            // price base
            var priceBase = voiture.tarifBase!!.Prix

            voiture.options.forEach {
                voitureOption.add(it.CodeOption!!)
                priceOptions+= it.tarifOption!!.Prix
            }
            var priceTotal = priceBase+priceCouleur+priceOptions
            voiture.prixTotal = priceTotal
            voiture.Image = version?.couleurs?.get(0)?.CheminImage
            //avoir price
            if ((currentColor == voiture.Couleur!!.CodeHexa) && (existArray(currentOptions,voitureOption))){
                Log.i("we have a match here : ",voitureOption.toString())
                commanderButton!!.visibility = View.VISIBLE
                disponibilite!!.text = "DISPONIBLE"
                priceContainer!!.background = getDrawable(R.drawable.indiponible)
                prixVoiture!!.text = "$priceTotal DA "
                voitureSelectionne = voiture
                return
            }else{
                commanderButton!!.visibility = View.GONE
                disponibilite!!.text = "NON DISPONIBLE"
                priceContainer!!.background = getDrawable(R.drawable.disponible)
                prixVoiture!!.text = " -- "
            }
        }
    }


    /**
     * if element ofarray exist in another array
     */
    private  fun existArray(arrayOne : ArrayList<Int> ,arrayTwo : ArrayList<Int>) : Boolean{
        if(arrayOne.size != arrayTwo.size) return false
        arrayOne.forEach {
            if (!arrayTwo.contains(it)) return false
        }
        return true
    }
}
