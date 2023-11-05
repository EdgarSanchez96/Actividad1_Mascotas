package com.example.actividad1_mascotas

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.actividad1_mascotas.models.Pet
import java.text.SimpleDateFormat
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
            val backgroundColor = (constraintLayout.background as? ColorDrawable)?.color
            if (backgroundColor != null) {
                window.statusBarColor = backgroundColor
                window.navigationBarColor = backgroundColor
            }
        }

        val data: Bundle? = intent.extras
        if (data != null) {
            var id: Int = data.getInt("id") - 1
            val generalFunctions = GeneralFunctions(filesDir)
            val petList = generalFunctions.getListPets()

            // Se recupera el registro que se presentara en detalle
            var petObtain: Pet = petList.get(id)

            // Se modifica el formato de presentación del campo fecha admisión dd/MM/yyyy
            val dateFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            var date =  petObtain.adoption_date

            // Agregar datos del json a la vista
            imgPet.setImageBitmap(petObtain.image)
            txtName.text = petObtain.name
            txtSpecie.text = petObtain.species.toString()
            txtBreed.text = petObtain.breed
            txtSex.text = petObtain.sex.toString()
            txtClassification.text = petObtain.classification.toString()
            txtAdoptionDate.text = sdf.format(date)
            txtObservation.text = petObtain.observation.toString()
            txtRefugeStatus.text = petObtain.refuge_status.toString()
            var adopStatus: Boolean = petObtain.adoption_status
            txtAdoptionStatus.text = if (adopStatus) "SI" else "NO"
            var publicStatus = petObtain.publication_status
            txtPublicationStatus.text = if (publicStatus) "SI" else "NO"

        }


        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, ListPetsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
