package me.ddivad.keeper.extensions

import me.ddivad.keeper.services.DEFAULT_REQUIRED_PERMISSION
import me.ddivad.keeper.services.Permission
import me.jakejmattson.discordkt.api.dsl.Command

val commandPermissions: MutableMap<Command, Permission> = mutableMapOf()

var Command.requiredPermissionLevel: Permission
    get() = commandPermissions[this] ?: DEFAULT_REQUIRED_PERMISSION
    set(value) {
        commandPermissions[this] = value
    }