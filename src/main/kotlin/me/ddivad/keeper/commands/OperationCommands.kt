package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.embeds.buildStatsEmbed
import me.jakejmattson.discordkt.commands.commands

@Suppress("unused")
fun operationCommands(configuration: Configuration, statsService: StatisticsService) = commands("Operation", Permissions.STAFF) {
    slash("enable") {
        description = "Enable the bot reactions"
        execute {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration[guild.id]?.enabled = true
            configuration.save()
            respond("Bot enabled", false)
        }
    }

    slash("disable") {
        description = "Disabled the bot reactions"
        execute {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration[guild.id]?.enabled = false
            configuration.save()
            respond("Bot disabled", false)
        }
    }

    slash("stats") {
        description = "View statistics about Keeper"
        execute {
            respond(false) {
                buildStatsEmbed(guild, configuration, statsService)
            }
        }
    }
}