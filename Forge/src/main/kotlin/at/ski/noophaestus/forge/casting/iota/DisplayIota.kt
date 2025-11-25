package at.ski.noophaestus.forge.casting.iota

// https://github.com/miyucomics/hexpose/blob/main/src/main/java/miyucomics/hexpose/iotas/DisplayIota.kt
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
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asCompound
import at.ski.noophaestus.api.component.sanitize
import net.minecraft.ChatFormatting
import net.minecraft.locale.Language
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.LiteralContents
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.server.level.ServerLevel

class DisplayIota(text: Component) : Iota(TYPE, text) {
    override fun isTruthy() = true
    val text = this.payload as Component
    override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is DisplayIota) && this.text == that.text

    fun getRoot() = this.text.getRoot()

    fun modifyRootBuilder(modifier: (StringBuilder) -> StringBuilder): DisplayIota {
        val builder = StringBuilder(getRoot())
        modifier(builder)
        return getWithNewRoot(builder.toString())
    }

    fun modifyRootString(modifier: (StringBuilder) -> String): DisplayIota {
        val builder = modifier(StringBuilder(getRoot()))
        return getWithNewRoot(builder)
    }

    fun getChildren(): List<Component> = this.text.siblings

    fun getWithNewRoot(root: String): DisplayIota {
        val result = MutableComponent.create(LiteralContents(root))
        result.style = this.text.style
        result.siblings.clear()
        result.siblings.addAll(this.text.siblings.map(Component::copy))
        return DisplayIota(result)
    }

    fun getWithNewChildren(children: List<Component>): Component {
        return this.text.copy().also {
            it.siblings.clear()
            it.siblings.addAll(children)
        }
    }

    override fun serialize(): Tag {
        val serialized = Component.Serializer.toJson(text)
        if (serialized.length > 32000)
            return CompoundTag()
        return CompoundTag().also { it.putString("text", serialized) }
    }

    companion object {
        var TYPE: IotaType<DisplayIota> = object : IotaType<DisplayIota>() {
            override fun color() = 0xff_db3f30.toInt()
            override fun display(tag: Tag): Component {
                if (!tag.asCompound.contains("text"))
                    return Component.literal("arimfexendrapuse").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.OBFUSCATED)
                return Component.Serializer.fromJson((tag as CompoundTag).getString("text"))!!
            }

            override fun deserialize(
                tag: Tag,
                world: ServerLevel
            ): DisplayIota? {
                if (!tag.asCompound.contains("text"))
                    return null
                return DisplayIota(Component.Serializer.fromJson((tag as CompoundTag).getString("text"))!!.withStyle(ChatFormatting.RESET))
            }
        }

        fun createSanitized(text: Component) = DisplayIota(text.sanitize())
    }
}

inline val Component.asActionResult get() = listOf(DisplayIota.createSanitized(this))

fun List<Iota>.getDisplay(idx: Int, argc: Int = 0): DisplayIota {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is DisplayIota)
        return x
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "display")
}

fun Component.getRoot(): String {
    return when (val content = this.contents) {
        is LiteralContents -> content.text
        is TranslatableContents -> String.format(Language.getInstance().getOrDefault(content.key), content.args)
        else -> "arimfexendrapuse"
    }
}