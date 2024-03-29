package com.example.sayaradzmb.ui.activities.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.ui.activities.AjouterAnnonceActivity
import com.example.sayaradzmb.ui.activities.AnnonceOffersActivity
import com.example.sayaradzmb.ui.adapter.CarImageSliderAdapter
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
import com.example.sayaradzmb.ui.adapter.VehiculeImageAdapter
import com.example.sayaradzmb.viewmodel.UserAnnonceViewModel
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.annonce_view.view.*

class AnnonceFragment : Fragment(), SharedPreferenceInterface {

    /****
     *** This fragment to display the different
     ***/
    private var annoncesList = ArrayList<Comparable<*>>()
    private lateinit var customAdapter: CustomCardsAdapter
    private lateinit var activityView: View
    private lateinit var model: UserAnnonceViewModel
    var idUser : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activityView = inflater.inflate(
            R.layout.fragement_annonce,
            container,
            false
        )

        // preparer Recycler view
        intialiserRecyclerView()

        idUser = avoirIdUser(activityView.context).toString()
        //Recuperer les annonces
        model = ViewModelProviders.of(this).get(UserAnnonceViewModel::class.java)

        model.getAnnonce().observe(this, Observer {
            customAdapter.swapData(it!!)
        })

        //pour passer à une autre vue : ajouter une nouvelle annonce
        val addBtn = activityView.findViewById<FloatingActionButton>(R.id.ajouter_annonce_button)

        addBtn.setOnClickListener {
            // preparé l'activité d'ajout
            val intent = Intent(activityView.context, AjouterAnnonceActivity::class.java)
            // lancer l'activité
            startActivityForResult(intent,123)
        }
        customAdapter.setOnItemClickListener(object : CustomCardsAdapter.OnClickItemListener {
            override fun onClickItem(id: Int, state: String, position: Int) {

            }

            override fun onPopupMenuRequested(value: Comparable<*>, view: View, position: Int) {
                val popup = PopupMenu(view.context, view.delete_icon_btn)
                val inflat: MenuInflater = popup.menuInflater
                inflat.inflate(R.menu.card_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    //do your things in each of the following cases
                    when (item.itemId) {
                        R.id.delete_annonce -> {
                            model.supprimerAnnonce((value as Annonce).idAnnonce!!, position)
                            true
                        }
                        R.id.edit_annonce -> {
                            //Inflate the dialog --> ajouter le contenu en xml au dialog
                            val mDialogView =
                                LayoutInflater.from(context).inflate(R.layout.edit_price_annonce, null)
                            //AlertDialogBuilder
                            val mBuilder = AlertDialog.Builder(context!!)
                                .setView(mDialogView)
                                .setIcon(R.drawable.cancel)

                            mBuilder.setNegativeButton("Annuler") { dialog, which ->
                                // Do something when user press the positive button
                                Toast.makeText(context, "the price hasn't been changed", Toast.LENGTH_SHORT).show()
                            }

                            mBuilder.setPositiveButton("Confirmer") { dialog, which ->
                                val price = mDialogView.findViewById<TextInputEditText>(R.id.prix_edit).text.toString()
                                model.UpdateAnnoncePrice((value as Annonce).idAnnonce!!, price)
                                // Do something when user press the positive button
                                Toast.makeText(context, "the price has successfully changed", Toast.LENGTH_SHORT).show()
                            }

                            mBuilder.create()
                            mBuilder.show()

                            true
                        }
                        R.id.apercu_annonce -> {
                            //Inflate the dialog --> ajouter le contenu en xml au dialog
                            val mDialogView =
                                LayoutInflater.from(context).inflate(R.layout.content_apercu_annonce, null)

                            //AlertDialogBuilder
                            val mBuilder = AlertDialog.Builder(context!!)
                                .setView(mDialogView)
                                .setIcon(R.drawable.cancel)

                            //init the adapter
                            val pAdapter = VehiculeImageAdapter(mBuilder.context, (value as Annonce).images!!)
                            val ver = value.version!!
                            //Setting the data
                            mDialogView.findViewById<TextView>(R.id.marque_title).text =
                                ver.NomMarque.plus(" ").plus(ver.NomModele).plus(" ").plus(ver.NomVersion)

                            mDialogView.findViewById<TextView>(R.id.prix_specification).text = (value as Annonce).Prix
                            mDialogView.findViewById<TextView>(R.id.KM_title).text = (value as Annonce).Km
                            mDialogView.findViewById<TextView>(R.id.color_title).text =
                                (value as Annonce).Couleur
                            mDialogView.findViewById<TextView>(R.id.year_title).text = value.Annee.toString()
                            mDialogView.findViewById<TextView>(R.id.type_title).text = (value as Annonce).Carburant
                            mDialogView.findViewById<TextView>(R.id.text_description).text =
                                (value as Annonce).Description
                            mDialogView.findViewById<Button>(R.id.btn_offer).visibility = View.GONE
                            val adapter = CarImageSliderAdapter(mBuilder.context, value.images!!)
                            val sliderView = mDialogView.findViewById<SliderView>(R.id.images_viewer)
                            sliderView.sliderAdapter = adapter
                            sliderView.startAutoCycle()
                            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
                            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                            sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                            sliderView.indicatorSelectedColor = Color.rgb(30,94,162)
                            sliderView.indicatorUnselectedColor = Color.GRAY
                            sliderView.scrollTimeInSec = 7 //set scroll delay in seconds :
                            sliderView.startAutoCycle()

                            //afficher le dialog
                            mBuilder.show()
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }

            override fun onButtonClickItem(value: Comparable<*>, position: Int) {
                val intent = Intent(context, AnnonceOffersActivity::class.java)
                intent.putExtra("annonce", value as Annonce)
                startActivity(intent)
            }

        })
        model.loadAnnounces(idUser)

        /*refresh_layout.setOnRefreshListener {
            recupereAnnonce(idUser)
            customAdapter.notifyDataSetChanged()
        }*/
        return activityView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            Log.i("results" , requestCode.toString() + resultCode.toString())
        if (requestCode == 123 && resultCode == Activity.RESULT_OK ){
            if (data!!.hasExtra("annonce")){
            model.loadAnnounces(idUser)
            }
            else {
                Toast.makeText(context, "Wrong data received",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun intialiserRecyclerView() {
        val rv = activityView.findViewById<RecyclerView>(R.id.recyler_view_annonce)
        val layout = LinearLayoutManager(activityView.context)
        layout.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layout
        customAdapter =
            CustomCardsAdapter(activityView.context!!, annoncesList)
        rv.adapter = customAdapter

    }




}