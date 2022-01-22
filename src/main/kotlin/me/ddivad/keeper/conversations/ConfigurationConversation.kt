package me.ddivad.keeper.conversations

import dev.kord.core.entity.Guild
import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.arguments.EveryArg
import me.jakejmattson.discordkt.arguments.RoleArg
import me.jakejmattson.discordkt.arguments.UnicodeEmojiArg
import me.jakejmattson.discordkt.conversations.conversation

class ConfigurationConversation(private val configuration: Configuration) {
    fun createConfigurationConversation(guild: Guild) = conversation {
        val prefix = prompt(EveryArg, "Bot prefix:")
        val role = prompt(RoleArg, "Admin role:")
        val reaction = prompt(UnicodeEmojiArg, "Save message reaction:")

        configuration.setup(guild, prefix, role, reaction.unicode)
        respond("Guild setup")
    }
}