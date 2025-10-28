package at.ski.noophaestus.api.item

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.ski.noophaestus.features.enchanting.EnchantmentGroup
import net.minecraft.nbt.*
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentInstance
import ram.talia.moreiotas.api.asActionResult


object ItemStackUtils {

    fun ItemStack.noophaestus_enchantmentGroupTags(): ListTag =
        (this as ItemStackAccessor).noophaestus_getEnchantmentGroupTags()

    fun noophaestus_recursivePrint(obj: Any?, indent: Int = 0, env: CastingEnvironment, visited: MutableSet<Any?> = mutableSetOf()) {
        val indentStr = " ".repeat(indent)

        when (obj) {
            null -> {
                env.printMessage(NullIota().display())
                return
            }
            is Tag -> {
                when (obj) {
                    is CompoundTag -> {
                        env.printMessage(Component.literal("${indentStr}CompoundTag {"))
                        for (key in obj.allKeys) {
                            val value = obj.get(key)
                            env.printMessage(Component.literal("${indentStr}  $key:"))
                            noophaestus_recursivePrint(value, indent + 4, env, visited)
                        }
                        env.printMessage(Component.literal("${indentStr}}"))
                    }
                    is ListTag -> {
                        env.printMessage(Component.literal("${indentStr}ListTag[size=${obj.size}] ["))
                        for (i in 0 until obj.size) {
                            env.printMessage(Component.literal("${indentStr}  [$i]:"))
                            noophaestus_recursivePrint(obj[i], indent + 4, env, visited)
                        }
                        env.printMessage(Component.literal("${indentStr}]"))
                    }
                    is StringTag -> env.printMessage(Component.literal("${indentStr}\"${obj.asString}\""))
                    is IntTag -> env.printMessage(Component.literal("${indentStr}${obj.asInt}"))
                    is LongTag -> env.printMessage(Component.literal("${indentStr}${obj.asLong}"))
                    is ShortTag -> env.printMessage(Component.literal("${indentStr}${obj.asShort}"))
                    is ByteTag -> env.printMessage(Component.literal("${indentStr}${obj.asByte}"))
                    is FloatTag -> env.printMessage(Component.literal("${indentStr}${obj.asFloat}"))
                    is DoubleTag -> env.printMessage(Component.literal("${indentStr}${obj.asDouble}"))
                    is IntArrayTag -> env.printMessage(Component.literal("${indentStr}IntArrayTag[len=${obj.asIntArray.size}]"))
                    is LongArrayTag -> env.printMessage(Component.literal("${indentStr}LongArrayTag[len=${obj.asLongArray.size}]"))
                    is ByteArrayTag -> env.printMessage(Component.literal("${indentStr}ByteArrayTag[len=${obj.asByteArray.size}]"))
                    else -> env.printMessage(Component.literal("${indentStr}${obj.type.name}"))
                }
                return
            }
            is EnchantmentGroup -> {
                env.printMessage(Component.literal("${indentStr}EnchantmentGroup(name=${obj.name.string}) {"))
                obj.enchantments.forEachIndexed { idx, (instance, active) ->
                    val enchKey = net.minecraft.core.registries.BuiltInRegistries.ENCHANTMENT.getKey(instance.enchantment)
                    env.printMessage(Component.literal("${indentStr}  [$idx] id=$enchKey lvl=${instance.level} active=$active"))
                }
                env.printMessage(Component.literal("${indentStr}}"))
                return
            }
            is Pair<*, *> -> {
                env.printMessage(Component.literal("${indentStr}Pair("))
                env.printMessage(Component.literal("${indentStr}  first:"))
                noophaestus_recursivePrint(obj.first, indent + 4, env, visited)
                env.printMessage(Component.literal("${indentStr}  second:"))
                noophaestus_recursivePrint(obj.second, indent + 4, env, visited)
                env.printMessage(Component.literal("${indentStr})"))
                return
            }
            is Collection<*> -> {
                env.printMessage(Component.literal("${indentStr}Collection[size=${obj.size}] ["))
                obj.forEachIndexed { i, e ->
                    env.printMessage(Component.literal("${indentStr}  [$i]:"))
                    noophaestus_recursivePrint(e, indent + 4, env, visited)
                }
                env.printMessage(Component.literal("${indentStr}]"))
                return
            }
            is String, is Boolean, is Number -> {
                env.printMessage(Component.literal("${indentStr}$obj"))
                return
            }
            else -> {
                // Fallback: just toString to avoid reflection issues
                env.printMessage(Component.literal("${indentStr}${obj::class.simpleName}: $obj"))
                return
            }
        }
    }
    // ... existing code ...
}