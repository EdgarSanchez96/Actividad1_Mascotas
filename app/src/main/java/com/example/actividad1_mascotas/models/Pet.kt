package com.example.actividad1_mascotas.models

import java.util.Date

//Se define cada uno de los atributos del objeto Pet(Mascota)
enum class TypeSpecies {
    CANINO, FELINO
}

enum class TypeSex {
    MACHO, HEMBRA
}

enum class TypeClassification {
    CACHORRO, ADULTO
}

enum class TypeRefugeStatus {
    FIJO, TEMPORAL
}

data class Pet(
    val species: TypeSpecies,
    val name: String,
    val breed: String,
    val sex: TypeSex,
    val classification: TypeClassification,
    val adoption_date: Date,
    val observation: String,
    val refuge_status: TypeRefugeStatus,
    val adoption_status: Boolean,
    val image: Int,
    val publication_status: Boolean,
)