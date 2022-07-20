package me.ddivad.keeper.dataclasses

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions

@Suppress("unused")
object Permissions {
    val GUILD_OWNER = Permissions(Permission.ManageGuild)
    val STAFF = Permissions(Permission.ManageMessages)
    val EVERYONE = Permissions(Permission.SendMessages)
}