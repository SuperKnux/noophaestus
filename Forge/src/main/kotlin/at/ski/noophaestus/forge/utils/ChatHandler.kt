package at.ski.noophaestus.forge.utils

/**
 * Copyright (c) 2025 Miyu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.ski.noophaestus.api.RingBuffer
import at.ski.noophaestus.forge.casting.iota.DisplayIota
import net.minecraft.network.chat.Component
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.time.Duration
import java.time.Instant
import kotlin.math.floor

object ChatHandler {
    fun init() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onServerChat(event: ServerChatEvent) {
        val player = event.player
        val raw = event.message.string  // plain text
        val component = event.message   // Component

        chatLog.add(Message(player.displayName, component, Instant.now()))
    }

    fun getLast(): List<Iota> = chatLog.last()?.intoHex(Instant.now()) ?: listOf(NullIota())

    private val chatLog = RingBuffer<Message>(32)
    private data class Message(val sender: Component, val message: Component, val timestamp: Instant) {
        fun intoHex(now: Instant) = listOf(DisplayIota.createSanitized(sender), DisplayIota.createSanitized(message),
            DoubleIota(floor(Duration.between(now, timestamp).toMillis() / -50.0))
        )
    }
}