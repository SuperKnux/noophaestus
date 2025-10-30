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

@Mixin(ItemStack.class)
public class MixinItemStack implements ItemStackAccessor {
    @Shadow
    @Nullable
    private CompoundTag tag;
    @Unique
    private static final String TAG_ENCH_GROUP = "EnchantmentGroups";

    @Definition(id = "tag", field = "Lnet/minecraft/world/item/ItemStack;tag:Lnet/minecraft/nbt/CompoundTag;")
    @Definition(id = "contains", method = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z")
    @Expression("this.tag.contains('Enchantments', 9)")
    @ModifyExpressionValue(method = "isEnchanted", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean noophaestus$isEnchantedFirstBool(boolean original) {
        return original || tag.contains(TAG_ENCH_GROUP, 9);
    }

    @ModifyReturnValue(method = "isEnchanted", at = @At(value = "RETURN", ordinal = 0))
    private boolean noophaestus$isEnchantedModifyReturn(boolean original) {
        return original || !tag.getList(TAG_ENCH_GROUP, 10).isEmpty();
    }

    @Override
    public @NotNull ListTag noophaestus_getEnchantmentGroupTags() {
        return tag != null ? tag.getList(TAG_ENCH_GROUP, CompoundTag.TAG_COMPOUND) : new ListTag();
    }
}
