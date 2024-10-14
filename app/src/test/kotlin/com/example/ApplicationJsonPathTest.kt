package com.example

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.ktor.client.*
import com.example.model.Type
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*


class ApplicationJsonPathTest {
    @Test
    fun productsCanBeFound() = testApplication {
        application {
            module()
        }
        val jsonDoc = client.getAsJsonPath("/pantry/products")

        val result: List<String> = jsonDoc.read("$[*].name")
        assertEquals("broccoli", result[0])
        assertEquals("carrot", result[1])
        assertEquals("chicken breast", result[2])
    }

    @Test
    fun productsCanBeFoundByType() = testApplication {
        application {
            module()
        }
        val type = Type.Vegetable
        val jsonDoc = client.getAsJsonPath("/pantry/products/byType/$type")

        val result: List<String> =
            jsonDoc.read("$[?(@.type == '$type')].name")
        assertEquals(2, result.size)

        assertEquals("broccoli", result[0])
        assertEquals("carrot", result[1])
    }

    suspend fun HttpClient.getAsJsonPath(url: String): DocumentContext {
        val response = this.get(url) {
            accept(ContentType.Application.Json)
        }
        return JsonPath.parse(response.bodyAsText())
    }
}
