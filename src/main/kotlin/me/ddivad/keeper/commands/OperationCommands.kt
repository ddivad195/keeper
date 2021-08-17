package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.embeds.buildStatsEmbed
import me.jakejmattson.discordkt.api.commands.commands

@Suppress("unused")
fun operationCommands(configuration: Configuration, statsService: StatisticsService) = commands("Operation", Permissions.STAFF) {
    guildCommand("enable") {
        description = "Enable the bot reactions"
        execute {
            if (!configuration.hasGuildConfig(guild.id.value)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration[guild.id.value]?.enabled = true
            configuration.save()
            respond("Bot enabled")
        }
    }

    guildCommand("disable") {
        description = "Disabled the bot reactions"
        execute {
            if (!configuration.hasGuildConfig(guild.id.value)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration[guild.id.value]?.enabled = false
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