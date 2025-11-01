package at.ski.noophaestus.util;

import at.ski.noophaestus.api.item.ItemStackAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static at.ski.noophaestus.casting.iota.EnchantmentGroupIota.TAG_ENCH_GROUP_IS_ACTIVE;
import static at.ski.noophaestus.casting.iota.EnchantmentGroupIota.TAG_ENCH_GROUP_TAGS;
import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId;
import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentLevel;

public final class ItemStackUtils {

    public static void addEnchantmentGroups(EnchantmentHelper.EnchantmentVisitor visitor, ItemStack stack, CallbackInfo ci) {

        ListTag groupTag = ((ItemStackAccessor) (Object) stack).noophaestus_getEnchantmentGroupTags();

        for(int i = 0; i < groupTag.size(); ++i) {
            CompoundTag compoundTag = groupTag.getCompound(i);
            ListTag enchantmentsTag = compoundTag.getList(TAG_ENCH_GROUP_TAGS, 10);
            for (int j = 0; j < enchantmentsTag.size(); ++j) {
                if (!enchantmentsTag.getCompound(j).getBoolean(TAG_ENCH_GROUP_IS_ACTIVE)) continue;
                int finalJ = j;
                BuiltInRegistries.ENCHANTMENT.getOptional(getEnchantmentId(enchantmentsTag.getCompound(j))).ifPresent((enchantment) -> visitor.accept(enchantment, getEnchantmentLevel(enchantmentsTag.getCompound(finalJ))));
            }
        }
    }

    public static int setEnchantmentGroups(int original, Enchantment enchantment, ItemStack stack) {
        ResourceLocation resourceLocation = getEnchantmentId(enchantment);
        ListTag groupTag = ((ItemStackAccessor) (Object) stack).noophaestus_getEnchantmentGroupTags();

        for (int i = 0; i < groupTag.size(); ++i) {
            ListTag enchantmentsTag = groupTag.getCompound(i).getList(TAG_ENCH_GROUP_TAGS, 10);
            for (int j = 0; j < enchantmentsTag.size(); ++j) {
                CompoundTag compoundTag = enchantmentsTag.getCompound(j);
                ResourceLocation resourceLocation2 = getEnchantmentId(compoundTag);
                if (resourceLocation2 != null && resourceLocation2.equals(resourceLocation)) {
                    return getEnchantmentLevel(compoundTag);
                }
            }
        }

        return original;
    }


    private static void addActiveEnchantmentsFromGroups(ListTag original, ListTag groupTag) {
        for (int i = 0; i < groupTag.size(); i++) {
            ListTag enchantmentsTag = groupTag.getCompound(i).getList(TAG_ENCH_GROUP_TAGS, 10);
            if (enchantmentsTag.isEmpty()) {
                return;
            }
            addActiveEnchantments(original, enchantmentsTag);
        }
    }


    private static void addActiveEnchantments(ListTag original, ListTag enchantmentsTag) {
        for (int j = 0; j < enchantmentsTag.size(); j++) {
            CompoundTag enchantment = enchantmentsTag.getCompound(j);
            if (enchantment.getBoolean(TAG_ENCH_GROUP_IS_ACTIVE)) {
                original.add(enchantment);
            }
        }
    }

    public static ListTag getEnchantmentTagsAddGroups(ItemStack stack, ListTag original) {
        ListTag groupTag = ((ItemStackAccessor) (Object) stack).noophaestus_getEnchantmentGroupTags();
        if (groupTag.isEmpty()) {
            return original;
        }

        addActiveEnchantmentsFromGroups(original, groupTag);
        return original;
    }

}
