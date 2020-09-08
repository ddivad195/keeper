package me.ddivad.keeper.conversations

import me.ddivad.keeper.dataclasses.Configuration
import me.jakejmattson.discordkt.api.arguments.EveryArg
import me.jakejmattson.discordkt.api.arguments.RoleArg
import me.jakejmattson.discordkt.api.arguments.UnicodeEmoteArg
import me.jakejmattson.discordkt.api.dsl.conversation.Conversation
import me.jakejmattson.discordkt.api.dsl.conversation.conversation
import net.dv8tion.jda.api.entities.Guild

class ConfigurationConversation(private val configuration: Configuration): Conversation() {
    @Conversation.Start
    fun createConfigurationConversation(guild: Guild) = conversation {
        val prefix = promptMessage(EveryArg, "Bot prefix:")
        val role = promptMessage(RoleArg, "Admin role:")
        val reaction = promptMessage(UnicodeEmoteArg, "Save message reaction:")

        configuration.setup(guild, prefix, role, reaction)
    }
}