package com.example.actividad1_mascotas.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1_mascotas.R
import com.example.actividad1_mascotas.models.Pet

class PetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // Almacenar en variables cada uno de los componentes de la interfaz a través de su id
    private val txtPetName: TextView = view.findViewById(R.id.petName)
    private val txtPetSpecies: TextView = view.findViewById(R.id.petSpecies)
    private val txtPetBreed: TextView = view.findViewById(R.id.petBreed)
    private val txtPetSex: TextView = view.findViewById(R.id.petSex)
    private val ivPetImage: ImageView = view.findViewById(R.id.petImage)

    // Función que permite renderizar cada uno de los valores en sus respectivos componentes, de acuerdo al objeto recibido
    fun render(pet: Pet) {
        txtPetName.text = pet.name
        txtPetSpecies.text = " - " + pet.species.name
        txtPetBreed.text = pet.breed
        txtPetSex.text = pet.sex.name

        ivPetImage.setImageBitmap(pet.image)
    }
}
