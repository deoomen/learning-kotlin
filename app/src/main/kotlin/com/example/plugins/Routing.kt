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

fun Application.configureRouting() {
    // install(StatusPages) {
    //     exception<IllegalStateException> { call, cause ->
    //         call.respondText("App in illegal state as ${cause.message}")
    //     }
    // }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")

        get("/error-test") {
            throw IllegalStateException("Too Busy")
        }

        // ---
        staticResources("/pantry/product-ui", "product-ui")

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
                    call.respondText(
                        contentType = ContentType.parse("text/html"),
                        text = products.productsAsTable()
                    )
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

                        call.respondText(
                            contentType = ContentType.parse("text/html"),
                            text = products.productsAsTable(),
                        )
                    } catch (ex: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                post {
                    val formContent = call.receiveParameters()

                    val params = Triple(
                        formContent["name"] ?: "",
                        formContent["quantity"] ?: "",
                        formContent["type"] ?: "",
                    )

                    if (params.toList().any { it.isEmpty() }) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }

                    try {
                        val type = Type.valueOf(params.third)
                        ProductRepository.addProduct(
                            Product(
                                params.first,
                                params.second.toInt(),
                                type,
                            )
                        )

                        call.respond(HttpStatusCode.NoContent)
                    } catch (ex: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest)
                    } catch (ex: IllegalStateException) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}
