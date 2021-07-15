package me.ddivad.keeper.services

import dev.kord.core.supplier.EntitySupplyStrategy
import kotlinx.coroutines.flow.toList
import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.api.Discord
import me.jakejmattson.discordkt.api.annotations.Service
import me.jakejmattson.discordkt.api.extensions.toSnowflake

@Service
class CacheService(private val discord: Discord, private val configuration: Configuration) {
    suspend fun run() {
        configuration.guildConfigurations.forEach { config ->
            try {
                val guild = config.key.toSnowflake().let { discord.kord.getGuild(it) } ?: return@forEach
                val roles = guild.withStrategy(EntitySupplyStrategy.cachingRest).roles.toList()
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }
}