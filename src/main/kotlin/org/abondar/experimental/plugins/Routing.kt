package org.abondar.experimental.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.abondar.experimental.generator.SnowflakeGenerator
import org.abondar.experimental.model.SnowflakeResponse


fun Application.configureApiRouting() {
    val machineId = environment.config.property("snowflake.machineId").getString()

    val generator = SnowflakeGenerator(machineId.toLong())

    routing {

        /**
         * Generate snowflake id
         *
         *
         * Responses:
         * - 200: short url [SnowflakeResponse]
         */
        get("/snowflake") {
            val id = generator.generateId()
            call.respond(SnowflakeResponse(id))
        }
    }
}
