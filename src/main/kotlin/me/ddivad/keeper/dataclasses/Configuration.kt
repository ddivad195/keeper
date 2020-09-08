package me.ddivad.keeper.dataclasses

import me.jakejmattson.discordkt.api.dsl.data.Data
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

data class Configuration(val botOwner: Long = 394484823944593409,
                         val guildConfigurations: MutableMap<Long, GuildConfiguration> = mutableMapOf()) : Data("config/config.json") {
    operator fun get(id: Long) = guildConfigurations[id]

    fun setup(guild: Guild, prefix: String, role: Role, reaction: String) {
        if (guildConfigurations[guild.idLong] != null) return

        val newConfiguration = GuildConfiguration(prefix, role.idLong, reaction)

        guildConfigurations[guild.idLong] = newConfiguration
        save()
    }
}

data class GuildConfiguration(
        var prefix: String,
        var requiredRoleId: Long,
        var bookmarkReaction: String
) {
    fun getLiveRole(jda: JDA) = jda.getRoleById(requiredRoleId)
}