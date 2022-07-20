package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "HighIQ", ModuleCategory.PLAYER)
class IQ : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Booster", "Disabler", "Editor"), "Booster")
    private val boostValue = IntegerValue("Booster", 666666, 114514, 1919810)
    private val disableAACValue = BoolValue("AAC114514Disabler", false)
    private val disableJZValue = BoolValue("Matrix66666Disabler", false)
    private val editValue = ListValue("EditSavingMode", arrayOf("NMSL", "WDNMD", "LLL", "FW", "SB", "MDZZ", "NT"), "NMSL")
    private val plusValue = BoolValue("Plus", true)

    override val tag: String
        get() = modeValue.get()
}