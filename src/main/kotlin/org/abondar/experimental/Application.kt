package org.abondar.experimental

import io.ktor.server.application.*
import org.abondar.experimental.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureApp()
    configureRouting()
    configureCors()
    configureSwagger()

}
