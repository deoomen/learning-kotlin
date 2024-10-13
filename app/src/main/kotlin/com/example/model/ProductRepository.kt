package com.example.model

object ProductRepository {
    private val products = mutableListOf(
        Product("broccoli", 1, Type.Vegetable),
        Product("carrot", 4, Type.Vegetable),
        Product("chicken breast", 2, Type.Meat),
        Product("apple", 5, Type.Fruit),
        Product("pepper", 1, Type.Other),
    )

    fun allProducts(): List<Product> = products

    fun productsByType(type: Type) = products.filter {
        it.type == type
    }

    fun productByName(name: String) = products.find {
        it.name.equals(name, ignoreCase = true)
    }

    fun addProduct(product: Product) {
        if (productByName(product.name) != null) {
            throw IllegalStateException("Cannot duplicate products!")
        }
        products.add(product)
    }
}
