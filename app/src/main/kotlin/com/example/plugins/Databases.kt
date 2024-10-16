package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import io.ktor.server.config.*

fun Application.configureDatabases(config: ApplicationConfig) {
    Database.connect(
        url = config.property("storage.jdbcURL").getString(),
        user = config.property("storage.user").getString(),
        password = config.property("storage.password").getString(),
    )
}
