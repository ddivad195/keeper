package me.ddivad.keeper.preconditions

import me.ddivad.keeper.extensions.requiredPermissionLevel
import me.ddivad.keeper.services.PermissionsService
import me.jakejmattson.discordkt.api.dsl.command.CommandEvent
import me.jakejmattson.discordkt.api.dsl.preconditions.*

class PermissionPrecondition(private val permissionsService: PermissionsService) : Precondition() {
    override fun evaluate(event: CommandEvent<*>): PreconditionResult {
        val command = event.command ?: return Fail()
        val requiredPermissionLevel = command.requiredPermissionLevel

        if (event.guild == null) {
            return if(permissionsService.hasClearance(null, event.author, requiredPermissionLevel)) {
                Pass
            } else {
                Fail("Missing clearance to use this command.")
            }
        } else {
            val guild = event.guild!!
            if (!permissionsService.hasClearance(event.guild, event.author, requiredPermissionLevel))
                return Fail("Missing clearance to use this command.")
            return Pass
        }
    }
}