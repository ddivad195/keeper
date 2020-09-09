package me.ddivad.keeper.commands

import me.ddivad.keeper.conversations.ConfigurationConversation
import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.discordkt.api.services.ConversationService

@CommandSet("Configuration")
fun guildConfigurationCommands(configuration: Configuration, conversationService: ConversationService) = commands {
    command("configure") {
        description = "Configure a guild to use Keeper."
        execute {
            if (configuration.hasGuildConfig(it.guild!!.idLong))
                return@execute it.respond("Guild configuration exists. To modify it use the commands to set values.")

            conversationService.startPublicConversation<ConfigurationConversation>(it.author, it.channel, it.guild!!)
            it.respond("Guild setup")
        }
    }
    
    command("setPrefix") {
        description = "Set the prefix required for the bot to register a command."
        execute(AnyArg("Prefix")) {
            if (!configuration.hasGuildConfig(it.guild!!.idLong))
                return@execute it.respond("Please run the **configure** command to set this initially.")

            val prefix = it.args.first

            configuration[it.guild!!.idLong]?.prefix = prefix
            configuration.save()

            it.respond("Prefix set to: $prefix")
        }
    }

    command("setRole") {
        description = "Set the role required to use this bot."
        execute(RoleArg) {
            if (!configuration.hasGuildConfig(it.guild!!.idLong))
                return@execute it.respond("Please run the **configure** command to set this initially.")

            val requiredRole = it.args.first

            configuration[it.guild!!.idLong]?.requiredRoleId = requiredRole.idLong
            configuration.save()

            it.respond("Required role set to: ${requiredRole.name}")
        }


        command("setReaction") {
            description = "Set the reaction used to save messages"
            execute(UnicodeEmoteArg) {
                if (!configuration.hasGuildConfig(it.guild!!.idLong))
                    return@execute it.respond("Please run the **configure** command to set this initially.")

                val reaction = it.args.first

                configuration[it.guild!!.idLong]?.bookmarkReaction = reaction
                configuration.save()

                it.respond("Reaction set to: $reaction")
            }
        }
    }
}