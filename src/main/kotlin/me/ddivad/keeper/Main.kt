package me.ddivad.keeper

import com.google.gson.Gson
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.extensions.requiredPermissionLevel
import me.ddivad.keeper.services.PermissionsService
import me.ddivad.keeper.services.StatisticsService
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.jda.fullName
import me.jakejmattson.discordkt.api.extensions.jda.profileLink
import me.jakejmattson.discordkt.api.extensions.jda.toMember
import java.awt.Color
import java.util.*

data class Properties(val author: String, val version: String, val discordKt: String, val repository: String)

private val propFile = Properties::class.java.getResource("/properties.json").readText()
val project: Properties = Gson().fromJson(propFile, Properties::class.java)
val startTime = Date()

fun main(args: Array<String>) {
    val token = args.firstOrNull()
            ?: throw IllegalArgumentException("No program arguments provided. Expected bot token.")

    bot(token) {
        configure {
            val (configuration, permissionsService, statsService: StatisticsService)
                    = it.getInjectionObjects(Configuration::class, PermissionsService::class, StatisticsService::class)

            commandReaction = null
            allowMentionPrefix = true

            prefix {
                it.guild?.let { configuration[it.idLong]?.prefix } ?: "<none>"
            }

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
                with(project) {
                    val guild = it.guild ?: return@mentionEmbed
                    val jda = it.discord.jda
                    val prefix = it.relevantPrefix
                    val role = configuration[guild.idLong]?.getLiveRole(jda)?.takeUnless { it == guild.publicRole }?.asMention
                            ?: ""

                    author {
                        jda.retrieveUserById(394484823944593409).queue { user ->
                            iconUrl = user.effectiveAvatarUrl
                            name = user.fullName()
                            url = user.profileLink
                        }
                    }

                    simpleTitle = "Keeper"
                    thumbnail = jda.selfUser.effectiveAvatarUrl
                    color = infoColor
                    description = "A bot for saving useful messages to a DM by reacting to them."
                    addInlineField("Required role", role)
                    addInlineField("Prefix", prefix)
                    addField("Config Info", "```" +
                            "Enabled: ${configuration[it.guild!!.idLong]?.enabled}\n" +
                            "Reaction: ${configuration[it.guild!!.idLong]?.bookmarkReaction}\n" +
                            "```")
                    addField("Bot Info", "```" +
                            "Version: $version\n" +
                            "DiscordKt: $discordKt\n" +
                            "Kotlin: ${KotlinVersion.CURRENT}\n" +
                            "Uptime: ${statsService.uptime}" +
                            "```")
                    addInlineField("Source", repository)
                }
            }


            visibilityPredicate {
                val guild = it.guild ?: return@visibilityPredicate false
                val member = it.user.toMember(guild)!!
                val permission = it.command.requiredPermissionLevel

                permissionsService.hasClearance(guild, it.user, permission)
            }
        }
    }
}