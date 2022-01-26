package me.ddivad.keeper

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.ddivad.keeper.services.CacheService
import me.ddivad.keeper.services.StatisticsService
import me.jakejmattson.discordkt.dsl.bot
import me.jakejmattson.discordkt.extensions.*
import java.awt.Color

@KordPreview
@PrivilegedIntent
suspend fun main() {
    val token = System.getenv("BOT_TOKEN") ?: null
    val defaultPrefix = System.getenv("DEFAULT_PREFIX") ?: "<none>"
    require(token != null) { "Expected the bot token as an environment variable" }

    bot(token) {
        val configuration = data("config/config.json") { Configuration() }

        prefix {
            guild?.let { configuration[guild!!.id]?.prefix } ?: defaultPrefix
        }

        configure {
            commandReaction = null
            allowMentionPrefix = true
            theme = Color(0x00BFFF)
            entitySupplyStrategy = EntitySupplyStrategy.cacheWithCachingRestFallback
            intents = Intents(
                Intent.GuildMessageReactions,
                Intent.DirectMessagesReactions
            )
            permissions = Permissions
        }

        mentionEmbed {
            val (configuration, statsService) = it.discord.getInjectionObjects(Configuration::class, StatisticsService::class)
            val guild = it.guild ?: return@mentionEmbed
            val guildConfiguration = configuration[it.guild!!.id] ?: return@mentionEmbed
            val self = it.channel.kord.getSelf()
            val liveRole = guild.getRole(guildConfiguration.requiredRoleId)
            author {
                val user = guild.kord.getUser(Snowflake(394484823944593409))
                icon = user?.avatar?.url
                name = user?.username
                url = user?.profileLink
            }

            title = "Keeper"
            thumbnail {
                url = self.pfpUrl
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
                    "Version: 1.7.0\n" +
                    "DiscordKt: ${it.discord.versions.library}\n" +
                    "Kord: ${it.discord.versions.kord}\n" +
                    "Kotlin: ${KotlinVersion.CURRENT}\n" +
                    "```")
            addField("Uptime", statsService.uptime)
            addField("Source", "http://github.com/ddivad195/keeper")
        }

        onStart {
            val cacheService = this.getInjectionObjects(CacheService::class)
            try {
                cacheService.run()
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }
}
