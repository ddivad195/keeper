package me.ddivad.keeper.commands

import me.ddivad.keeper.conversations.ConfigurationConversation
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.jakejmattson.discordkt.arguments.*
import me.jakejmattson.discordkt.commands.commands
import java.lang.Compiler.command

@Suppress("unused")
fun guildConfigurationCommands(configuration: Configuration) = commands("Configuration", Permissions.STAFF) {
    command("configure") {
        description = "Configure a guild to use Keeper."
        execute {
            if (configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            ConfigurationConversation(configuration)
                    .createConfigurationConversation(guild)
                    .startPublicly(discord, author, channel)
        }
    }

    slash("setPrefix") {
        description = "Set the prefix required for the bot to register a command."
        execute(AnyArg("Prefix")) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }

            val prefix = args.first
            configuration[guild.id]?.prefix = prefix
            configuration.save()
            respondPublic("Prefix set to: $prefix")
        }
    }

    slash("setRole") {
        description = "Set the role required to use this bot."
        execute(RoleArg) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respondPublic("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }

            val requiredRole = args.first
            configuration[guild.id]?.requiredRoleId = requiredRole.id
            configuration.save()
            respondPublic("Required role set to: ${requiredRole.name}")
        }

        slash("setReaction") {
            description = "Set the reaction used to save messages"
            execute(UnicodeEmojiArg) {
                if (!configuration.hasGuildConfig(guild.id)) {
                    respond("Guild configuration exists. To modify it use the commands to set values.")
                    return@execute
                }

                val reaction = args.first
                configuration[guild.id]?.bookmarkReaction = reaction.unicode
                configuration.save()
                respondPublic("Reaction set to: $reaction")
            }
        }
    }
}