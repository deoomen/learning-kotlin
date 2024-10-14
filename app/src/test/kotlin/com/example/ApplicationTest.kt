package com.example

import com.example.model.Type
import com.example.model.Product
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun productsCanBeFoundByType() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/pantry/products/byType/Vegetable")
        val results = response.body<List<Product>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedProductNames = listOf("broccoli", "carrot")
        val actualProductNames = results.map(Product::name)
        assertContentEquals(expectedProductNames, actualProductNames)
    }

    @Test
    fun invalidTypeProduces400() = testApplication {
        val response = client.get("/pantry/products/byType/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }


    @Test
    fun unusedTypeProduces404() = testApplication {
        val response = client.get("/pantry/products/byType/Dairy")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newProductsCanBeAdded() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val product = Product("salt", 3, Type.Other)
        val response1 = client.post("/pantry/products") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )
            setBody(product)
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/pantry/products")
        assertEquals(HttpStatusCode.OK, response2.status)

        val productNames = response2
            .body<List<Product>>()
            .map { it.name }

        assertContains(productNames, "salt")
    }
}
