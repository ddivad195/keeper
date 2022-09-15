package me.ddivad.keeper.dataclasses

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import me.ddivad.keeper.logger
import me.ddivad.keeper.utilities.buildLogMessage
import me.jakejmattson.discordkt.Discord
import me.jakejmattson.discordkt.dsl.Data
import me.jakejmattson.discordkt.dsl.edit
import me.jakejmattson.discordkt.extensions.sendPrivateMessage
import mu.KotlinLogging

val logger = KotlinLogging.logger {  }

@Serializable
data class Configuration(
    var totalBookmarks: Int = 0,
    val guildConfigurations: MutableMap<Snowflake, GuildConfiguration> = mutableMapOf()
) : Data() {
    operator fun get(id: Snowflake) = guildConfigurations[id]

    fun setup(guild: Guild, reaction: String) {
        if (guildConfigurations[guild.id] != null) return
        val newConfiguration = GuildConfiguration(reaction, true)
        edit { guildConfigurations[guild.id] = newConfiguration }
    }

    fun hasGuildConfig(guildId: Snowflake) = guildConfigurations.containsKey(guildId)
}

@Serializable
data class GuildConfiguration(
    var bookmarkReaction: String,
    var enabled: Boolean,
    var bookmarkCount: Int = 0,
    val reminders: MutableList<Reminder> = mutableListOf()
)

@Serializable
data class Reminder(val user: Snowflake, val guildId: Snowflake, val endTime: Long, val message: MessageDetails) {
    fun launch(discord: Discord, configuration: Configuration) {
        val guildConfiguration = configuration[guildId] ?: return
        logger.debug { "Adding reminder - User: $user - Guild: $guildId, Endtime: $endTime" }
        GlobalScope.launch {
            delay(endTime - System.currentTimeMillis())
            logger.debug { "Reminder triggered: $user - Guild: $guildId, Endtime: $endTime" }
            val author = message.author?.let { discord.kord.getUser(it) }
            val channel = discord.kord.getChannel(message.channel)
            discord.kord.getUser(user)?.sendPrivateMessage {
                title = "Reminder"
                description = "You asked me to remind you about [this message](${message.link}) from ${author?.mention} in ${channel?.mention}"
                color = discord.configuration.theme
            }
            configuration.edit { guildConfiguration.reminders.remove(this@Reminder) }
        }
    }
}

@Serializable
data class MessageDetails(val channel: Snowflake, val author: Snowflake?, val link: String)