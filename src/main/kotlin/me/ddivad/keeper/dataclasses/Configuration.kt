package me.ddivad.keeper.dataclasses

import me.jakejmattson.discordkt.api.dsl.data.Data
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

data class Configuration(val botOwner: Long = 394484823944593409,
                         var totalBookmarks: Int = 0,
                         val guildConfigurations: MutableMap<Long, GuildConfiguration> = mutableMapOf()) : Data("config/config.json") {
    operator fun get(id: Long) = guildConfigurations[id]

    fun setup(guild: Guild, prefix: String, role: Role, reaction: String) {
        if (guildConfigurations[guild.idLong] != null) return

        val newConfiguration = GuildConfiguration(prefix, role.idLong, reaction, true)

        guildConfigurations[guild.idLong] = newConfiguration
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
    fun getLiveRole(jda: JDA) = jda.getRoleById(requiredRoleId)
}