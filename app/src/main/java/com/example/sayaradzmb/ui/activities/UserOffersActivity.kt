package com.example.sayaradzmb.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Offre
import com.example.sayaradzmb.model.UserOffre
import com.example.sayaradzmb.ui.adapter.CustomCardsAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_offers)
        setSupportActionBar(findViewById(R.id.toolbar))

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }

       // toolbar.title = "Mes Offres"


        //initialize the adapter
        prepareRecyclerView()

        // Load our BooksViewModel or create a new one
        model = ViewModelProviders.of(this).get(UserOffersViewModel::class.java)
        // Listen for changes on the BooksViewModel
        model.getOffre().observe(this, Observer<List<UserOffre>> {

            offerAdapter.swapData(it!!)

        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(holder: RecyclerView.ViewHolder, p1: Int) {
                val item = offerAdapter.getItemAt(holder.adapterPosition)
                if (item is UserOffre)
                    model.deleteOffer((item).idOffre , holder.adapterPosition)
                else throw IllegalArgumentException("Invalid view type")
            }

        }).attachToRecyclerView(rv_offers)


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
                                value.vehicule.NomMarque.plus(" "+value.vehicule.NomModele).plus(" "+value.vehicule.NomVersion)
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

    private fun prepareRecyclerView() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        val adapter = findViewById<RecyclerView>(R.id.rv_offers)
        adapter.layoutManager = layout
        offerAdapter = CustomCardsAdapter(this, offerList)
        adapter.adapter = offerAdapter
    }
}