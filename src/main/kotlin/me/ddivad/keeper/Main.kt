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

val startTime = Date()

fun main(args: Array<String>) {
    val token = System.getenv("BOT_TOKEN") ?: null
    val prefix = System.getenv("DEFAULT_PREFIX") ?: "<none>"
    require(token != null) { "Expected the bot token as an environment variable" }

    bot(token) {
        configure {
            val (configuration, permissionsService, statsService: StatisticsService)
                    = it.getInjectionObjects(Configuration::class, PermissionsService::class, StatisticsService::class)

            commandReaction = null
            allowMentionPrefix = true

            prefix {
                it.guild?.let { configuration[it.idLong]?.prefix } ?: prefix
            }

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
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
                            "Version: 1.2.0\n" +
                            "DiscordKt: 0.19.0\n" +
                            "Kotlin: ${KotlinVersion.CURRENT}\n" +
                            "Uptime: ${statsService.uptime}" +
                            "```")
                    addInlineField("Source", "http://github.com/ddivad195/keeper")
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