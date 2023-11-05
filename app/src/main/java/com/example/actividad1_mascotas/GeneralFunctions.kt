package com.example.actividad1_mascotas

import android.graphics.BitmapFactory
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

class GeneralFunctions(private val filesDir: File) {
    fun getListPets(): List<Pet> {
        val petList = mutableListOf<Pet>()

        try {
            val fileName = "pets.json"
            val fileRead = File(filesDir, fileName)

            if (fileRead.exists()) {
                val json = fileRead.readText()
                val jsonArray = JSONArray(json)

                for (i in 0 until jsonArray.length()) {
                    val dateFormat = "dd/MM/yyyy"
                    val jsonObject = jsonArray.getJSONObject(i)
                    val date_pet = stringToDate(jsonObject.getString("adoption_date"), dateFormat)
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
                            classification = TypeClassification.valueOf(jsonObject.getString("classification")),
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
            }
        } catch (e: IOException) {
            println(e)
        }

        return petList
    }

    private fun stringToDate(dateString: String, format: String): Date? {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return try {
            formatter.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }
}
