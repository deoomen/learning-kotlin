package com.example.model

import kotlinx.serialization.Serializable

enum class Type {
    Vegetable, Meat, Fruit, Dairy, Other
}

@Serializable
data class Product(
    val name: String,
    val quantity: Int,
    val type: Type,
)
