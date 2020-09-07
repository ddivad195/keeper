package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.*
import me.jakejmattson.discordkt.api.dsl.command.commands

@CommandSet("GuildConfiguration")
fun guildConfigurationCommands(configuration: Configuration) = commands {
    command("SetPrefix") {
        description = "Set the prefix required for the bot to register a command."
        execute(AnyArg("Prefix")) {
            val prefix = it.args.first

            configuration[it.guild!!.idLong]?.prefix = prefix
            configuration.save()

            it.respond("Prefix set to: $prefix")
        }
    }

    command("SetRole") {
        description = "Set the role required to use this bot."
        execute(RoleArg) {
            val requiredRole = it.args.first

            configuration[it.guild!!.idLong]?.requiredRoleId = requiredRole.idLong
            configuration.save()

            it.respond("Required role set to: ${requiredRole.name}")
        }
    }
}