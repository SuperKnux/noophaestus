package at.ski.noophaestus.features.enchanting

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

/**
 * Fluent builder/editor for EnchantmentGroup.
 * - Ensures at least one enchantment exists
 * - Ensures exactly one active enchantment (defaults first to active)
 */
class EnchGroupBuilder private constructor(
    private var name: MutableComponent,
    private val entries: MutableList<Pair<EnchantmentInstance, Boolean>>
) {

    // region Construction

    companion object {
        fun create(name: String): EnchGroupBuilder =
            EnchGroupBuilder(Component.literal(name).copy(), mutableListOf())

        fun create(name: MutableComponent): EnchGroupBuilder =
            EnchGroupBuilder(name.copy(), mutableListOf())

        fun from(group: EnchantmentGroup): EnchGroupBuilder =
            EnchGroupBuilder(group.name.copy(), group.enchantments.toMutableList())
    }

    // endregion

    // region Mutators

    fun name(newName: String): EnchGroupBuilder {
        this.name = Component.literal(newName).copy()
        return this
    }

    fun name(newName: MutableComponent): EnchGroupBuilder {
        this.name = newName.copy()
        return this
    }

    fun add(enchantment: Enchantment, level: Int, active: Boolean = false): EnchGroupBuilder {
        entries.add(EnchantmentInstance(enchantment, level) to active)
        return this
    }

    fun add(instance: EnchantmentInstance, active: Boolean = false): EnchGroupBuilder {
        entries.add(instance to active)
        return this
    }

    fun removeAt(index: Int): EnchGroupBuilder {
        require(index in entries.indices) { "Index $index out of bounds" }
        entries.removeAt(index)
        return this
    }

    fun clear(): EnchGroupBuilder {
        entries.clear()
        return this
    }

    fun setLevel(index: Int, level: Int): EnchGroupBuilder {
        require(index in entries.indices) { "Index $index out of bounds" }
        val (inst, active) = entries[index]
        entries[index] = EnchantmentInstance(inst.enchantment, level) to active
        return this
    }

    fun setActive(index: Int): EnchGroupBuilder {
        require(index in entries.indices) { "Index $index out of bounds" }
        for (i in entries.indices) {
            val (inst, _) = entries[i]
            entries[i] = inst to (i == index)
        }
        return this
    }

    // Convenience: set active by predicate
    fun setActiveIf(predicate: (EnchantmentInstance) -> Boolean): EnchGroupBuilder {
        val idx = entries.indexOfFirst { predicate(it.first) }
        require(idx >= 0) { "No enchantment matched predicate" }
        return setActive(idx)
    }
    
    //  Grab first enchantment; if it was active, set first enchantment to active if possible
    fun pop(): Pair<EnchantmentInstance?, EnchGroupBuilder> {
        val entry = entries.removeFirstOrNull()
        var inst : EnchantmentInstance? = null
        var active : Boolean = false
        if (entry != null) {
            inst = entry.first; active = entry.second
            if (active) {
                entries[0] = entries[0].first to true
            }
        } else {
            inst = null
        }

        return inst to this
    }
    
    //  Grab last enchantment; if it was active, set first enchantment to active if possible
    fun popLast(): Pair<EnchantmentInstance?, EnchGroupBuilder> {
        val entry = entries.removeLastOrNull()
        var inst: EnchantmentInstance? = null
        var active = false
        if (entry != null) {
            inst = entry.first; active = entry.second
            if (active && entries.isNotEmpty()) {
                entries[0] = entries[0].first to true
            }
        }
        return inst to this
    }

    // endregion

    // region Queries

    fun size(): Int = entries.size

    fun get(index: Int): Pair<EnchantmentInstance, Boolean> = entries[index]

    // endregion

    // region Build

    /**
     * Validates invariants:
     * - At least one enchantment
     * - Exactly one active; if none active, first becomes active
     */
    fun build(): EnchantmentGroup {
        require(entries.isNotEmpty()) { "EnchantmentGroup must contain at least one enchantment" }

        val activeCount = entries.count { it.second }
        val normalized = when {
            activeCount == 1 -> entries
            activeCount == 0 -> entries.mapIndexed { i, (inst, _) -> inst to (i == 0) }.toMutableList()
            else -> {
                // keep the first active, disable the rest
                var seen = false
                entries.map { (inst, active) ->
                    val nowActive = active && !seen
                    if (active && !seen) seen = true
                    inst to nowActive
                }.toMutableList()
            }
        }

        return EnchantmentGroup(name.copy(), normalized)
    }

    // endregion
}