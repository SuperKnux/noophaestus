package at.ski.noophaestus.interop.inline

import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.features.enchanting.EnchantmentGroup
import com.mojang.serialization.Codec
import com.samsthenerd.inline.api.InlineData
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation

class InlineEnchantmentGroupData(enchantmentGroup: EnchantmentGroup) : InlineData<InlineEnchantmentGroupData> {
    val enchantmentGroupData = enchantmentGroup
    override fun getType(): InlineData.InlineDataType<InlineEnchantmentGroupData> {
        return InlineEnchantmentGroupDataType
    }

    override fun getRendererId(): ResourceLocation {
        return Noophaestus.id("enchantment")
    }

    override fun getExtraStyle(): Style {
        return Style.EMPTY
    }

    object InlineEnchantmentGroupDataType : InlineData.InlineDataType<InlineEnchantmentGroupData> {
        private val ID = Noophaestus.id("enchantment_group")
        override fun getId(): ResourceLocation {
            return ID
        }

        override fun getCodec(): Codec<InlineEnchantmentGroupData> {
            return EnchantmentGroup.CODEC.xmap(::InlineEnchantmentGroupData) { it.enchantmentGroupData }
        }
    }
}