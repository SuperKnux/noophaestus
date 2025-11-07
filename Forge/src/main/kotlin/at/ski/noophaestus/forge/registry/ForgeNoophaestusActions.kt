package at.ski.noophaestus.forge.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.ski.noophaestus.forge.casting.actions.display.OpCompareStyles
import at.ski.noophaestus.forge.casting.actions.display.OpDisintegrateDisplay
import at.ski.noophaestus.forge.casting.actions.display.OpParseDisplay
import at.ski.noophaestus.forge.casting.actions.display.OpSplitDisplay
import at.ski.noophaestus.forge.casting.actions.display.chat.OpGetMessage
import at.ski.noophaestus.forge.casting.actions.display.style.*
import at.ski.noophaestus.registry.NoophaestusActions
import at.ski.noophaestus.registry.NoophaestusRegistrar
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation

object ForgeNoophaestusActions : NoophaestusRegistrar<ActionRegistryEntry>(
    HexRegistries.ACTION,
    { HexActions.REGISTRY },
) {
    val CREATE_DISPLAY = make("create_display", "awaqeeeee", HexDir.SOUTH_WEST, OpCreateDisplay)
    val DISPLAY_CHILDREN = make("display_children", "dwdeqqqqq", HexDir.SOUTH_EAST, OpDisplayChildren)
    val DISPLAY_COLOR = make("display_color", "awaqeeeeewded", HexDir.SOUTH_WEST, OpDisplayColor)
    val DISPLAY_BOLD = make("display_bold", "awaqeeeeedd", HexDir.SOUTH_WEST, OpDisplayBoolean(Style::bold, Style::withBold))
    val DISPLAY_ITALICS = make("display_italics", "awaqeeeeede", HexDir.SOUTH_WEST, OpDisplayBoolean(Style::italic, Style::withItalic))
    val DISPLAY_UNDERLINE = make("display_underline", "awaqeeeeedw", HexDir.SOUTH_WEST, OpDisplayBoolean(Style::underlined, Style::withUnderlined))
    val DISPLAY_STRIKETHROUGH = make("display_strikethrough", "awaqeeeeedq", HexDir.SOUTH_WEST, OpDisplayBoolean(Style::strikethrough, Style::withStrikethrough))
    val DISPLAY_OBFUSCATED = make("display_obfuscated", "awaqeeeeeda", HexDir.SOUTH_WEST, OpDisplayBoolean(Style::obfuscated, Style::withObfuscated))
    val DISPLAY_FONT = make("display_font", "awaqeeeeedaqa", HexDir.SOUTH_WEST, OpDisplayFont)

    val COMPARE_STYLE = make("compare_style", "dwdeqqqqqdda", HexDir.SOUTH_EAST, OpCompareStyles)
    val PARSE_DISPAY = make("parse_display", "dwdewqqqwqqaeq", HexDir.SOUTH_EAST, OpParseDisplay)
    val SPLIT_DISPLAY = make("split_display", "dwdeqqqwqqqqae", HexDir.SOUTH_EAST, OpSplitDisplay)
    val DISINTEGRATE_DISPAY = make("disintegrate_display", "dwdeqqqqqdeee", HexDir.SOUTH_EAST, OpDisintegrateDisplay)

    val GET_MESSAGE = make("get_message", "aeeedw", HexDir.SOUTH_WEST, OpGetMessage)
    private fun make(name: String, signature: String, startDir: HexDir, action: Action) =
        make(name, startDir, signature) { action }

    private fun make(name: String, startDir: HexDir, signature: String, getAction: () -> Action) =
        NoophaestusActions.register(ResourceLocation("hexpose", name)) {
            ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), getAction())
        }
}

