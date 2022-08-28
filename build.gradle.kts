import java.util.*

group = "me.ddivad"
version = "1.9.2"
description = "A bot for saving useful messages to a DM by reacting to them."

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("me.jakejmattson:DiscordKt:0.23.4")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"

        Properties().apply {
            setProperty("name", "Keeper")
            setProperty("description", project.description)
            setProperty("version", version.toString())
            setProperty("url", "https://github.com/ddivad195/keeper")

            store(file("src/main/resources/bot.properties").outputStream(), null)
        }
    }

    shadowJar {
        archiveFileName.set("Keeper.jar")
        manifest {
            attributes(
                "Main-Class" to "me.ddivad.keeper.MainKt"
            )
        }
    }
}