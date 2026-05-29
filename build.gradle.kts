import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val logbackVersion: String by project
val junitVersion: String by project

plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
    id("io.ktor.plugin") version "3.5.0"
    id("com.google.cloud.tools.jib") version "3.4.3"
}

group = "org.abondar.experimental"
version = "1.1.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-routing-openapi")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-jackson")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-config-yaml")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

    testImplementation("io.ktor:ktor-server-test-host")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

ktor {
    openApi {
        enabled = true
        codeInferenceEnabled = true
        onlyCommented = false
    }
}

tasks.test {
    useJUnitPlatform()
}


var registry = System.getenv("DOCKER_REGISTRY")

jib {
    from {
        image = "eclipse-temurin:21-jre"
        platforms {
//            platform {
//                architecture = "amd64"
//                os = "linux"
//            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }

    }
    to {
        image = "$registry/snowflakegenerator:$version"
        auth {
            username = System.getenv("DOCKER_USERNAME")
            password = System.getenv("DOCKER_PWD")
        }
    }

    container {
        mainClass = mainClass
        ports = listOf("8080")
    }
}

