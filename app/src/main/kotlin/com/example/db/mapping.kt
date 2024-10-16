package com.example.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import com.example.model.Type
import com.example.model.Product
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ProductTable : IntIdTable("product") {
    val name = varchar("name", 50)
    val quantity = integer("quantity")
    val type = varchar("priority", 50)
}

class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var name by ProductTable.name
    var quantity by ProductTable.quantity
    var type by ProductTable.type
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: ProductDAO) = Product(
    dao.name,
    dao.quantity,
    Type.valueOf(dao.type)
)
