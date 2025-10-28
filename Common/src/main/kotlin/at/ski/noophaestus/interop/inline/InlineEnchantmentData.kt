package at.ski.noophaestus.interop.inline

import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.Noophaestus.MODID
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.samsthenerd.inline.api.InlineData
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.Objects.requireNonNull
import java.util.regex.Pattern

class InlineEnchantmentData(enchantments: List<EnchantmentInstance>) : InlineData<InlineEnchantmentData> {
    private val enchantmentData: List<EnchantmentInstance> = enchantments

    override fun getType(): InlineData.InlineDataType<InlineEnchantmentData> {
        return InlineEnchantmentDataType
    }

    override fun getRendererId(): ResourceLocation {
        return Noophaestus.id("enchantment")
    }

    override fun getExtraStyle(): Style {
        return Style.EMPTY
    }

    fun getEnchantments(): List<EnchantmentInstance> {
        return enchantmentData
    }

    object InlineEnchantmentDataType : InlineData.InlineDataType<InlineEnchantmentData> {
        private val ID = ResourceLocation(MODID, "enchantment")
        val ENCHANTMENT_REGEX = "[<(\\[{]\\s*(?:(?:[0-9a-zA-Z._-]+[,!+:; ])?[0-9a-zA-Z._-]+[,!+:; ][0-9]+)(?:\\s*[,!+:; ]\\s*(?:(?:[0-9a-zA-Z._-]+[,!+:; ])?[0-9a-zA-Z._\\/-]+[,!+:; ][0-9]+)*\\s*)*[>)\\]}]|(?:(?:[0-9a-zA-Z._-]+[,!+:; ])?[0-9a-zA-Z._-]+)(?:[,!+:; ][0-9]+)*"

        fun parseEnchantmentPattern(pattern: Pattern, string: String) : List<List<String>> {
            val matcher = pattern.matcher(string)
            val enchantments = mutableListOf<String>()
            val enchantmentLevels = mutableListOf<String>()
            while (matcher.find()) {
                val key = matcher.group(1)
                val value = matcher.group(2)

                enchantments.add(key)
                enchantmentLevels.add(value)
            }

            return enchantments.zip(enchantmentLevels) { enchantment, level -> listOf(enchantment, level) }

        }

        override fun getId(): ResourceLocation {
            return ID
        }

        override fun getCodec(): Codec<InlineEnchantmentData> {
            val enchantmentCodec: Codec<Enchantment> = ResourceLocation.CODEC.xmap(
                { rl -> BuiltInRegistries.ENCHANTMENT.get(rl) },
                { enchantment -> BuiltInRegistries.ENCHANTMENT.getKey(enchantment) }
            )

            val enchantmentInstanceCodec: Codec<EnchantmentInstance> = RecordCodecBuilder.create { instance ->
                instance.group(
                    enchantmentCodec.fieldOf("id").forGetter(EnchantmentInstance::enchantment),
                    Codec.INT.fieldOf("level").forGetter(EnchantmentInstance::level)
                ).apply(instance, ::EnchantmentInstance)
            }

            val listCodec: Codec<List<EnchantmentInstance>> = enchantmentInstanceCodec.listOf()

            return listCodec.xmap(::InlineEnchantmentData) { data -> data.enchantmentData }
        }
    }
}