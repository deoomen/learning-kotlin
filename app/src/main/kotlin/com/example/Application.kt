package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import com.example.model.PostgresProductRepository

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = PostgresProductRepository()

    configureSerialization(repository)
    configureDatabases()
    configureRouting()
}
