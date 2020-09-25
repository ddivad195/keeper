package me.ddivad.keeper.services

import me.jakejmattson.discordkt.api.annotations.Service
import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.api.extensions.jda.toMember
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User

enum class Permission {
    BOT_OWNER,
    GUILD_OWNER,
    USER,
    EVERYONE
}

val DEFAULT_REQUIRED_PERMISSION = Permission.USER

@Service
class PermissionsService(private val configuration: Configuration) {
    fun hasClearance(guild: Guild?, user: User, requiredPermissionLevel: Permission): Boolean {
        val permissionLevel = guild?.getMember(user)?.let { getPermissionLevel(it) }

        return if(permissionLevel == null) {
            requiredPermissionLevel == Permission.EVERYONE
        } else {
            getPermissionLevel(user.toMember(guild)!!).ordinal <= requiredPermissionLevel.ordinal
        }
    }

    private fun getPermissionLevel(member: Member) =
            when {
                member.isBotOwner() -> Permission.BOT_OWNER
                member.isGuildOwner() -> Permission.GUILD_OWNER
                member.isUser() -> Permission.USER
                else -> Permission.EVERYONE
            }

    private fun Member.isBotOwner() = user.idLong == configuration.botOwner
    private fun Member.isGuildOwner() = isOwner
    private fun Member.isUser() = configuration[guild.idLong]?.getLiveRole(guild.jda) in roles
}