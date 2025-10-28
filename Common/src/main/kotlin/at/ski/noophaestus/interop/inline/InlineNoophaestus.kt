package at.ski.noophaestus.interop.inline

import com.samsthenerd.inline.api.InlineAPI

object InlineNoophaestus {
    fun init() {
        InlineAPI.INSTANCE.addDataType(InlineEnchantmentData.InlineEnchantmentDataType)
        InlineAPI.INSTANCE.addDataType(InlineEnchantmentGroupData.InlineEnchantmentGroupDataType)
    }
}