package me.ddivad.keeper.dataclasses

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Role
import kotlinx.serialization.Serializable
import me.jakejmattson.discordkt.dsl.Data
import me.jakejmattson.discordkt.extensions.toSnowflake

@Serializable
data class Configuration(val botOwner: Snowflake = "394484823944593409".toSnowflake(),
                         var totalBookmarks: Int = 0,
                         val guildConfigurations: MutableMap<Snowflake, GuildConfiguration> = mutableMapOf()) : Data() {
    operator fun get(id: Snowflake) = guildConfigurations[id]

    fun setup(guild: Guild, prefix: String, role: Role, reaction: String) {
        if (guildConfigurations[guild.id] != null) return

        val newConfiguration = GuildConfiguration(prefix, role.id, reaction, true)

        guildConfigurations[guild.id] = newConfiguration
        save()
    }
    fun hasGuildConfig(guildId: Snowflake) = guildConfigurations.containsKey(guildId)
}

@Serializable
data class GuildConfiguration(
        var prefix: String,
        var requiredRoleId: Snowflake,
        var bookmarkReaction: String,
        var enabled: Boolean,
        var bookmarkCount: Int = 0
)