package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.utilities.buildStatsEmbed
import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.discordkt.api.services.ConversationService

@CommandSet("Operation")
fun operationCommands(configuration: Configuration, conversationService: ConversationService, statsService: StatisticsService) = commands {
    command("enable") {
        description = "Enable the bot's functionality"
        execute {
            if (!configuration.hasGuildConfig(it.guild!!.idLong))
                return@execute it.respond("Please run the **configure** command to set this initially.")

            configuration[it.guild!!.idLong]?.enabled = true
            configuration.save()

            it.respond("Bot enabled")
        }
    }

    command("disable") {
        description = "Enable the bot's functionality"
        execute {
            if (!configuration.hasGuildConfig(it.guild!!.idLong))
                return@execute it.respond("Please run the **configure** command to set this initially.")

            configuration[it.guild!!.idLong]?.enabled = false
            configuration.save()

            it.respond("Bot disabled")
        }
    }

    command("stats") {
        description = "View statistics about Keeper"
        execute {
            it.respond(buildStatsEmbed(it.guild!!, configuration, statsService))
        }
    }
}