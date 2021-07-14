package me.ddivad.keeper.embeds

import dev.kord.common.kColor
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Message
import dev.kord.rest.Image
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.datetime.toJavaInstant
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.utilities.formatDate
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.jumpLink
import java.awt.Color

suspend fun EmbedBuilder.buildSavedMessageEmbed(message: Message, guild: Guild) {
    color = Color(0x00BFFF).kColor
    author {
        name = message.author?.tag
        icon = message.author?.avatar?.url
    }
    description = "**Saved from **${message.channel.mention}\n\n${message.content}"
    if (message.attachments.isNotEmpty()) image = message.attachments.first().url

    addField("", "[View Original](${message.jumpLink()})")
    footer {
        icon = guild.getIconUrl(Image.Format.PNG) ?: ""
        text = "${guild.name}  •  ${formatDate(message.timestamp.toJavaInstant())}"
    }
}

fun EmbedBuilder.buildStatsEmbed(guild: Guild, configuration: Configuration, statsService: StatisticsService) {
    title = "Stats"
    color = Color(0x00BFFF).kColor
    field {
        name = "Messages Bookmarked"
        value = """
                        ```
                        Total Bookmarks (global): ${String.format("%4d", configuration.totalBookmarks)}
                        Total Bookmarks (guild):  ${String.format("%4d", configuration[guild.id.value]?.bookmarkCount)}
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