package com.example.sayaradzmb.ui.activities.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Annonce
import com.example.sayaradzmb.ui.activities.AjouterAnnonceActivity
import com.example.sayaradzmb.ui.activities.AnnonceOffersActivity
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
import com.example.sayaradzmb.ui.adapter.VehiculeImageAdapter
import com.example.sayaradzmb.viewmodel.UserAnnonceViewModel
import kotlinx.android.synthetic.main.annonce_view.view.*

class AnnonceFragment : Fragment(), SharedPreferenceInterface {

    /****
     *** This fragment to display the different
     ***/
    private var annoncesList = ArrayList<Comparable<*>>()
    private lateinit var customAdapter: CustomCardsAdapter
    private lateinit var activityView: View
    private lateinit var model: UserAnnonceViewModel

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
            startActivity(intent)
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
                                (value as Annonce).CodeCouleur.toString()
                            mDialogView.findViewById<TextView>(R.id.year_title).text = "jan 2018"
                            mDialogView.findViewById<TextView>(R.id.type_title).text = (value as Annonce).Carburant
                            mDialogView.findViewById<TextView>(R.id.text_description).text =
                                (value as Annonce).Description
                            mDialogView.findViewById<ViewPager>(R.id.images_viewer).adapter = pAdapter


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