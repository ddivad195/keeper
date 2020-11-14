package me.ddivad.keeper.listeners

import com.gitlab.kordlib.core.event.message.ReactionAddEvent
import com.gitlab.kordlib.kordx.emoji.Emojis
import com.gitlab.kordlib.kordx.emoji.addReaction
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.embeds.buildSavedMessageEmbed
import me.jakejmattson.discordkt.api.dsl.listeners
import me.jakejmattson.discordkt.api.extensions.isSelf
import me.jakejmattson.discordkt.api.extensions.sendPrivateMessage

fun onGuildMessageReactionAddEvent(configuration: Configuration, statsService: StatisticsService) = listeners {
    on<ReactionAddEvent> {
        val guild = guild?.asGuildOrNull() ?: return@on
        if (!configuration[guild.id.longValue]?.enabled!!) return@on
        if (this.emoji.name == configuration[guild.id.longValue]?.bookmarkReaction) {
            statsService.bookmarkAdded(this)
            this.user.sendPrivateMessage {
                buildSavedMessageEmbed(getUser(), message.asMessage(), guild)
            }.addReaction(Emojis.x)
        }
    }

    on<ReactionAddEvent> {
        if (this.getGuild() == null && this.emoji.name == "❌" && !this.user.isSelf()) {
            this.message.delete()
        }
    }

}

