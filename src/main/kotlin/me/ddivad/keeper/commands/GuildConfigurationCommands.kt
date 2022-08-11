package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.jakejmattson.discordkt.arguments.*
import me.jakejmattson.discordkt.commands.commands

@Suppress("unused")
fun guildConfigurationCommands(configuration: Configuration) = commands("Configuration", Permissions.STAFF) {
    slash("configure") {
        description = "Configure a guild to use Keeper."
        execute(UnicodeEmojiArg) {
            if (configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration already exists. To modify it use the commands to set values.")
                return@execute
            }
            configuration.setup(guild, args.first.unicode)
            respond("Guild Setup")

        }
    }

    slash("setReaction") {
        description = "Set the reaction used to save messages"
        execute(UnicodeEmojiArg) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration does not exist. Run `/configure` first.")
                return@execute
            }

            val reaction = args.first
            configuration[guild.id]?.bookmarkReaction = reaction.unicode
            configuration.save()
            respondPublic("Reaction set to: $reaction")
        }
    }

    slash("enable") {
        description = "Enable the bot reactions"
        execute {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration does not exist. Run `/configure` first.")
                return@execute
            }
            configuration[guild.id]?.enabled = true
            configuration.save()
            respondPublic("Reactions enabled")
        }
    }

    slash("disable") {
        description = "Disable the bot reactions"
        execute {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration does not exist. Run `/configure` first.")
                return@execute
            }
            configuration[guild.id]?.enabled = false
            configuration.save()
            respondPublic("Reactions disabled")
        }
    }
}
