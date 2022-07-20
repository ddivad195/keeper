package me.ddivad.keeper.commands

import dev.kord.rest.request.KtorRequestException
import dev.kord.x.emoji.Emojis
import dev.kord.x.emoji.addReaction
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.ddivad.keeper.embeds.buildSavedMessageEmbed
import me.ddivad.keeper.services.StatisticsService
import me.jakejmattson.discordkt.arguments.MessageArg
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.extensions.sendPrivateMessage
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

@Suppress("unused")
fun generalCommands(configuration: Configuration, statsService: StatisticsService) = commands("General") {
    slash("bookmark", "Bookmark") {
        description = "Bookmark a message using Keeper"
        requiredPermissions = Permissions.EVERYONE
        execute(MessageArg) {
            val guild = guild.asGuildOrNull()
            statsService.bookmarkAdded(guild)
            try {
                this.author.sendPrivateMessage {
                    buildSavedMessageEmbed(args.first.asMessage(), guild)
                }.addReaction(Emojis.x)
                respond("Message Bookmarked ${Emojis.bookmark}")
                logger.info { "Message Bookmarked by ${this.author.username}" }
            } catch (e: KtorRequestException) {
                respond("Looks like you have DMs disabled. To bookmark messages, this needs to be enabled.")
                logger.error { "Bookmark DM could not be sent" }
            }

        }
    }
}