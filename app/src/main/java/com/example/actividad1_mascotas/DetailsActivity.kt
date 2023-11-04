package com.example.actividad1_mascotas

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.actividad1_mascotas.models.Pet
import com.example.actividad1_mascotas.models.TypeClassification
import com.example.actividad1_mascotas.models.TypeRefugeStatus
import com.example.actividad1_mascotas.models.TypeSex
import com.example.actividad1_mascotas.models.TypeSpecies
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
//        val txtSpecie: TextView = findViewById(R.id.txtSpecie)
//        val txtBreed: TextView = findViewById(R.id.txtBreed)
//        val txtSex: TextView = findViewById(R.id.txtSex)

//        val txtClassification: TextView = findViewById(R.id.txtClassification)
//        val txtAdoptionDate: TextView = findViewById(R.id.txtAdoptionDate)
//        val txtObservation: TextView = findViewById(R.id.txtObservation)
//        val txtRefugeStatus: TextView = findViewById(R.id.txtRefugeStatus)
//        val txtAdoptionStatus: TextView = findViewById(R.id.txtAdoptionStatus)
//        val txtImage: TextView = findViewById(R.id.txtImage)
//        val txtPublicationStatus: TextView = findViewById(R.id.txtPublicationStatus)

        val data: Bundle? = intent.extras
        if (data != null) {
            var id: Int = data.getInt("id")
            
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
                        val imageName = jsonObject.getString("image")+".png"

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

                    var petObtain:Pet = petList.get(id)

                    imgPet.setImageBitmap(petObtain.image)
                    txtName.text = petObtain.name
                }
            } catch (e: IOException) {
                println(e)
            }
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