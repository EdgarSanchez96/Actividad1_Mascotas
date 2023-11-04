package com.example.actividad1_mascotas

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1_mascotas.adapters.PetAdapter
import com.example.actividad1_mascotas.models.Pet
import com.example.actividad1_mascotas.models.TypeClassification
import com.example.actividad1_mascotas.models.TypeRefugeStatus
import com.example.actividad1_mascotas.models.TypeSex
import com.example.actividad1_mascotas.models.TypeSpecies
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Date

class ListPetsActivity : AppCompatActivity() {
    //Creación de la lista de objetos Pet(Mascota) con cada uno de sus atributos anteriormente definidos en la clase Pet
    private val pets = listOf(
        Pet(
            species = TypeSpecies.CANINO,
            name = "Tobi",
            breed = "Bulldog",
            sex = TypeSex.MACHO,
            classification = TypeClassification.ADULTO,
            adoption_date = Date(),
            observation = "Observación de Tobi",
            refuge_status = TypeRefugeStatus.FIJO,
            adoption_status = true,
            image = R.drawable.bulldog,
            publication_status = true
        ),
        Pet(
            species = TypeSpecies.FELINO,
            name = "Whiskers",
            breed = "Siamés",
            sex = TypeSex.HEMBRA,
            classification = TypeClassification.ADULTO,
            adoption_date = Date(),
            observation = "Observación de Whiskers",
            refuge_status = TypeRefugeStatus.TEMPORAL,
            adoption_status = false,
            image = R.drawable.siames,
            publication_status = true
        ),
        Pet(
            species = TypeSpecies.CANINO,
            name = "Boby",
            breed = "Husky",
            sex = TypeSex.MACHO,
            classification = TypeClassification.CACHORRO,
            adoption_date = Date(),
            observation = "Observación de Boby",
            refuge_status = TypeRefugeStatus.FIJO,
            adoption_status = true,
            image = R.drawable.husky,
            publication_status = true
        ),
        Pet(
            species = TypeSpecies.FELINO,
            name = "Banby",
            breed = "Persa",
            sex = TypeSex.HEMBRA,
            classification = TypeClassification.ADULTO,
            adoption_date = Date(),
            observation = "Observación de Banby",
            refuge_status = TypeRefugeStatus.FIJO,
            adoption_status = false,
            image = R.drawable.persa,
            publication_status = true
        ),
        Pet(
            species = TypeSpecies.CANINO,
            name = "Leopoldo",
            breed = "Pastor Alemán",
            sex = TypeSex.MACHO,
            classification = TypeClassification.CACHORRO,
            adoption_date = Date(),
            observation = "Observación de Leopoldo",
            refuge_status = TypeRefugeStatus.TEMPORAL,
            adoption_status = false,
            image = R.drawable.pastor_aleman,
            publication_status = true
        ),
        Pet(
            species = TypeSpecies.FELINO,
            name = "Rogelio",
            breed = "Azul Ruso",
            sex = TypeSex.MACHO,
            classification = TypeClassification.CACHORRO,
            adoption_date = Date(),
            observation = "Observación de Rogelio",
            refuge_status = TypeRefugeStatus.FIJO,
            adoption_status = true,
            image = R.drawable.azul_ruso,
            publication_status = true
        )
    )
    //Se define una variable de tio RecyclerView
    private lateinit var rvPets: RecyclerView
    //Se define una variable de tipo PetAdapter
    private lateinit var petsAdapter: PetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pets)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainConstraintLayout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtiene el color de fondo del ConstraintLayout principal
            val backgroundColor = (constraintLayout.background as? ColorDrawable)?.color
            println(backgroundColor)

            // Verifica si el color es nulo
            if (backgroundColor != null) {
                // Aplica el color al statusBarColor si no es nulo
                window.statusBarColor = backgroundColor
                window.navigationBarColor = backgroundColor
            }
        }
        //Se llama al método para inicializar los componentes
        initComponent()

        //Se llama al método para asignar un Adapter al RecyclerView
        initUI()
        val btnRegisterPet = findViewById<FloatingActionButton>(R.id.fabAddPet)
        btnRegisterPet.setOnClickListener {
            val intent = Intent(this, RegisterPetActivity::class.java)
            startActivity(intent)
        }
    }
    //Función para inicializar los componentes
    private fun initComponent(){
        rvPets = findViewById(R.id.rvPets)
    }

    //Función para inicializar el RecyclerView con su Adapter
    private fun initUI(){
        petsAdapter = PetAdapter(pets)
        rvPets.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //Evento al realizar un click sobre un item del RecyclerView
        petsAdapter.setOnRecyclerViewClickListener(object : PetAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Imprime un mensaje por consola de la acción del click
                Log.d("RecyclerViewClick", "Hiciste clic en el elemento en la posición $position")
                //Realizar los Intent para rediriguir a la pantalla de detalle al hacer click a un item del RecyclerView...
            }
        })

        rvPets.adapter = petsAdapter
    }
}