package me.ddivad.keeper.utilities

import me.jakejmattson.discordkt.api.dsl.embed.embed
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

fun buildSavedMessageEmbed(messageAuthor: User, message: Message, messageChannel: MessageChannel, guild: Guild) = embed {
    color = infoColor
    author {
        name = messageAuthor.asTag
        iconUrl = messageAuthor.effectiveAvatarUrl
    }
    addField("Saved from #${messageChannel.name}", message.contentRaw)
    addField("", "[View Original](${message.jumpUrl})")
    footer {
        text = guild.name
        iconUrl = guild.iconUrl
        timeStamp = message.timeCreated
    }
}