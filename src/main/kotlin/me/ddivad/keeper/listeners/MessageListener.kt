package me.ddivad.keeper.listeners

import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.x.emoji.Emojis
import dev.kord.x.emoji.addReaction
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.embeds.buildSavedMessageEmbed
import me.jakejmattson.discordkt.dsl.listeners
import me.jakejmattson.discordkt.extensions.isSelf
import me.jakejmattson.discordkt.extensions.sendPrivateMessage

@Suppress("unused")
fun onGuildMessageReactionAddEvent(configuration: Configuration, statsService: StatisticsService) = listeners {
    on<ReactionAddEvent> {
        if (guild !== null) {
            val guild = guild?.asGuildOrNull() ?: return@on
            val guildConfiguration = configuration[guild.id] ?: return@on
            if (!guildConfiguration.enabled) return@on

            if (this.emoji.name == configuration[guild.id]?.bookmarkReaction) {
                statsService.bookmarkAdded(guild)
                this.user.sendPrivateMessage {
                    buildSavedMessageEmbed(message.asMessage(), guild)
                }.addReaction(Emojis.x)
            }
        } else {
            if (this.emoji.name == "❌" && !this.user.isSelf()) {
                this.message.delete()
            }
        }
    }
}

