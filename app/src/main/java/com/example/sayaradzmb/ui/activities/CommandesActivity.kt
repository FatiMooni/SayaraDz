package com.example.sayaradzmb.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import com.example.sayaradzmb.model.Commande
import com.example.sayaradzmb.ui.adapter.CommandesAdapter
import com.example.sayaradzmb.viewmodel.CommandeViewModel
import kotlinx.android.synthetic.main.activity_mycommand.*

class CommandesActivity : AppCompatActivity(), SharedPreferenceInterface {

    lateinit var model: CommandeViewModel
    lateinit var idUser: String
    private var commandeList = ArrayList<Commande>()
    private var commandeAdapter: CommandesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycommand)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = "Mes Commandes"

        //initialise the adapter
        prepareRecyclerView()

        // Load our BooksViewModel or create a new one
        model = ViewModelProviders.of(this).get(CommandeViewModel::class.java)
        // Listen for changes on the BooksViewModel
        model.getCommande().observe(this, Observer<List<Commande>> {

            commandeList.addAll(it!!)
            commandeAdapter!!.notifyDataSetChanged()

        })

        //fetch data
        idUser = avoirIdUser(this).toString()
        model.loadMesCommandes(idUser!!)

    }

    fun prepareRecyclerView() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        val adapter = findViewById<RecyclerView>(R.id.rv_command)
        adapter.layoutManager = layout
        commandeAdapter = CommandesAdapter(this, commandeList!!)
        adapter.adapter = commandeAdapter
    }
}