package me.ddivad.keeper.utilities

import dev.kord.core.entity.Guild

fun buildLogMessage(guild: Guild, message: String) =
    "${guild.name} (${guild.id}): $message"