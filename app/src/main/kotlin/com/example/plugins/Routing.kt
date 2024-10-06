package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")

        get("/ingredients") {
            // Return a list of components (simple list to start with)
            call.respond(HttpStatusCode.OK, listOf("broccoli", "carrot", "potato", "tomato", "pepper"))
        }

        get("/similar/{ingredient}") {
            val ingredient = call.parameters["ingredient"] ?: ""
            // Here there will be a call to Qdrant and return similar components
            call.respond(HttpStatusCode.OK, "Search similar to: $ingredient")
        }
    }
}
