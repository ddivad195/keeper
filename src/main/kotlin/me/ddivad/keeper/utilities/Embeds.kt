package me.ddivad.keeper.utilities

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.StatisticsService
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

fun buildStatsEmbed(guild: Guild, configuration: Configuration, statsService: StatisticsService) = embed {
    simpleTitle = "Stats"
    color = infoColor
    field {
        name = "Messages Bookmarked"
        value = """
                        ```
                        Total Bookmarks (global): ${String.format("%4d", configuration.totalBookmarks)}
                        Total Bookmarks (guild):  ${String.format("%4d", configuration[guild.idLong]?.bookmarkCount)}
                        Since Restart:            ${String.format("%4d", statsService.totalBookmarks)}
                        ```
                    """.trimIndent()
    }
    field {
        name = "Ping"
        value = statsService.ping
        inline = true
    }

    field {
        name = "Uptime"
        value = statsService.uptime
    }
}