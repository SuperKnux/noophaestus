package at.ski.noophaestus

import at.petrak.hexcasting.api.utils.asCompound
import at.ski.noophaestus.api.item.ItemStackAccessor
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota.Companion.TAG_ENCH_GROUP_NAME
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota.Companion.TAG_ENCH_GROUP_TAGS
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import at.ski.noophaestus.config.NoophaestusServerConfig
import at.ski.noophaestus.interop.inline.InlineNoophaestus
import at.ski.noophaestus.networking.NoophaestusNetworking
import at.ski.noophaestus.registry.NoophaestusActions
import at.ski.noophaestus.registry.NoophaestusArithmetics
import at.ski.noophaestus.registry.NoophaestusIotaTypes
import net.darkhax.bookshelf.api.Services
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId
import org.apache.commons.lang3.StringUtils


object Noophaestus {
    const val MODID = "noophaestus"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        NoophaestusServerConfig.init()
        initRegistries(
            NoophaestusActions,
            NoophaestusIotaTypes,
            NoophaestusArithmetics,
        )
        NoophaestusNetworking.init()
        InlineNoophaestus.init()
        NoophaestusJava.init()
    }



    fun initServer() {
        NoophaestusServerConfig.initServer()
    }
}
