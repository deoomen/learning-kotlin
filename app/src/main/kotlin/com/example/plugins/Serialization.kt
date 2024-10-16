package com.example.plugins

import com.example.model.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(repository: ProductRepository) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/pantry") {
            route("/products") {
                get {
                    val products = repository.allProducts()
                    call.respond(products)
                }

                get("/byName/{name}") {
                    val name = call.parameters["name"]
                    if (name == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val product = repository.productByName(name)
                    if (product == null) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(product)
                }

                get("/byType/{type}") {
                    val typeAsText = call.parameters["type"]

                    if (typeAsText == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    try {
                        val type = Type.valueOf(typeAsText)
                        val products = repository.productsByType(type)

                        if (products.isEmpty()) {
                            call.respond(HttpStatusCode.NotFound)
                            return@get
                        }

                        call.respond(products)
                    } catch (ex: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                post {
                    try {
                        val product = call.receive<Product>()
                        repository.addProduct(product)
                        call.respond(HttpStatusCode.NoContent)
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: JsonConvertException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                delete("/{name}") {
                    val name = call.parameters["name"]

                    if (name == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@delete
                    }

                    if (repository.removeProduct(name)) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}
