package at.ski.noophaestus.interop.inline

import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.interop.inline.InlineEnchantmentData.InlineEnchantmentDataType
import com.samsthenerd.inline.api.client.InlineClientAPI
import com.samsthenerd.inline.api.data.ItemInlineData
import com.samsthenerd.inline.api.matching.InlineMatch
import com.samsthenerd.inline.api.matching.MatcherInfo
import com.samsthenerd.inline.api.matching.RegexMatcher
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.regex.Pattern

object InlineNoophaestusClient {
    fun initClient() {
        val enchantmentMatcherID = Noophaestus.id("enchantment")
        InlineClientAPI.INSTANCE.addMatcher(RegexMatcher.Standard("enchantment", InlineEnchantmentDataType.ENCHANTMENT_REGEX, enchantmentMatcherID, {
            enchantmentListString: String ->
            val enchantmentListStringLowercase : String = enchantmentListString.lowercase()

            val regexPatternString = "([0-9a-zA-Z._\\/:-]+)[,!+:; ]([0-9]+)"
            val regexPattern = Pattern.compile(regexPatternString)

            val returnedEnchantments = InlineEnchantmentDataType.parseEnchantmentPattern(regexPattern, enchantmentListStringLowercase)
            val enchantmentList = mutableListOf<EnchantmentInstance>()

            for (enchantment in returnedEnchantments) {
                var enchantmentName = enchantment[0]
                val enchantmentLevel = enchantment[1].toInt()

                if (!enchantmentName.contains(":")) {
                    enchantmentName = "minecraft:$enchantmentName"
                }
                val enchantmentRL = ResourceLocation(enchantmentName)

                if (!BuiltInRegistries.ENCHANTMENT.containsKey(enchantmentRL)) {
                    return@Standard null
                }
                val enchantmentInstance = EnchantmentInstance(BuiltInRegistries.ENCHANTMENT.get(enchantmentRL), enchantmentLevel)
                enchantmentList.add(enchantmentInstance)
            }
            val enchantmentBookStack = BuiltInRegistries.ITEM.get(ResourceLocation("minecraft:enchanted_book")).defaultInstance
            for (enchantment in enchantmentList) {
                enchantmentBookStack.enchant(enchantment.enchantment, enchantment.level)
            }
            val he = HoverEvent(HoverEvent.Action.SHOW_ITEM,
                HoverEvent.ItemStackInfo(enchantmentBookStack)
            )
            InlineMatch.DataMatch(ItemInlineData(enchantmentBookStack), Style.EMPTY.withHoverEvent(he))
        }, MatcherInfo.fromId(enchantmentMatcherID)))
    }


}