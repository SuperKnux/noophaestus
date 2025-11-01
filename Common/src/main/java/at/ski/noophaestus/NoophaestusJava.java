package at.ski.noophaestus;

import at.ski.noophaestus.api.item.ItemStackAccessor;
import net.darkhax.bookshelf.api.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static at.ski.noophaestus.casting.iota.EnchantmentGroupIota.*;
import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId;

public class NoophaestusJava {
    public static void init() {
        Services.EVENTS.addItemTooltipListener(NoophaestusJava::addEnchantmentGroupTooltips);
    }

    public static void addEnchantmentGroupTooltips(ItemStack stack, List<Component> tooltip, TooltipFlag tooltipFlag) {
        Set<Enchantment> enchantments = EnchantmentHelper.getEnchantments(stack).keySet();
        if (enchantments.isEmpty()) return;

        ListTag enchantmentGroups = ((ItemStackAccessor) (Object) stack).noophaestus_getEnchantmentGroupTags();
        if (enchantmentGroups.isEmpty()) return;
        int lastIdx = -1;

        for (int idx = 0; idx < tooltip.size(); idx++) {
            ComponentContents component = tooltip.get(idx).getContents();
            if (!
                    (component instanceof TranslatableContents &&
                            enchantments.stream().anyMatch(enchantment -> Objects.equals(((TranslatableContents) component).getKey(), enchantment.getDescriptionId()))
            )) {
                lastIdx = 0;
                break;
            }
            lastIdx = idx;
        }

        for (int i = 0; i < enchantmentGroups.size(); i++) {
            MutableComponent finalComponent = Component.literal(((CompoundTag) enchantmentGroups.get(i)).getString(TAG_ENCH_GROUP_NAME)).withStyle(style -> style.withColor(0xcb00f5).withBold(true));
            ListTag enchantmentGroupTags = ((CompoundTag) enchantmentGroups.get(i)).getList(TAG_ENCH_GROUP_TAGS, 10);
            tooltip.add(lastIdx + i + 1, finalComponent);

            if (Screen.hasShiftDown()) {
                for (Tag enchantment : enchantmentGroupTags) {
                    CompoundTag enchantmentTag = (CompoundTag) enchantment;
                    MutableComponent enchantmentComponent;
                    if (enchantmentTag.getBoolean(TAG_ENCH_GROUP_IS_ACTIVE)) {
                        enchantmentComponent = Component.literal("->").append(StringUtils.repeat(" ", 1)).append(BuiltInRegistries.ENCHANTMENT.getOptional(getEnchantmentId(enchantmentTag)).get().getFullname(enchantmentTag.getInt("lvl")));
                    } else {
                        enchantmentComponent = Component.literal("").append(StringUtils.repeat(" ", 3)).append(BuiltInRegistries.ENCHANTMENT.getOptional(getEnchantmentId(enchantmentTag)).get().getFullname(enchantmentTag.getInt("lvl"))).withStyle(style -> style.withColor(0x555555));
                    }

                    tooltip.add(lastIdx + i + 2, enchantmentComponent);
                }
            } else {
                tooltip.add(lastIdx + i + 2, Component.literal("..."));
            }
        }
    }


}
