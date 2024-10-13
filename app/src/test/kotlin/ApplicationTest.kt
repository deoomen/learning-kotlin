package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals


class ApplicationTest {
    @Test
    fun productsCanBeFoundByType() = testApplication {
        application {
            module()
        }

        val response = client.get("/pantry/products/byType/Vegetable")
        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(body, "broccoli")
        assertContains(body, "carrot")
    }

    @Test
    fun invalidTypeProduces400() = testApplication {
        application {
            module()
        }

        val response = client.get("/pantry/products/byType/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedTypeProduces404() = testApplication {
        application {
            module()
        }

        val response = client.get("/pantry/products/byType/Dairy")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newProductCanBeAdded() = testApplication {
        application {
            module()
        }

        val response1 = client.post("/pantry/products") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(
                listOf(
                    "name" to "Milk",
                    "quantity" to "1",
                    "type" to "Dairy"
                ).formUrlEncode()
            )
        }

        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/pantry/products")
        assertEquals(HttpStatusCode.OK, response2.status)
        val body = response2.bodyAsText()

        assertContains(body, "Milk")
        assertContains(body, "Dairy")
    }
}
