package org.abondar.experimental

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.*
import org.abondar.experimental.model.SnowflakeResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class ApiTest {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `Call snowflake endpoint`() = testApplication {
        environment {
            config = MapApplicationConfig(
                "snowflake.machineId" to "1"
            )
        }

        application {
            module()
        }

        client.get("/snowflake").apply {
            assertEquals(HttpStatusCode.OK, status)

            val responseBody = bodyAsText()
            assertNotNull(responseBody)

            val response = objectMapper.readValue(responseBody, SnowflakeResponse::class.java)
            assertNotNull(response.snowflakeId)  // Verify that snowflakeId is not null
            assertTrue(response.snowflakeId > 0)
        }
    }
}
