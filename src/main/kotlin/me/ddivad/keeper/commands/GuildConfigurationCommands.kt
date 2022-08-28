package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.jakejmattson.discordkt.arguments.*
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.dsl.edit

@Suppress("unused")
fun guildConfigurationCommands(configuration: Configuration) = commands("Configuration", Permissions.STAFF) {
    slash("configure", "Configure a guild to use Keeper.") {
        execute(UnicodeEmojiArg("Reaction", "The reaction that will be used to bookmark messages")) {
            if (configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration already exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration.setup(guild, args.first.unicode)
            respond("Guild Setup")

        }
    }

    slash("setReaction", "Set the reaction used to save messages") {
        execute(UnicodeEmojiArg("Reaction", "The reaction that will be used to bookmark messages")) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration does not exist. Run `/configure` first.")
                return@execute
            }

            val reaction = args.first
            configuration.edit { guildConfigurations[guild.id]?.bookmarkReaction = reaction.unicode }
            respondPublic("Reaction set to: $reaction")
        }
    }

    slash("enable", "Enable the bot reactions") {
        execute {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration does not exist. Run `/configure` first.")
                return@execute
            }
            configuration.edit { guildConfigurations[guild.id]?.enabled = true }
            respondPublic("Reactions enabled")
        }
    }

    slash("disable", "Disable the bot reactions") {
        execute {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration does not exist. Run `/configure` first.")
                return@execute
            }
            configuration.edit { guildConfigurations[guild.id]?.enabled = false }
            respondPublic("Reactions disabled")
        }
    }
}
