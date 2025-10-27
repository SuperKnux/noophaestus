package at.ski.noophaestus.mixin;


import at.ski.noophaestus.api.item.ItemStackAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class MixinItemStack implements ItemStackAccessor {
    @Shadow
    @Nullable
    private CompoundTag tag;
    @Unique
    private static final String TAG_ENCH_GROUP = "EnchantmentGroups";

    @Inject(
            method = "save",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/CompoundTag;copy()Lnet/minecraft/nbt/CompoundTag;", shift = At.Shift.AFTER)
    )
    private void noophaestus$saveEnchantmentGroups(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir, @Local(argsOnly = true) CompoundTag tag) {
        CompoundTag compound = new CompoundTag();
        compoundTag.getCompound("tag").put(TAG_ENCH_GROUP, new ListTag());
    }

    @Override
    public @NotNull ListTag noophaestus$getEnchantmentGroupTags() {
        return tag != null ? tag.getList(TAG_ENCH_GROUP, CompoundTag.TAG_COMPOUND) : new ListTag();
    }
}
