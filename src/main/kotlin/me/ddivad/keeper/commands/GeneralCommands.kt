package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.extensions.requiredPermissionLevel
import me.ddivad.keeper.services.Permission
import me.jakejmattson.discordkt.api.arguments.MessageArg
import me.jakejmattson.discordkt.api.dsl.commands

fun generalCommands(configuration: Configuration) = commands("General") {
    dmCommand("delete") {
        description = "Delete a Keeper bookmark by ID inside of DM channel"
        requiredPermissionLevel = Permission.USER
        execute(MessageArg) {
            val message = args.first

            if (message.author == this.channel.kord.getSelf().asUser()) {
                message.delete()
            } else {
                respond("Can only delete messages sent by ${this.channel.kord.getSelf().mention} in direct message channels.")
            }
        }
    }
}