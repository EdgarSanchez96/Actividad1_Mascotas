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
import org.json.JSONArray
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListPetsActivity : AppCompatActivity() {
    //Creación de la lista de objetos Pet(Mascota) con cada uno de sus atributos anteriormente definidos en la clase Pet
    private val pets = mutableListOf<Pet>()
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

        try {
            // Lee el archivo JSON desde el directorio "res/raw"
            val inputStream: InputStream = resources.openRawResource(R.raw.pets)
            val json = inputStream.bufferedReader().use { it.readText() }

            // Parsea el JSON y agrega las mascotas a la lista
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

                val pet = Pet(
                    id = jsonObject.getInt("id"),
                    species = TypeSpecies.valueOf(jsonObject.getString("species")),
                    name = jsonObject.getString("name"),
                    breed = jsonObject.getString("breed"),
                    sex = TypeSex.valueOf(jsonObject.getString("sex")),
                    classification = TypeClassification.valueOf(jsonObject.getString("classification")),
                    adoption_date = dateFormat.parse(jsonObject.getString("adoption_date")),
                    observation = jsonObject.getString("observation"),
                    refuge_status = TypeRefugeStatus.valueOf(jsonObject.getString("refuge_status")),
                    adoption_status = jsonObject.getBoolean("adoption_status"),
                    image = resources.getIdentifier(jsonObject.getString("image"), "drawable", packageName),
                    publication_status = jsonObject.getBoolean("publication_status")
                )
                pets.add(pet)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println(e)
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
        rvPets.adapter = petsAdapter
    }
}