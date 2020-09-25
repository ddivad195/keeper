package me.ddivad.keeper.services

import me.jakejmattson.discordkt.api.annotations.Service
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.utilities.timeToString
import me.jakejmattson.discordkt.api.Discord
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import java.util.*

@Service
class StatisticsService(private val configuration: Configuration, private val discord: Discord) {
    private var startTime: Date = Date()
    private var _totalBookmarks: Int = 0
    var totalBookmarks: Int
        get() = _totalBookmarks
        private set(value) {
            _totalBookmarks = value
        }

    fun bookmarkAdded(event: GuildMessageReactionAddEvent) {
        totalBookmarks++
        configuration.totalBookmarks++
        configuration[event.guild.idLong]!!.bookmarkCount++
        configuration.save()
    }

    val uptime: String
        get() = timeToString(Date().time - startTime.time)

    val ping: String
        get() = "${discord.jda.gatewayPing} ms"
}