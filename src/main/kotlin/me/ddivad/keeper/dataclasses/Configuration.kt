package me.ddivad.keeper.dataclasses

import com.gitlab.kordlib.core.entity.Guild
import com.gitlab.kordlib.core.entity.Role
import me.jakejmattson.discordkt.api.dsl.Data
import me.jakejmattson.discordkt.api.extensions.toSnowflake


data class Configuration(val botOwner: Long = 394484823944593409,
                         var totalBookmarks: Int = 0,
                         val guildConfigurations: MutableMap<Long, GuildConfiguration> = mutableMapOf()) : Data("config/config.json") {
    operator fun get(id: Long) = guildConfigurations[id]

    fun setup(guild: Guild, prefix: String, role: Role, reaction: String) {
        if (guildConfigurations[guild.id.longValue] != null) return

        val newConfiguration = GuildConfiguration(prefix, role.id.longValue, reaction, true)

        guildConfigurations[guild.id.longValue] = newConfiguration
        save()
    }
    fun hasGuildConfig(guildId: Long) = guildConfigurations.containsKey(guildId)
}

data class GuildConfiguration(
        var prefix: String,
        var requiredRoleId: Long,
        var bookmarkReaction: String,
        var enabled: Boolean,
        var bookmarkCount: Int = 0
) {
    suspend fun getLiveRole(guild: Guild) = guild.getRole(requiredRoleId.toSnowflake())
}