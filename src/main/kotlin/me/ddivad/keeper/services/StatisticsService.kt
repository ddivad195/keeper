package me.ddivad.keeper.services

import dev.kord.core.entity.Guild
import me.jakejmattson.discordkt.annotations.Service
import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.Discord
import me.jakejmattson.discordkt.extensions.toTimeString
import java.util.*
import kotlin.time.ExperimentalTime

@Service
class StatisticsService(private val configuration: Configuration, private val discord: Discord) {
    private var startTime: Date = Date()
    private var _totalBookmarks: Int = 0
    var totalBookmarks: Int
        get() = _totalBookmarks
        private set(value) {
            _totalBookmarks = value
        }

    fun bookmarkAdded(guild: Guild) {
        totalBookmarks++
        configuration.totalBookmarks++
        configuration[guild.id]!!.bookmarkCount++
        configuration.save()
    }

    val uptime: String
        get() = ((Date().time - startTime.time) / 1000).toTimeString()

    @OptIn(ExperimentalTime::class)
    val ping: String
        get() = "${discord.kord.gateway.averagePing}"
}