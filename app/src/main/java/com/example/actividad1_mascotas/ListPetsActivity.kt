package com.example.actividad1_mascotas

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1_mascotas.adapters.PetAdapter
import com.example.actividad1_mascotas.models.Pet
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListPetsActivity : AppCompatActivity() {
    //Creación de la lista de objetos Pet(Mascota)
    private var pets = mutableListOf<Pet>()

    //Se define una variable de tio RecyclerView
    private lateinit var rvPets: RecyclerView

    //Se define una variable de tipo PetAdapter
    private lateinit var petsAdapter: PetAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pets)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainConstraintLayout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val backgroundColor = (constraintLayout.background as? ColorDrawable)?.color
            if (backgroundColor != null) {
                window.statusBarColor = backgroundColor
                window.navigationBarColor = backgroundColor
            }
        }
        getPets()
        //Se llama al método para inicializar los componentes
        initComponent()

        //Se llama al método para asignar un Adapter al RecyclerView
        initUI()
        val btnRegisterPet = findViewById<FloatingActionButton>(R.id.fabAddPet)
        btnRegisterPet.setOnClickListener {
            val intent = Intent(this, RegisterPetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Obtiene el listado de todas las mascotas
    private fun getPets() {
        val generalFunctions = GeneralFunctions(filesDir)
        val petsList = generalFunctions.getListPets()
        pets.clear()
        pets.addAll(petsList)
    }

    //Función para inicializar los componentes
    private fun initComponent() {
        rvPets = findViewById(R.id.rvPets)
    }

    //Función para inicializar el RecyclerView con su Adapter
    private fun initUI() {
        petsAdapter = PetAdapter(pets)
        rvPets.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //Evento al realizar un click sobre un item del RecyclerView
        petsAdapter.setOnRecyclerViewClickListener(object : PetAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Imprime un mensaje por consola de la acción del click
                Log.d("RecyclerViewClick", "Hiciste clic en el elemento en la posición $position")
                //Realizar los Intent para rediriguir a la pantalla de detalle al hacer click a un item del RecyclerView...

                val intent = Intent(this@ListPetsActivity, DetailsActivity::class.java)
                intent.putExtra("id", pets[position].id)
                startActivity(intent)
            }
        })

        rvPets.adapter = petsAdapter
    }
}