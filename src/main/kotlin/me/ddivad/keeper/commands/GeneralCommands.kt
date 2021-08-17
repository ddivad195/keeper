package me.ddivad.keeper.commands

import me.ddivad.keeper.dataclasses.Permissions
import me.jakejmattson.discordkt.api.arguments.MessageArg
import me.jakejmattson.discordkt.api.commands.commands

@Suppress("unused")
fun generalCommands() = commands("General") {
    dmCommand("delete") {
        description = "Delete a Keeper bookmark by ID inside of DM channel"
        requiredPermission = Permissions.NONE
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