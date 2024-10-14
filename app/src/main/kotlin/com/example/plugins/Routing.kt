package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.ContentType
import com.example.model.*
import io.ktor.serialization.*

fun Application.configureRouting() {
    // install(StatusPages) {
    //     exception<IllegalStateException> { call, cause ->
    //         call.respondText("App in illegal state as ${cause.message}")
    //     }
    // }
    routing {
        staticResources("/static", "static")

        route("/pantry") {
            get {
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                        text = """
                    <h3>TODO:</h3>
                    <ol>
                        <li>A table of all the products</li>
                        <li>A form to submit new product</li>
                    </ol>
                    """.trimIndent()
                )
            }

            route("/products") {
                get {
                    val products = ProductRepository.allProducts()
                    call.respond(products)
                }

                get("/byName/{name}") {
                    val name = call.parameters["name"]
                    if (name == null) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val product = ProductRepository.productByName(name)
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
                        val products = ProductRepository.productsByType(type)

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
                        ProductRepository.addProduct(product)
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

                    if (ProductRepository.removeProduct(name)) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}
