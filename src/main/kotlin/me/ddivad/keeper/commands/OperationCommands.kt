package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.embeds.buildStatsEmbed
import me.jakejmattson.discordkt.api.dsl.commands

fun operationCommands(configuration: Configuration, statsService: StatisticsService) = commands("Operation") {
    guildCommand("enable") {
        description = "Enable the bot's functionality"
        execute {
            if (!configuration.hasGuildConfig(guild.id.longValue)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration[guild.id.longValue]?.enabled = true
            configuration.save()
            respond("Bot enabled")
        }
    }

    guildCommand("disable") {
        description = "Enable the bot's functionality"
        execute {
            if (!configuration.hasGuildConfig(guild.id.longValue)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration[guild.id.longValue]?.enabled = false
            configuration.save()
            respond("Bot disabled")
        }
    }

    guildCommand("stats") {
        description = "View statistics about Keeper"
        execute {
            respond {
                buildStatsEmbed(guild, configuration, statsService)
            }
        }
    }
}