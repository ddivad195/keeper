package me.ddivad.keeper

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.common.kColor
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.gateway.PrivilegedIntent
import me.ddivad.keeper.dataclasses.Configuration
import me.ddivad.keeper.dataclasses.Permissions
import me.ddivad.keeper.services.CacheService
import me.ddivad.keeper.services.StatisticsService
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.addInlineField
import me.jakejmattson.discordkt.api.extensions.profileLink
import me.jakejmattson.discordkt.api.extensions.toSnowflake
import java.awt.Color

@KordPreview
@PrivilegedIntent
suspend fun main() {
    val token = System.getenv("BOT_TOKEN") ?: null
    val defaultPrefix = System.getenv("DEFAULT_PREFIX") ?: "<none>"
    require(token != null) { "Expected the bot token as an environment variable" }

    bot(token) {
        prefix {
            val configuration = discord.getInjectionObjects(Configuration::class)

            guild?.let { configuration[guild!!.id.value]?.prefix } ?: defaultPrefix
        }
        configure {
            commandReaction = null
            allowMentionPrefix = true
            theme = Color(0x00BFFF)
            entitySupplyStrategy = EntitySupplyStrategy.cacheWithCachingRestFallback
            permissions(Permissions.STAFF)
        }

        mentionEmbed {
            val (configuration, statsService) = it.discord.getInjectionObjects(Configuration::class, StatisticsService::class)
            val guild = it.guild ?: return@mentionEmbed
            val guildConfiguration = configuration[it.guild!!.id.value] ?: return@mentionEmbed
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
            color = it.discord.configuration.theme?.kColor
            description = "A bot for saving useful messages to a DM by reacting to them."
            addInlineField("Required role", liveRole.mention)
            addInlineField("Prefix", it.prefix())
            addField("Config Info", "```" +
                    "Enabled: ${guildConfiguration.enabled}\n" +
                    "Reaction: ${guildConfiguration.bookmarkReaction}\n" +
                    "```")
            addField("Bot Info", "```" +
                    "Version: 1.4.1\n" +
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
