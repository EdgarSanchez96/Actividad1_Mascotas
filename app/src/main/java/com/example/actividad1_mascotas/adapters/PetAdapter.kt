package com.example.actividad1_mascotas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1_mascotas.R
import com.example.actividad1_mascotas.models.Pet
import com.example.actividad1_mascotas.viewHolders.PetViewHolder

class PetAdapter(private val pets: List<Pet>): RecyclerView.Adapter<PetViewHolder>() {

    //Interfaz para manejar los clics en los elementos del RecyclerView
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    //Almacena la instancia del oyente de clicks
    private var onItemClickListener: OnItemClickListener? = null

    //Método para establecer el oyente de clicks
    fun setOnRecyclerViewClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    //Se crea un view en base al card_layout creado para cada item y se retorna un PetViewHolder con dicho view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return PetViewHolder(view)
    }

    //Se procede a enviar cada uno de los items de la lista pets al método render de PetViewHolder
    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.render(pets[position])

        //Configura la escucha de clicks en el elemento del RecyclerView
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    //Se retorna la cantidad de items de la lista pets
    override fun getItemCount(): Int {
        return pets.size
    }

}