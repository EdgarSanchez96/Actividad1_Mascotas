package com.example.actividad1_mascotas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1_mascotas.adapters.PetAdapter
import com.example.actividad1_mascotas.models.Pet

class MainActivity : AppCompatActivity() {

    //Creación de la lista de objetos Pet(Mascota) con cada uno de sus atributos anteriormente definidos en la clase Pet
    private val pets = listOf(
        Pet(
            species = "Canino",
            name = "Tobi",
            breed = "Bulldog",
            sex = "Macho",
            classification = "Doméstico",
            image = R.drawable.bulldog
        ),
        Pet(
            species = "Felino",
            name = "Whiskers",
            breed = "Siamés",
            sex = "Hembra",
            classification = "Doméstico",
            image = R.drawable.siames
        ),
        Pet(
            species = "Canino",
            name = "Boby",
            breed = "Husky",
            sex = "Macho",
            classification = "Doméstico",
            image = R.drawable.husky
        ),
        Pet(
            species = "Felino",
            name = "Banby",
            breed = "Persa",
            sex = "Hembra",
            classification = "Doméstico",
            image = R.drawable.persa
        ),
        Pet(
            species = "Canino",
            name = "Leopoldo",
            breed = "Pastor Alemán",
            sex = "Macho",
            classification = "Doméstico",
            image = R.drawable.pastor_aleman
        ),
        Pet(
            species = "Felino",
            name = "Rogelio",
            breed = "Azul Ruso",
            sex = "Macho",
            classification = "Doméstico",
            image = R.drawable.azul_ruso
        )
    )

    //Se define una variable de tio RecyclerView
    private lateinit var rvPets: RecyclerView
    //Se define una variable de tipo PetAdapter
    private lateinit var petsAdapter: PetAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_NoTitleBar);
        setContentView(R.layout.activity_main)

        //Se llama al método para inicializar los componentes
        initComponent()

        //Se llama al método para asignar un Adapter al RecyclerView
        initUI()
    }

    //Función para inicializar los componentes
    private fun initComponent(){
        rvPets = findViewById(R.id.rvPets)
    }

    //Función para inicializar el RecyclerView con su Adapter
    private fun initUI(){
        petsAdapter = PetAdapter(pets)
        rvPets.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPets.adapter = petsAdapter
    }
}