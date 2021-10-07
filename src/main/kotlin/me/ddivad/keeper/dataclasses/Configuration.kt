package me.ddivad.keeper.dataclasses

import dev.kord.core.entity.Guild
import dev.kord.core.entity.Role
import kotlinx.serialization.Serializable
import me.jakejmattson.discordkt.api.dsl.Data

@Serializable
data class Configuration(val botOwner: Long = 394484823944593409,
                         var totalBookmarks: Int = 0,
                         val guildConfigurations: MutableMap<Long, GuildConfiguration> = mutableMapOf()) : Data() {
    operator fun get(id: Long) = guildConfigurations[id]

    fun setup(guild: Guild, prefix: String, role: Role, reaction: String) {
        if (guildConfigurations[guild.id.value] != null) return

        val newConfiguration = GuildConfiguration(prefix, role.id.value, reaction, true)

        guildConfigurations[guild.id.value] = newConfiguration
        save()
    }
    fun hasGuildConfig(guildId: Long) = guildConfigurations.containsKey(guildId)
}

@Serializable
data class GuildConfiguration(
        var prefix: String,
        var requiredRoleId: Long,
        var bookmarkReaction: String,
        var enabled: Boolean,
        var bookmarkCount: Int = 0
)