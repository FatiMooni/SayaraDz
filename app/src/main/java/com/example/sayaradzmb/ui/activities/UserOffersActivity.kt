package com.example.sayaradzmb.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.UserOffre
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
import com.example.sayaradzmb.ui.adapter.ItemLookup
import com.example.sayaradzmb.viewmodel.UserOffersViewModel
import kotlinx.android.synthetic.main.activity_user_command.*
import kotlinx.android.synthetic.main.content_annonce_offers.*
import kotlinx.android.synthetic.main.user_offer_card.view.*


class UserOffersActivity : AppCompatActivity(), SharedPreferenceInterface {

    //**** Variables ****//
    private lateinit var offerAdapter: CustomCardsAdapter
    lateinit var model: UserOffersViewModel
    lateinit var idUser: String
    private var offerList = ArrayList<Comparable<*>>()
    var adapter: RecyclerView? = null


    /***
     * Pour utiliser la multi selection du recycler view
     */
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_offers)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState != null)
            tracker?.onRestoreInstanceState(savedInstanceState)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }


        //initialize the adapter
        prepareRecyclerView()

        /** Use the selection builder to initialize the tracker **/
        tracker = SelectionTracker.Builder<Long>(
            "selection-1",
            rv_offers,
            StableIdKeyProvider(rv_offers),
            ItemLookup(rv_offers),
            StorageStrategy.createLongStorage() //Since that the id is Long
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        offerAdapter.setTracker(tracker)

        // Load our BooksViewModel or create a new one
        model = ViewModelProviders.of(this).get(UserOffersViewModel::class.java)
        // Listen for changes on the BooksViewModel
        model.getOffre().observe(this, Observer<List<UserOffre>> {

            offerAdapter.swapData(it!!)
            Toast.makeText(this@UserOffersActivity, it.toString(), Toast.LENGTH_SHORT).show()

        })

        val snackbar = Snackbar
            .make(rv_offers, "Message is deleted", Snackbar.LENGTH_INDEFINITE)
            .setAction("DELETE") {
                val Items = tracker?.selection?.size()
                AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Suppression de $Items Offres")
                    .setMessage("Etes-vous sures de supprimer ces offres ?")
                    .setPositiveButton("Oui") { _, _ ->
                        tracker?.selection?.forEach {
                            model.deleteOffer(it.toInt())
                        }
                    }
                    .setNegativeButton("Annuler", null)
                    .show()
            }

        /*** Use an observer to set actions to the tracker **/
        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    val nItems: Int? = tracker?.selection?.size()
                    toolbar.title = if (nItems != null && nItems > 0) {

                        if (nItems == 1) snackbar.show()
                        // Change title and color of action bar
                        "$nItems offres séléctionnés"

                    } else {
                        snackbar.dismiss()
                        // Reset title to default values
                        "Mes Offres"

                    }


                }
            })




        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(holder: RecyclerView.ViewHolder, p1: Int) {
                val item = offerAdapter.getItemAt(holder.adapterPosition)
                if (item is UserOffre) {
                    model.deleteOffer((item).idOffre, holder.adapterPosition)
                } else throw IllegalArgumentException("Invalid view type")
            }

        }).attachToRecyclerView(adapter)


        offerAdapter.setOnItemClickListener(object : CustomCardsAdapter.OnClickItemListener {
            override fun onPopupMenuRequested(value: Comparable<*>, view: View, position: Int) {
                val popup = PopupMenu(view.context, view.card_menu)
                val inflater = popup.menuInflater
                inflater.inflate(R.menu.offer_card_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    //do your things in each of the following cases
                    when (item.itemId) {
                        R.id.btn_dcln -> {
                            model.updateOffersList((value as UserOffre).idOffre, "annuler", position)
                            true
                        }
                        R.id.btn_aperçu -> {
                            val intent = Intent(view.context, AnnonceApercuActivity::class.java)

                            intent.putExtra(
                                AnnonceApercuActivity.EXTRA_ANNONCE_ID,
                                (value as UserOffre).annonce.idAnnonce
                            )
                            intent.putExtra(
                                AnnonceApercuActivity.EXTRA_AUTOMOBILISTE,
                                value.automobiliste
                            )

                            intent.putExtra(
                                AnnonceApercuActivity.EXTRA_ANNONCE_NAME,
                                value.vehicule.NomMarque.plus(" " + value.vehicule.NomModele).plus(" " + value.vehicule.NomVersion)
                            )
                            // lancer l'activité
                            ContextCompat.startActivity(view.context, intent, null)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }

            override fun onClickItem(id: Int, state: String, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        //fetch data
        idUser = avoirIdUser(this).toString()
        model.loadOffers(idUser)

    }

    // To save the state of the tracker
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (outState != null)
            tracker?.onSaveInstanceState(outState)
    }

    private fun prepareRecyclerView() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        adapter = findViewById<RecyclerView>(R.id.rv_offers)
        adapter!!.layoutManager = layout
        offerAdapter = CustomCardsAdapter(this, offerList)
        adapter!!.adapter = offerAdapter
    }
}