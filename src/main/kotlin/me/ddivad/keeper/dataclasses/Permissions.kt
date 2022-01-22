package me.ddivad.keeper.dataclasses

import me.jakejmattson.discordkt.dsl.Permission
import me.jakejmattson.discordkt.dsl.PermissionSet
import me.jakejmattson.discordkt.dsl.permission

@Suppress("unused")
object Permissions : PermissionSet {
    val BOT_OWNER = permission("Bot Owner") { users(discord.getInjectionObjects<Configuration>().botOwner) }
    val GUILD_OWNER = permission("Guild Owner") { users(guild!!.ownerId) }
    val STAFF = permission("Staff") {
        discord.getInjectionObjects<Configuration>()[guild!!.id]?.requiredRoleId?.let {
            roles(it)
        }
    }
    val NONE = permission("None") { roles(guild!!.everyoneRole.id) }

    override val hierarchy: List<Permission> = listOf(NONE, STAFF, GUILD_OWNER, BOT_OWNER)
    override val commandDefault: Permission = STAFF
}