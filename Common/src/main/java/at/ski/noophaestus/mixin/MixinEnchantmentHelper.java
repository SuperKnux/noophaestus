package at.ski.noophaestus.mixin;

import static at.ski.noophaestus.casting.iota.EnchantmentGroupIota.*;
import static at.ski.noophaestus.casting.iota.EnchantmentIota.*;
import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId;
import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentLevel;

import at.ski.noophaestus.api.item.ItemStackAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;


@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
    @ModifyReturnValue(method = "getEnchantments", at = @At("RETURN"))
    private static Map<Enchantment, Integer> noophaestus$returnZeroIfEmpty(Map<Enchantment, Integer> original, @Local(argsOnly = true) ItemStack stack) {
        CompoundTag tag = stack.getTag();
        ListTag enchantmentGroups = new ListTag();
        if (tag.contains("EnchantmentGroups", 9)) {
            enchantmentGroups = tag.getList("EnchantmentGroups", 10);
            if (enchantmentGroups.isEmpty()) {
                return original;
            }
        }

        noophaestus$readAllGroupsAndMerge(enchantmentGroups, original);

        return original;
    }









    @Inject(method = "runIterationOnItem", at = @At("TAIL"))
    private static void noophaestus$mergeEnchantmentGroups(EnchantmentHelper.EnchantmentVisitor visitor, ItemStack stack, CallbackInfo ci) {

        ListTag groupTag = ((ItemStackAccessor) (Object) stack).noophaestus_getEnchantmentGroupTags();

        for(int i = 0; i < groupTag.size(); ++i) {
            CompoundTag compoundTag = groupTag.getCompound(i);
            ListTag enchantmentsTag = compoundTag.getList(TAG_ENCH_GROUP_TAGS, 10);
            for (int j = 0; j < enchantmentsTag.size(); ++j) {
                if (!enchantmentsTag.getCompound(j).getBoolean(TAG_ENCH_GROUP_IS_ACTIVE)) continue;
                int finalJ = j;
                System.out.println(finalJ);
                BuiltInRegistries.ENCHANTMENT.getOptional(getEnchantmentId(enchantmentsTag.getCompound(j))).ifPresent((enchantment) -> visitor.accept(enchantment, getEnchantmentLevel(enchantmentsTag.getCompound(finalJ))));
            }
        }
    }

    @ModifyReturnValue(method = "getItemEnchantmentLevel", at = @At("RETURN"))
    private static int noophaestus$getItemEnchantmentLevel(int original, @Local(argsOnly = true) Enchantment enchantment, @Local(argsOnly = true) ItemStack stack) {
        ResourceLocation resourceLocation = getEnchantmentId(enchantment);
        ListTag groupTag = ((ItemStackAccessor) (Object) stack).noophaestus_getEnchantmentGroupTags();

        for (int i = 0; i < groupTag.size(); ++i) {
            CompoundTag compoundTag = groupTag.getCompound(i);
            ResourceLocation resourceLocation2 = getEnchantmentId(compoundTag);
            if (resourceLocation2 != null && resourceLocation2.equals(resourceLocation)) {
                return getEnchantmentLevel(compoundTag);
            }
        }
    
        return original;
    }

    @Unique
    private static void noophaestus$readAllGroupsAndMerge(ListTag list, Map<Enchantment, Integer> out) {
        for (int i = 0; i < list.size(); i++) {
            CompoundTag groupOrEntry = list.getCompound(i);

            // If this compound holds its own entries list
            if (groupOrEntry.contains(TAG_ENCH_GROUP_TAGS, ListTag.TAG_LIST)) {
                ListTag entries = groupOrEntry.getList(TAG_ENCH_GROUP_TAGS, ListTag.TAG_COMPOUND);
                noophaestus$extractAndMergeActiveEntries(entries, out);
            } else {
                // Treat the compound itself as a single entry
                noophaestus$extractAndMergeActiveEntry(groupOrEntry, out);
            }
        }
    }

    @Unique
    private static void noophaestus$extractAndMergeActiveEntries(ListTag entries, Map<Enchantment, Integer> out) {
        for (int j = 0; j < entries.size(); j++) {
            noophaestus$extractAndMergeActiveEntry(entries.getCompound(j), out);
        }
    }

    @Unique
    private static void noophaestus$extractAndMergeActiveEntry(CompoundTag entry, Map<Enchantment, Integer> out) {
        if (!entry.getBoolean(TAG_ENCH_GROUP_IS_ACTIVE)) return;

        String id;
        int lvl;

        id = entry.getString(TAG_ID);
        lvl = entry.contains(TAG_LEVEL) ? entry.getInt(TAG_LEVEL) : entry.getShort(TAG_LEVEL);

        Enchantment ench = BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(id));
        if (ench == null) return;

        out.merge(ench, lvl, Math::max);
    }

}
