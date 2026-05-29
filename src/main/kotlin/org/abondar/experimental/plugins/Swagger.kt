package org.abondar.experimental.plugins

import io.ktor.openapi.OpenApiInfo
import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "/swaggerUI") {
            info = OpenApiInfo("Generator API", "1.0.0", "Snowflake ID generator")
        }
    }
}