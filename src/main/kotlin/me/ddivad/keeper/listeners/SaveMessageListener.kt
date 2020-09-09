package me.ddivad.keeper.listeners

import com.google.common.eventbus.Subscribe
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.utilities.buildSavedMessageEmbed
import me.jakejmattson.discordkt.api.extensions.jda.sendPrivateMessage
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent

class ModerationListener(private val configuration: Configuration) {
    @Subscribe
    fun onGuildMessageReactionAddEvent(event: GuildMessageReactionAddEvent) {
        if (event.user.isBot) return
        if (!configuration[event.guild.idLong]?.enabled!!) return

        if (event.reaction.reactionEmote.name == configuration[event.guild.idLong]?.bookmarkReaction) {
            event.channel.retrieveMessageById(event.messageId).queue {
                event.user.sendPrivateMessage(buildSavedMessageEmbed(it.author, it, event.channel, event.guild))
            }
        }
    }
}
