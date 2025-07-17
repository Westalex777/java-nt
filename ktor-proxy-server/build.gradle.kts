plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "3.0.0"
}

application {
    mainClass.set("MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:3.0.0")
    implementation("io.ktor:ktor-server-netty-jvm:3.0.0")
    implementation("io.ktor:ktor-server-call-logging:3.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")
    implementation("io.ktor:ktor-client-core:3.0.0")
    implementation("io.ktor:ktor-client-cio:3.0.0")
}
