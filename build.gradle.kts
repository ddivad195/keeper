group = "me.ddivad"
version = Versions.BOT
description = "A bot for saving useful messages to a DM by reacting to them."

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("me.jakejmattson:DiscordKt:${Versions.DISCORDKT}")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
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


object Versions {
    const val BOT = "1.2.0"
    const val DISCORDKT = "0.22.0-SNAPSHOT"
}