package me.ddivad.keeper

import dev.kord.common.annotation.KordPreview
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.CacheService
import me.jakejmattson.discordkt.dsl.bot
import java.awt.Color

@KordPreview
@PrivilegedIntent
suspend fun main() {
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
            val cacheService = this.getInjectionObjects(CacheService::class)
            try {
                cacheService.run()
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }
}
