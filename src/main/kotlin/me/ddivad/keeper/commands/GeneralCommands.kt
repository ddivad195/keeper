package me.ddivad.keeper.commands

import dev.kord.x.emoji.Emojis
import dev.kord.x.emoji.addReaction
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.ddivad.keeper.embeds.buildSavedMessageEmbed
import me.ddivad.keeper.services.StatisticsService
import me.jakejmattson.discordkt.api.arguments.MessageArg
import me.jakejmattson.discordkt.api.commands.commands
import me.jakejmattson.discordkt.api.extensions.sendPrivateMessage

@Suppress("unused")
fun generalCommands(configuration: Configuration, statsService: StatisticsService) = commands("General") {
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

    slash("bookmark", "Bookmark") {
        description = "Bookmark a message using Keeper"
        requiredPermission = Permissions.NONE
        execute(MessageArg) {
            val guild = guild?.asGuildOrNull() ?: return@execute
            statsService.bookmarkAdded(guild)
            this.author.sendPrivateMessage {
                buildSavedMessageEmbed(args.first.asMessage(), guild)
            }.addReaction(Emojis.x)
            respond("Message Bookmarked ${Emojis.bookmark}")
        }
    }
}