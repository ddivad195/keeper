package me.ddivad.keeper.conversations

import dev.kord.core.entity.Guild
import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.api.arguments.EveryArg
import me.jakejmattson.discordkt.api.arguments.RoleArg
import me.jakejmattson.discordkt.api.arguments.UnicodeEmojiArg
import me.jakejmattson.discordkt.api.conversations.conversation

class ConfigurationConversation(private val configuration: Configuration) {
    fun createConfigurationConversation(guild: Guild) = conversation {
        val prefix = prompt(EveryArg, "Bot prefix:")
        val role = prompt(RoleArg, "Admin role:")
        val reaction = prompt(UnicodeEmojiArg, "Save message reaction:")

        configuration.setup(guild, prefix, role, reaction.unicode)
    }
}