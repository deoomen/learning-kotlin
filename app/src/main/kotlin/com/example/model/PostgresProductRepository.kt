package com.example.model

import com.example.db.ProductDAO
import com.example.db.ProductTable
import com.example.db.daoToModel
import com.example.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresProductRepository : ProductRepository {
    override suspend fun allProducts(): List<Product> = suspendTransaction {
        ProductDAO.all().map(::daoToModel)
    }

    override suspend fun productsByType(type: Type): List<Product> = suspendTransaction {
        ProductDAO
            .find { (ProductTable.type eq type.toString()) }
            .map(::daoToModel)
    }

    override suspend fun productByName(name: String): Product? = suspendTransaction {
        ProductDAO
            .find { (ProductTable.name eq name) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addProduct(product: Product): Unit = suspendTransaction {
        ProductDAO.new {
            name = product.name
            quantity = product.quantity
            type = product.type.toString()
        }
    }

    override suspend fun removeProduct(name: String): Boolean = suspendTransaction {
        val rowsDeleted = ProductTable.deleteWhere {
            ProductTable.name eq name
        }
        rowsDeleted == 1
    }
}
