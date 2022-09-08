package me.ddivad.keeper

import dev.kord.common.annotation.KordPreview
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.dsl.bot
import mu.KotlinLogging
import java.awt.Color

val logger = KotlinLogging.logger {  }

@KordPreview
@PrivilegedIntent
fun main() {
    val token = System.getenv("BOT_TOKEN") ?: null
    require(token != null) { "Expected the bot token as an environment variable" }

    bot(token) {
       data("config/config.json") { Configuration() }

        prefix { "/" }

        configure {
            theme = Color(0x00BFFF)
            recommendCommands = false
            intents = Intents(
                Intent.GuildMessageReactions,
                Intent.DirectMessagesReactions
            )
        }

        onStart {
            logger.info { "Bot Ready" }
        }
    }
}
