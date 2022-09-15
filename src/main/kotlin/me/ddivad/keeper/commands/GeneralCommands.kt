package me.ddivad.keeper.commands

import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Message
import dev.kord.rest.request.KtorRequestException
import dev.kord.x.emoji.Emojis
import dev.kord.x.emoji.addReaction
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.MessageDetails
import me.ddivad.keeper.dataclasses.Permissions
import me.ddivad.keeper.dataclasses.Reminder
import me.ddivad.keeper.embeds.buildSavedMessageEmbed
import me.ddivad.keeper.embeds.buildStatsEmbed
import me.ddivad.keeper.services.StatisticsService
import me.ddivad.keeper.utilities.buildLogMessage
import me.jakejmattson.discordkt.commands.commands
import me.jakejmattson.discordkt.conversations.conversation
import me.jakejmattson.discordkt.dsl.edit
import me.jakejmattson.discordkt.extensions.*
import mu.KotlinLogging
import java.time.Instant
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger { }

@Suppress("unused")
fun generalCommands(configuration: Configuration, statsService: StatisticsService) = commands("General") {
    message("Bookmark Message", "bookmark", "Bookmark this message", Permissions.EVERYONE) {
        val guild = guild.asGuildOrNull()
        statsService.bookmarkAdded(guild)
        try {
            this.author.sendPrivateMessage {
                buildSavedMessageEmbed(args.first.asMessage(), guild)
            }.addReaction(Emojis.x)
            respond("Message Bookmarked ${Emojis.bookmark}")
            logger.info { buildLogMessage(guild, "Message Bookmarked by ${this.author.idDescriptor()}") }
        } catch (e: KtorRequestException) {
            respond("Looks like you have DMs disabled. To bookmark messages, this needs to be enabled.")
            logger.error { buildLogMessage(guild, "Bookmark DM could not be sent") }
        }
    }

    message("Remind Me", "reminder", "Set a reminder about a message", Permissions.EVERYONE) {
        val guild = guild.asGuildOrNull()
        val interactionResponse = interaction?.deferEphemeralResponse() ?: return@message
        try {
            interactionResponse.respond {
                content = "Choose a time in the DM you just received and I'll remind you then!"
            }
            TimeConversation(configuration).createTimeConversation(guild, arg).startPrivately(discord, author)
        } catch (e: KtorRequestException) {
            interactionResponse.respond {
                content = "Looks like you have DMs disabled. To add message reminders, this needs to be enabled."
            }
            logger.error { buildLogMessage(guild, "Message reminder could not be added") }
        }
    }

    slash("stats", "View Keeper statistics", Permissions.EVERYONE) {
        execute {
            respondPublic {
                buildStatsEmbed(guild, configuration, statsService)
            }
        }
    }
}

class TimeConversation(private val configuration: Configuration) {
    fun createTimeConversation(guild: Guild, message: Message) = conversation {
        if (message.jumpLink() == null) {
            respond("Message is required")
            return@conversation
        }
        val guildConfiguration = configuration[guild.id] ?: return@conversation

        val time = promptSelect {
            this.selectionCount = 1..1
            content("Choose a reminder duration")
            option("1hr", description = "Set a 1 hour reminder", value = "3600000")
            option("3hr", description = "Set a 3 hour reminder", value = "10800000")
            option("12hr", description = "Set a 12 hour reminder", value = "43200000")
            option("24hr", description = "Set a 24 hour reminder", value = "86400000")
            option("cancel", description = "Cancel", value = "cancel")
        }.first()

        if (time != "cancel") {
            val endTime = Instant.now().plusMillis(time.toLong())
            val messageDetails = MessageDetails(message.channelId, message.author?.id, message.jumpLink()!!)
            val reminder = Reminder(user.id, guild.id, endTime.toEpochMilli(), messageDetails)
            configuration.edit { guildConfiguration.reminders.add(reminder) }
            reminder.launch(discord, configuration)
            respond("Got it, I'll remind you ${TimeStamp.at(endTime, TimeStyle.RELATIVE)}")
            logger.info {
                buildLogMessage(
                    guild,
                    "Message reminder created by ${user.idDescriptor()} for ${TimeUnit.MILLISECONDS.toHours(time.toLong())} hours"
                )
            }
        }
    }
}