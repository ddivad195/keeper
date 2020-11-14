package me.ddivad.keeper.embeds

import com.gitlab.kordlib.core.entity.Guild
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.rest.Image
import com.gitlab.kordlib.rest.builder.message.EmbedBuilder
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.extensions.jumpLink
import me.ddivad.keeper.services.StatisticsService
import me.jakejmattson.discordkt.api.extensions.addField
import java.awt.Color

fun EmbedBuilder.buildSavedMessageEmbed(message: Message, guild: Guild) {
    color = Color(0x00BFFF)
    author {
        name = message.author?.tag
        icon = message.author?.avatar?.url
    }
    description = "**Saved from **${message.channel.mention}\n\n${message.content}"
    if (message.attachments.isNotEmpty()) image = message.attachments.first().url

    addField("", "[View Original](${message.jumpLink(guild.id.value)})")
    footer {
        icon = guild.getIconUrl(Image.Format.PNG) ?: ""
        text = guild.name
    }
}

fun EmbedBuilder.buildStatsEmbed(guild: Guild, configuration: Configuration, statsService: StatisticsService) {
    title = "Stats"
    color = Color(0x00BFFF)
    field {
        name = "Messages Bookmarked"
        value = """
                        ```
                        Total Bookmarks (global): ${String.format("%4d", configuration.totalBookmarks)}
                        Total Bookmarks (guild):  ${String.format("%4d", configuration[guild.id.longValue]?.bookmarkCount)}
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