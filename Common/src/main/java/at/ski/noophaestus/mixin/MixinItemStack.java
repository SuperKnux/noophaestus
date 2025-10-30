package at.ski.noophaestus.mixin;


import at.ski.noophaestus.api.item.ItemStackAccessor;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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

    @Inject(method = "isEnchanted", at = @At("RETURN"), cancellable = true)
    private void noophaestus$isEnchantedInjectReturn(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            if (tag != null && tag.contains(TAG_ENCH_GROUP, 9)) {
                cir.setReturnValue(!tag.getList(TAG_ENCH_GROUP, 10).isEmpty());
            }
        }
    }

    @Override
    public @NotNull ListTag noophaestus_getEnchantmentGroupTags() {
        return tag != null ? tag.getList(TAG_ENCH_GROUP, CompoundTag.TAG_COMPOUND) : new ListTag();
    }
}
