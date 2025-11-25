package at.ski.noophaestus.api.component

// https://github.com/miyucomics/hexpose/blob/main/src/main/java/miyucomics/hexpose/utils/TextUtils.kt
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
import net.minecraft.locale.Language
import net.minecraft.network.chat.*
import net.minecraft.network.chat.contents.*


object ComponentUtils {
    fun split(text: Component) : MutableList<Component> {
        val chars = mutableListOf<Component>()
        collectStyledCharacters(text, text.style, chars)
        return chars
    }

    private fun collectStyledCharacters(text: Component, parentStyle: Style, out: MutableList<Component>) {
        val effectiveStyle = text.style.applyTo(parentStyle)
        val content = text.contents
        if (content is LiteralContents)
            content.text.forEach { out += Component.literal(it.toString()).withStyle(effectiveStyle) }
        for (child in text.siblings)
            collectStyledCharacters(child, effectiveStyle, out)
    }
}

fun Component.sanitize() : Component {
    val sanitizedRoot: MutableComponent = when (val content = this.contents) {
        is LiteralContents -> Component.literal(content.text)
        is TranslatableContents -> {
            val pattern = Language.getInstance().getOrDefault(content.key)
            val args = content.args.map { arg ->
                when (arg) {
                    is Component -> arg.sanitize().string
                    else -> arg.toString()
                }
            }.toTypedArray()
            Component.literal(String.format(pattern, *args))
        }
        else -> Component.literal("arimfexendrapuse")
    }

    sanitizedRoot.style = this.style
        .withClickEvent(null)
        .withHoverEvent(null)
        .withInsertion(null)

    for (child in this.siblings) {
        sanitizedRoot.append(child.sanitize())
    }

    return sanitizedRoot
}