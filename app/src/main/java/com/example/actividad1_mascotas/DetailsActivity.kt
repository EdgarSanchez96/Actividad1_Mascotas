package com.example.actividad1_mascotas

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.actividad1_mascotas.models.Pet
import com.example.actividad1_mascotas.models.TypeClassification
import com.example.actividad1_mascotas.models.TypeRefugeStatus
import com.example.actividad1_mascotas.models.TypeSex
import com.example.actividad1_mascotas.models.TypeSpecies
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val imgPet: ImageView = findViewById(R.id.imgPet)

        val txtName: TextView = findViewById(R.id.txtName)
        val txtSpecie: TextView = findViewById(R.id.txtSpecie)
        val txtBreed: TextView = findViewById(R.id.txtBreed)
        val txtSex: TextView = findViewById(R.id.txtSex)
        var txtClassification: TextView = findViewById(R.id.txtClassification)
        var txtAdoptionDate: TextView = findViewById(R.id.txtAdoptionDate)
        var txtObservation: TextView = findViewById(R.id.txtObservation)
        var txtRefugeStatus: TextView = findViewById(R.id.txtRefugeStatus)
        var txtAdoptionStatus: TextView = findViewById(R.id.txtAdoptionStatus)
        var txtPublicationStatus: TextView = findViewById(R.id.txtPublicationStatus)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainConstraintLayout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Obtiene el color de fondo del ConstraintLayout principal
            val backgroundColor = (constraintLayout.background as? ColorDrawable)?.color
            // Verifica si el color es nulo
            if (backgroundColor != null) {
                // Aplica el color al statusBarColor si no es nulo
                window.statusBarColor = backgroundColor
                window.navigationBarColor = backgroundColor
            }
        }

        val data: Bundle? = intent.extras
        if (data != null) {
            // Al obtener la posición es necesario restarle 1
            // Esto se hace porque el array comienza desde 0
            var id: Int = data.getInt("id") - 1

            // El siguiente codigo permite recorrer el json que utiliza la app
            try {
                val fileName = "pets.json"
                val fileRead = File(filesDir, fileName)
                if (fileRead.exists()) {
                    val json = fileRead.readText()
                    val jsonArray = JSONArray(json)

                    val petList = mutableListOf<Pet>()
                    for (i in 0 until jsonArray.length()) {
                        val dateFormat = "dd/MM/yyyy"
                        val jsonObject = jsonArray.getJSONObject(i)
                        val date_pet =
                            stringToDate(jsonObject.getString("adoption_date"), dateFormat)
                        val imageName = jsonObject.getString("image") + ".png"

                        // Recupera el recurso de imagen desde el almacenamiento interno
                        val petImagePath = File(filesDir, imageName)
                        val imageBitmap = BitmapFactory.decodeFile(petImagePath.absolutePath)
                        if (date_pet != null) {
                            val pet = Pet(
                                id = jsonObject.getInt("id"),
                                species = TypeSpecies.valueOf(jsonObject.getString("species")),
                                name = jsonObject.getString("name"),
                                breed = jsonObject.getString("breed"),
                                sex = TypeSex.valueOf(jsonObject.getString("sex")),
                                classification = TypeClassification.valueOf(
                                    jsonObject.getString(
                                        "classification"
                                    )
                                ),
                                adoption_date = date_pet,
                                observation = jsonObject.getString("observation"),
                                refuge_status = TypeRefugeStatus.valueOf(jsonObject.getString("refuge_status")),
                                adoption_status = jsonObject.getBoolean("adoption_status"),
                                image = imageBitmap,
                                publication_status = jsonObject.getBoolean("publication_status")
                            )
                            petList.add(pet)
                        }
                    }

                    // Se recupera el registro que se presentara en detalle
                    var petObtain: Pet = petList.get(id)

                    // Agregar datos del json a la vista
                    imgPet.setImageBitmap(petObtain.image)
                    txtName.text = petObtain.name
                    txtSpecie.text = petObtain.species.toString()
                    txtBreed.text = petObtain.breed
                    txtSex.text = petObtain.sex.toString()
                    txtClassification.text = petObtain.classification.toString()
                    txtAdoptionDate.text = petObtain.adoption_date.toString()
                    txtObservation.text = petObtain.observation.toString()
                    txtRefugeStatus.text = petObtain.refuge_status.toString()
                    var adopStatus: Boolean = petObtain.adoption_status
                    txtAdoptionStatus.text = if (adopStatus) "SI" else "NO"
                    var publicStatus = petObtain.publication_status
                    txtPublicationStatus.text = if (publicStatus) "SI" else "NO"

                }
            } catch (e: IOException) {
                println(e)
            }
        }

        // Boton volver
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, ListPetsActivity::class.java)
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
}