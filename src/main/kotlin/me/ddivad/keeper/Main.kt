package me.ddivad.keeper

import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.extensions.requiredPermissionLevel
import me.ddivad.keeper.services.PermissionsService
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.jda.fullName
import me.jakejmattson.discordkt.api.extensions.jda.profileLink
import me.jakejmattson.discordkt.api.extensions.jda.toMember
import java.awt.Color

fun main(args: Array<String>) {
    val token = args.firstOrNull()
            ?: throw IllegalArgumentException("No program arguments provided. Expected bot token.")

    bot(token) {
        configure {
            val (configuration, permissionsService)
                    = it.getInjectionObjects(Configuration::class, PermissionsService::class)

            commandReaction = null
            allowMentionPrefix = true

            prefix {
                it.guild?.let { configuration[it.idLong]?.prefix } ?: "<none>"
            }

            colors {
                infoColor = Color(0x00BFFF)
            }

            mentionEmbed {
                val guild = it.guild ?: return@mentionEmbed

                val jda = it.discord.jda
                val properties = it.discord.properties
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
                description = "A bot for saving useful messages." +
                        (if (role.isNotBlank())
                            "\nRequired Role: $role"
                        else "") +
                        "\nCurrent Prefix: `$prefix`" +
                        "\nUse `${prefix}help` to see commands."

                footer {
                    text = "2.1.0 - ${properties.libraryVersion} - ${properties.jdaVersion}"
                }
            }

            visibilityPredicate {
                val guild = it.guild ?: return@visibilityPredicate false
                val member = it.user.toMember(guild)!!
                val permission = it.command.requiredPermissionLevel

                permissionsService.hasClearance(member, permission)
            }

//            it.jda.guilds.forEach {
//                configuration.setup(it)
//            }
        }
    }
}