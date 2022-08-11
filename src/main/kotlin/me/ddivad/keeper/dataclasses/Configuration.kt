package me.ddivad.keeper.dataclasses

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import kotlinx.serialization.Serializable
import me.jakejmattson.discordkt.dsl.Data

@Serializable
data class Configuration(var totalBookmarks: Int = 0,
                         val guildConfigurations: MutableMap<Snowflake, GuildConfiguration> = mutableMapOf()) : Data() {
    operator fun get(id: Snowflake) = guildConfigurations[id]

    fun setup(guild: Guild, reaction: String) {
        if (guildConfigurations[guild.id] != null) return

        val newConfiguration = GuildConfiguration(reaction, true)

        guildConfigurations[guild.id] = newConfiguration
        save()
    }
    fun hasGuildConfig(guildId: Snowflake) = guildConfigurations.containsKey(guildId)
}

@Serializable
data class GuildConfiguration(
        var bookmarkReaction: String,
        var enabled: Boolean,
        var bookmarkCount: Int = 0
)