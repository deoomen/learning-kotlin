package com.example.model

class FakeProductRepository : ProductRepository {
    private val products = mutableListOf(
        Product("broccoli", 1, Type.Vegetable),
        Product("carrot", 4, Type.Vegetable),
        Product("chicken breast", 2, Type.Meat),
        Product("apple", 5, Type.Fruit),
        Product("pepper", 1, Type.Other),
    )

    override suspend fun allProducts(): List<Product> = products

    override suspend fun productsByType(type: Type) = products.filter {
        it.type == type
    }

    override suspend fun productByName(name: String) = products.find {
        it.name.equals(name, ignoreCase = true)
    }

    override suspend fun addProduct(product: Product) {
        if (productByName(product.name) != null) {
            throw IllegalStateException("Cannot duplicate products!")
        }

        products.add(product)
    }

    override suspend fun removeProduct(name: String): Boolean {
        return products.removeIf { it.name == name }
    }
}
