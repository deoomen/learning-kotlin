package com.example.model

enum class Type {
    Vegetable, Meat, Fruit, Dairy, Other
}

data class Product(
    val name: String,
    val quantity: Int,
    val type: Type
)

fun Product.productAsRow() = """
<tr>
    <td>$name</td><td>$quantity</td><td>$type</td>
</tr>
""".trimIndent()

fun List<Product>.productsAsTable() = this.joinToString(
    prefix = "<table rules=\"all\">",
    postfix = "</table>",
    separator = "\n",
    transform = Product::productAsRow,
)
