package org.abondar.experimental.plugins

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

fun Application.configureMetrics() {

    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(MicrometerMetrics) {
        this.registry = registry
    }

    routing {
        routing {
            get("/metrics") {
                call.respondText(registry.scrape())
            }

            get("/health") {
                call.respondText("""{"status":"UP"}""", ContentType.Application.Json)
            }
        }

    }
}