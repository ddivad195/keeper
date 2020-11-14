package me.ddivad.keeper.services

import com.gitlab.kordlib.core.entity.Member
import kotlinx.coroutines.flow.toList
import me.jakejmattson.discordkt.api.annotations.Service
import me.ddivad.keeper.dataclasses.Configuration

enum class Permission {
    BOT_OWNER,
    GUILD_OWNER,
    STAFF,
    USER,
    NONE
}

val DEFAULT_REQUIRED_PERMISSION = Permission.STAFF

@Service
class PermissionsService(private val configuration: Configuration) {
    suspend fun hasClearance(member: Member, requiredPermissionLevel: Permission) = member.getPermissionLevel().ordinal <= requiredPermissionLevel.ordinal

    private suspend fun Member.getPermissionLevel() =
            when {
                isBotOwner() -> Permission.BOT_OWNER
                isGuildOwner() -> Permission.GUILD_OWNER
                isStaff() -> Permission.STAFF
                isUser() -> Permission.USER
                else -> Permission.NONE
            }

    private fun Member.isBotOwner() = id.longValue == configuration.botOwner
    private suspend fun Member.isGuildOwner() = isOwner()
    private suspend fun Member.isStaff(): Boolean {
        val config = configuration[guildId.longValue] ?: return false
        return roles.toList().any { it.id.longValue == config.requiredRoleId }
    }

    private suspend fun Member.isUser() = asMemberOrNull() != null
}