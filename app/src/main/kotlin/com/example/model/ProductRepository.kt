package com.example.model

interface ProductRepository {
    suspend fun allProducts(): List<Product>
    suspend fun productsByType(type: Type): List<Product>
    suspend fun productByName(name: String): Product?
    suspend fun addProduct(product: Product)
    suspend fun removeProduct(name: String): Boolean
}
