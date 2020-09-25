package me.ddivad.keeper.listeners

import com.google.common.eventbus.Subscribe
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.utilities.buildSavedMessageEmbed
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent

class MessageListener(private val configuration: Configuration, private val statsService: StatisticsService) {
    @Subscribe
    fun onGuildMessageReactionAddEvent(event: GuildMessageReactionAddEvent) {
        if (event.user.isBot) return
        if (!configuration[event.guild.idLong]?.enabled!!) return
        if (event.reaction.reactionEmote.name == configuration[event.guild.idLong]?.bookmarkReaction) {
            statsService.bookmarkAdded(event)
            event.channel.retrieveMessageById(event.messageId).queue { it ->
                event.user.openPrivateChannel().queue{ channel ->
                    channel.sendMessage(buildSavedMessageEmbed(it.author, it, event.channel, event.guild)).queue{ message ->
                        message.addReaction("❌").queue()
                    }
                }
            }
        }
    }

    @Subscribe
    fun onPrivateMessageReactionAddEvent(event: PrivateMessageReactionAddEvent) {
        if (event.user!!.isBot) return
        if (event.reactionEmote.name == "❌") {
            event.channel.retrieveMessageById(event.messageId).queue{ it.delete().queue() }
        }
    }
}
