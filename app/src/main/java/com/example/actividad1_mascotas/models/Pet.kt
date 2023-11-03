package com.example.actividad1_mascotas.models

//Se define cada uno de los atributos del objeto Pet(Mascota)
data class Pet (
    val species: String,
    val name: String,
    val breed: String,
    val sex: String,
    val classification: String,
    val image: Int
)