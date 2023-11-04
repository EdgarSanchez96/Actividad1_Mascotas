package com.example.actividad1_mascotas

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.example.actividad1_mascotas.models.TypeClassification
import com.example.actividad1_mascotas.models.TypeRefugeStatus
import com.example.actividad1_mascotas.models.TypeSex
import com.example.actividad1_mascotas.models.TypeSpecies
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterPetActivity : AppCompatActivity() {
    private val PICK_IMAGE = 1
    private val CAPTURE_IMAGE = 2
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pet)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la actividad anterior
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

        val speciesAdapter = ArrayAdapter<TypeSpecies>(this, android.R.layout.simple_spinner_item, TypeSpecies.values())
        val sexAdapter = ArrayAdapter<TypeSex>(this, android.R.layout.simple_spinner_item, TypeSex.values())
        val classificationAdapter = ArrayAdapter<TypeClassification>(this, android.R.layout.simple_spinner_item, TypeClassification.values())
        val refugeStatusAdapter = ArrayAdapter<TypeRefugeStatus>(this, android.R.layout.simple_spinner_item, TypeRefugeStatus.values())

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
                val selectedDate = "$dayOfMonth/${month + 1}/$year" // Formatear la fecha como desees

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

            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Limitar a la fecha actual como fecha máxima
            datePickerDialog.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imgPet = findViewById<ImageView>(R.id.imgPet)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    imageUri = data?.data
                    imgPet.setImageURI(imageUri)
                }
                CAPTURE_IMAGE -> {
                    // Asegúrate de que `data` no sea nulo antes de acceder a `data?.data`
                    if (data != null && data.extras != null) {
                        val imageBitmap = data.extras!!.get("data") as Bitmap
                        // Aquí puedes mostrar la imagen capturada en el ImageView
                        imgPet.setImageBitmap(imageBitmap)
                    }
                }
            }
        }
    }
}