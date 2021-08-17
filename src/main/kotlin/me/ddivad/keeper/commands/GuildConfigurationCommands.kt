package me.ddivad.keeper.commands

import me.ddivad.keeper.conversations.ConfigurationConversation
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.commands.commands

@Suppress("unused")
fun guildConfigurationCommands(configuration: Configuration) = commands("Configuration", Permissions.STAFF) {
    guildCommand("configure") {
        description = "Configure a guild to use Keeper."
        execute {
            if (configuration.hasGuildConfig(guild.id.value)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }
            ConfigurationConversation(configuration)
                    .createConfigurationConversation(guild)
                    .startPublicly(discord, author, channel)
            respond("Guild setup")
        }
    }

    guildCommand("setPrefix") {
        description = "Set the prefix required for the bot to register a command."
        execute(AnyArg("Prefix")) {
            if (!configuration.hasGuildConfig(guild.id.value)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }

            val prefix = args.first
            configuration[guild.id.value]?.prefix = prefix
            configuration.save()
            respond("Prefix set to: $prefix")
        }
    }

    guildCommand("setRole") {
        description = "Set the role required to use this bot."
        execute(RoleArg) {
            if (!configuration.hasGuildConfig(guild.id.value)) {
                respond("Guild configuration exists. To modify it use the commands to set values.")
                return@execute
            }

            val requiredRole = args.first
            configuration[guild.id.value]?.requiredRoleId = requiredRole.id.value
            configuration.save()
            respond("Required role set to: ${requiredRole.name}")
        }


        guildCommand("setReaction") {
            description = "Set the reaction used to save messages"
            execute(UnicodeEmojiArg) {
                if (!configuration.hasGuildConfig(guild.id.value)) {
                    respond("Guild configuration exists. To modify it use the commands to set values.")
                    return@execute
                }

                val reaction = args.first
                configuration[guild.id.value]?.bookmarkReaction = reaction.unicode
                configuration.save()
                respond("Reaction set to: $reaction")
            }
        }
    }
}