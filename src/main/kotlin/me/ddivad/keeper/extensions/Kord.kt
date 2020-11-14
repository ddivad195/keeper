package me.ddivad.keeper.extensions

import com.gitlab.kordlib.core.entity.Message

fun Message.jumpLink(guildId:String) = "https://discord.com/channels/${guildId}/${channel.id.value}/${id.value}"