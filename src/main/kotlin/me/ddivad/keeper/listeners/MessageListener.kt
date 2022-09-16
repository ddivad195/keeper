package me.ddivad.keeper.listeners

import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.rest.request.KtorRequestException
import dev.kord.x.emoji.Emojis
import dev.kord.x.emoji.addReaction
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.embeds.buildSavedMessageEmbed
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.utilities.buildLogMessage
import me.jakejmattson.discordkt.dsl.listeners
import me.jakejmattson.discordkt.extensions.idDescriptor
import me.jakejmattson.discordkt.extensions.isSelf
import me.jakejmattson.discordkt.extensions.sendPrivateMessage
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

@Suppress("unused")
fun onGuildMessageReactionAddEvent(configuration: Configuration, statsService: StatisticsService) = listeners {
    on<ReactionAddEvent> {
        if (guild !== null) {
            val guild = guild?.asGuildOrNull() ?: return@on
            val guildConfiguration = configuration[guild.id] ?: return@on
            val msg = message.asMessageOrNull() ?: return@on
            if (!guildConfiguration.enabled) return@on
            val user = user.asUser()

            if (this.emoji.name == configuration[guild.id]?.bookmarkReaction) {
                statsService.bookmarkAdded(guild)
                try {
                   user.sendPrivateMessage {
                        buildSavedMessageEmbed(msg, guild)
                    }.addReaction(Emojis.x)
                    logger.info { buildLogMessage(guild, "Message Bookmarked by ${user.idDescriptor()}") }
                } catch (e: KtorRequestException) {
                    logger.error { buildLogMessage(guild, "Bookmark DM could not be sent to ${user.idDescriptor()}") }
                }
            }
        } else {
            if (this.emoji.name == "❌" && !this.user.isSelf()) {
                this.message.delete()
                val user = user.asUser()
                logger.info { "${user.idDescriptor()}: Bookmark Deleted" }
            }
        }
    }
}

