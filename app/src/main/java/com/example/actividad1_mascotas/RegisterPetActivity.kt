package com.example.actividad1_mascotas

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.actividad1_mascotas.models.Pet
import com.example.actividad1_mascotas.models.TypeClassification
import com.example.actividad1_mascotas.models.TypeRefugeStatus
import com.example.actividad1_mascotas.models.TypeSex
import com.example.actividad1_mascotas.models.TypeSpecies
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Build
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class RegisterPetActivity : AppCompatActivity() {
    private val PICK_IMAGE = 1
    private val CAPTURE_IMAGE = 2
    private var imageUri: Uri? = null
    private var imageBit: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pet)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainConstraintLayout)
        imageBit = BitmapFactory.decodeResource(resources, R.drawable.image_6)


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

        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, ListPetsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnLoadImage = findViewById<Button>(R.id.btnLoadImage)
        btnLoadImage.setOnClickListener {
            val items = arrayOf("Seleccionar de la galería", "Tomar una foto")
            val dialog = AlertDialog.Builder(this)
                .setTitle("Selecciona una opción")
                .setItems(items) { _, which ->
                    when (which) {
                        0 -> openGallery()
                        1 -> captureImage()
                    }
                }
                .create()
            dialog.show()
        }

        val spinnerSpecies = findViewById<Spinner>(R.id.spinnerSpecies)
        val spinnerSex = findViewById<Spinner>(R.id.spinnerSex)
        val spinnerClassification = findViewById<Spinner>(R.id.spinnerClassification)
        val spinnerShelterState = findViewById<Spinner>(R.id.spinnerShelterState)

        val speciesAdapter = ArrayAdapter<TypeSpecies>(
            this,
            android.R.layout.simple_spinner_item,
            TypeSpecies.values()
        )
        val sexAdapter =
            ArrayAdapter<TypeSex>(this, android.R.layout.simple_spinner_item, TypeSex.values())
        val classificationAdapter = ArrayAdapter<TypeClassification>(
            this,
            android.R.layout.simple_spinner_item,
            TypeClassification.values()
        )
        val refugeStatusAdapter = ArrayAdapter<TypeRefugeStatus>(
            this,
            android.R.layout.simple_spinner_item,
            TypeRefugeStatus.values()
        )

        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        refugeStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSpecies.adapter = speciesAdapter
        spinnerSex.adapter = sexAdapter
        spinnerClassification.adapter = classificationAdapter
        spinnerShelterState.adapter = refugeStatusAdapter

        val etAdmissionDate = findViewById<EditText>(R.id.etAdmissionDate)
        etAdmissionDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate =
                    "$dayOfMonth/${month + 1}/$year" // Formatear la fecha como desees

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val selectedDateDate = sdf.parse(selectedDate)
                val currentDate = Date() // Obtener la fecha actual

                if (selectedDateDate != null && !selectedDateDate.after(currentDate)) {
                    // La fecha seleccionada es válida (anterior o igual a la fecha actual)
                    etAdmissionDate.setText(selectedDate)
                } else {
                    // La fecha seleccionada es futura, muestra un mensaje de error
                    Toast.makeText(this, "Selecciona una fecha válida", Toast.LENGTH_SHORT).show()
                }
            }, year, month, day)

            datePickerDialog.datePicker.maxDate =
                System.currentTimeMillis() // Limitar a la fecha actual como fecha máxima
            datePickerDialog.show()
        }

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            registerPet()
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAPTURE_IMAGE)
    }

    private fun registerPet() {
        val etName = findViewById<EditText>(R.id.etName)
        val etBreed = findViewById<EditText>(R.id.etBreed)
        val spinnerSex = findViewById<Spinner>(R.id.spinnerSex)
        val spinnerClassification = findViewById<Spinner>(R.id.spinnerClassification)
        val spinnerSpecies = findViewById<Spinner>(R.id.spinnerSpecies)
        val spinnerShelterState = findViewById<Spinner>(R.id.spinnerShelterState)
        val etAdmissionDate = findViewById<EditText>(R.id.etAdmissionDate)
        val checkboxAdoptionStatus = findViewById<CheckBox>(R.id.checkboxAdoptionStatus)
        val checkboxPublicationStatus = findViewById<CheckBox>(R.id.checkboxPublicationStatus)
        val etObservations = findViewById<EditText>(R.id.etObservations)
        val ivPet = findViewById<ImageView>(R.id.imgPet)
        val imageBitmap = (ivPet.drawable as BitmapDrawable).bitmap

        if (etName.text.isEmpty() || etBreed.text.isEmpty() || etObservations.text.isEmpty() || etAdmissionDate.text.isEmpty() || spinnerSex.selectedItem == null ||
            spinnerClassification.selectedItem == null || spinnerSpecies.selectedItem == null || spinnerShelterState.selectedItem == null || imageBitmap == null
        ) {
            Toast.makeText(
                this,
                "Por favor complete todos los campos requeridos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Todos los campos requeridos están completos, continuar con el registro

            // Obtener los valores de los elementos
            val name = etName.text.toString()
            val breed = etBreed.text.toString()
            val sex = spinnerSex.selectedItem.toString()
            val classification = spinnerClassification.selectedItem.toString()
            val species = spinnerSpecies.selectedItem.toString()
            val shelterState = spinnerShelterState.selectedItem.toString()
            val isAdoptionStatus = checkboxAdoptionStatus.isChecked
            val isPublicationStatus = checkboxPublicationStatus.isChecked
            val observations = etObservations.text.toString()

            val admissionDate = etAdmissionDate.text.toString()
            val dateFormat = "dd/MM/yyyy"

            val date = stringToDate(admissionDate, dateFormat)
            if (date != null) {
                try {
                    val fileName = "pets.json"
                    val fileRead = File(filesDir, fileName)
                    if (fileRead.exists()) {
                        val json = fileRead.readText()
                        val jsonArray = JSONArray(json)

                        val petList = mutableListOf<Pet>()
                        for (i in 0 until jsonArray.length()) {
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

                        val nextId = petList.size + 1

                        val imageName = "image_" + nextId.toString()
                        val imageBitToSave = imageBit
                        if (imageBitToSave != null) {
                            saveImageToInternalStorage(imageBitToSave, imageName)
                            val newPet = Pet(
                                nextId,
                                TypeSpecies.valueOf(species),
                                name,
                                breed,
                                TypeSex.valueOf(sex),
                                TypeClassification.valueOf(classification),
                                date,
                                observations,
                                TypeRefugeStatus.valueOf(shelterState),
                                isAdoptionStatus,
                                imageBitmap,
                                isPublicationStatus
                            )
                            // Agregar la nueva mascota a la lista
                            petList.add(newPet)

                            // Convertir la lista de mascotas a JSON
                            val newJsonArray = JSONArray()
                            for (pet in petList) {
                                val jsonObject = JSONObject()
                                jsonObject.put("id", pet.id)
                                jsonObject.put("species", pet.species.name)
                                jsonObject.put("name", pet.name)
                                jsonObject.put("breed", pet.breed)
                                jsonObject.put("sex", pet.sex.name)
                                jsonObject.put("classification", pet.classification.name)
                                jsonObject.put(
                                    "adoption_date",
                                    dateToString(pet.adoption_date, dateFormat)
                                )
                                jsonObject.put("observation", pet.observation)
                                jsonObject.put("refuge_status", pet.refuge_status.name)
                                jsonObject.put("adoption_status", pet.adoption_status)
                                jsonObject.put(
                                    "image", "image_" + pet.id
                                )
                                jsonObject.put("publication_status", pet.publication_status)
                                newJsonArray.put(jsonObject)
                            }

                            val file = File(filesDir, "pets.json")
                            val newJsonString = newJsonArray.toString()
                            file.writeText(newJsonString)

                            Toast.makeText(
                                this,
                                "Mascota registrada exitosamente.",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            val intent = Intent(this, ListPetsActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "La imagen es nula", Toast.LENGTH_SHORT).show()

                        }


                    }

                } catch (e: IOException) {
                    println(e)
                }

            } else {
                Toast.makeText(
                    this,
                    "La fecha ingresada no es válida. El formato debe ser $dateFormat.",
                    Toast.LENGTH_SHORT
                ).show()
                etAdmissionDate.setText("")
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

    fun dateToString(date: Date, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imgPet = findViewById<ImageView>(R.id.imgPet)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    imageUri = data?.data
                    if (imageUri != null) {
                        try {
                            val imageBitmap =
                                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                            imgPet.setImageBitmap(imageBitmap)
                            imageBit = imageBitmap
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                CAPTURE_IMAGE -> {
                    if (data != null && data.extras != null) {
                        val imageBitmap = data.extras!!.get("data") as Bitmap
                        imgPet.setImageBitmap(imageBitmap)
                        imageBit = imageBitmap
                    }
                }
            }
        }

    }

    private fun saveImageToInternalStorage(imageBitmap: Bitmap, imageName: String) {
        val imageFile = File(filesDir, imageName + ".png")
        val outputStream = FileOutputStream(imageFile)

        try {
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            // Manejar errores si es necesario
        } finally {
            outputStream.close()
        }
    }

}
