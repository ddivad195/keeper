package me.ddivad.keeper.commands

import me.ddivad.keeper.conversations.ConfigurationConversation
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.jakejmattson.discordkt.arguments.*
import me.jakejmattson.discordkt.commands.commands

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
                respond("Guild configuration exists. To modify it use the commands to set values.", false)
                return@execute
            }

            val prefix = args.first
            configuration[guild.id]?.prefix = prefix
            configuration.save()
            respond("Prefix set to: $prefix", false)
        }
    }

    slash("setRole") {
        description = "Set the role required to use this bot."
        execute(RoleArg) {
            if (!configuration.hasGuildConfig(guild.id)) {
                respond("Guild configuration exists. To modify it use the commands to set values.", false)
                return@execute
            }

            val requiredRole = args.first
            configuration[guild.id]?.requiredRoleId = requiredRole.id
            configuration.save()
            respond("Required role set to: ${requiredRole.name}", false)
        }


        slash("setReaction") {
            description = "Set the reaction used to save messages"
            execute(UnicodeEmojiArg) {
                if (!configuration.hasGuildConfig(guild.id)) {
                    respond("Guild configuration exists. To modify it use the commands to set values.", false)
                    return@execute
                }

                val reaction = args.first
                configuration[guild.id]?.bookmarkReaction = reaction.unicode
                configuration.save()
                respond("Reaction set to: $reaction", false)
            }
        }
    }
}