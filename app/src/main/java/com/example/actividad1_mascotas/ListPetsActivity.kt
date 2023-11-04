package com.example.actividad1_mascotas

import android.content.Intent
import android.graphics.BitmapFactory
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
import com.example.actividad1_mascotas.models.TypeClassification
import com.example.actividad1_mascotas.models.TypeRefugeStatus
import com.example.actividad1_mascotas.models.TypeSex
import com.example.actividad1_mascotas.models.TypeSpecies
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListPetsActivity : AppCompatActivity() {
    //Creación de la lista de objetos Pet(Mascota)
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

    fun stringToDate(dateString: String, dateFormat: String): Date? {
        val format = SimpleDateFormat(dateFormat, Locale.getDefault())
        try {
            return format.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

    private fun getPets() {
        try {
            val fileName = "pets.json"
            val file = File(filesDir, fileName)
            if (file.exists()) {
                val json = file.readText()
                val jsonArray = JSONArray(json)
                println(jsonArray)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val dateFormat = "dd/MM/yyyy"
                    val date = stringToDate(jsonObject.getString("adoption_date"), dateFormat)
                    val imageName = jsonObject.getString("image")+".png"
                    val petImagePath = File(filesDir, imageName)
                    val imageBitmap = BitmapFactory.decodeFile(petImagePath.absolutePath)

                    if (date != null) {
                        val pet = Pet(
                            id = jsonObject.getInt("id"),
                            species = TypeSpecies.valueOf(jsonObject.getString("species")),
                            name = jsonObject.getString("name"),
                            breed = jsonObject.getString("breed"),
                            sex = TypeSex.valueOf(jsonObject.getString("sex")),
                            classification = TypeClassification.valueOf(jsonObject.getString("classification")),
                            adoption_date = date,
                            observation = jsonObject.getString("observation"),
                            refuge_status = TypeRefugeStatus.valueOf(jsonObject.getString("refuge_status")),
                            adoption_status = jsonObject.getBoolean("adoption_status"),
                            image = imageBitmap,
                            publication_status = jsonObject.getBoolean("publication_status")
                        )
                        pets.add(pet)
                    }
                }
            } else {
                println(" no existe el archivo")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println(e)
        }
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