package me.ddivad.keeper

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.gateway.Intent
import com.gitlab.kordlib.gateway.Intents
import com.gitlab.kordlib.gateway.PrivilegedIntent
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.extensions.requiredPermissionLevel
import me.ddivad.keeper.services.PermissionsService
import me.ddivad.keeper.services.StatisticsService
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.addInlineField
import me.jakejmattson.discordkt.api.extensions.profileLink
import me.jakejmattson.discordkt.api.extensions.toSnowflake
import java.awt.Color

@PrivilegedIntent
suspend fun main(args: Array<String>) {
    val token = System.getenv("BOT_TOKEN") ?: null
    val defaultPrefix = System.getenv("DEFAULT_PREFIX") ?: "<none>"
    require(token != null) { "Expected the bot token as an environment variable" }

    bot(token) {
        prefix {
            val configuration = discord.getInjectionObjects(Configuration::class)

            guild?.let { configuration[guild!!.id.longValue]?.prefix } ?: defaultPrefix
        }
        configure {
            commandReaction = null
            allowMentionPrefix = true
            theme = Color(0x00BFFF)
        }

        mentionEmbed {
            val (configuration, statsService) = it.discord.getInjectionObjects(Configuration::class, StatisticsService::class)
            val guild = it.guild ?: return@mentionEmbed
            val guildConfiguration = configuration[it.guild!!.id.longValue] ?: return@mentionEmbed
            val self = it.channel.kord.getSelf()
            val liveRole = guild.getRole(guildConfiguration.requiredRoleId.toSnowflake())
            author {
                val user = guild.kord.getUser(Snowflake(394484823944593409))
                icon = user?.avatar?.url
                name = user?.username
                url = user?.profileLink
            }

            title = "Keeper"
            thumbnail {
                url = self.avatar.url
            }
            color = it.discord.configuration.theme
            description = "A bot for saving useful messages to a DM by reacting to them."
            addInlineField("Required role", liveRole.mention)
            addInlineField("Prefix", it.prefix())
            addField("Config Info", "```" +
                    "Enabled: ${guildConfiguration.enabled}\n" +
                    "Reaction: ${guildConfiguration.bookmarkReaction}\n" +
                    "```")
            addField("Bot Info", "```" +
                    "Version: 1.3.2\n" +
                    "DiscordKt: ${it.discord.versions.library}\n" +
                    "Kotlin: ${KotlinVersion.CURRENT}\n" +
                    "```")
            addField("Uptime", statsService.uptime)
            addField("Source", "http://github.com/ddivad195/keeper")
        }

        intents {
            Intents.nonPrivileged.intents.forEach {
                +it
            }
            +Intent.GuildMembers
        }


        permissions {
            val requiredPermissionLevel = command.requiredPermissionLevel
            val guild = guild ?: return@permissions false
            val member = user.asMember(guild.id)

            val permissionsService = discord.getInjectionObjects(PermissionsService::class)
            return@permissions permissionsService.hasClearance(member, requiredPermissionLevel)
        }

    }
}
