package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.extensions.requiredPermissionLevel
import me.ddivad.keeper.services.Permission
import me.jakejmattson.discordkt.api.annotations.CommandSet
import me.jakejmattson.discordkt.api.arguments.MessageArg
import me.jakejmattson.discordkt.api.dsl.command.commands
import me.jakejmattson.discordkt.api.services.ConversationService

@CommandSet("General")
fun generalCommands(configuration: Configuration) = commands {
    command("delete") {
        description = "Delete a Keeper bookmark by ID inside of DM channel"
        requiresGuild = false
        requiredPermissionLevel = Permission.EVERYONE
        execute(MessageArg) {
            val message = it.args.first

            if (message.author == it.discord.jda.selfUser && it.guild == null) {
                message.delete().queue()
            } else {
                it.unsafeRespond("Can only delete messages sent by ${it.discord.jda.selfUser.asMention} in direct message channels.")
            }
        }
    }
}