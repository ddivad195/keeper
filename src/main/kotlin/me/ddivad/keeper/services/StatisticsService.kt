package me.ddivad.keeper.services

import com.gitlab.kordlib.core.event.message.ReactionAddEvent
import me.jakejmattson.discordkt.api.annotations.Service
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.utilities.timeToString
import me.jakejmattson.discordkt.api.Discord
import me.jakejmattson.discordkt.api.extensions.toTimeString
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

    suspend fun bookmarkAdded(event: ReactionAddEvent) {
        event.getGuild()?.let {
            totalBookmarks++
            configuration.totalBookmarks++
            configuration[it.id.longValue]!!.bookmarkCount++
            configuration.save()
        }
    }

    val uptime: String
        get() = ((Date().time - startTime.time) / 1000).toTimeString()

    val ping: String
        get() = "${discord.api.gateway.averagePing}"
}