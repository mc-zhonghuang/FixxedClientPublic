package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.block.BlockUtils.getBlock
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.BlockSlime
import java.util.*

@ModuleInfo(name = "SlimeJump", category = ModuleCategory.MOVEMENT)
class SlimeJump : Module() {
    private val motionValue = FloatValue("Motion", 0.42f, 0.2f, 1f)
    private val modeValue = ListValue("Mode", arrayOf("Set", "Add"), "Add")
    @EventTarget
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null && mc.theWorld != null && getBlock(mc.thePlayer.position.down()) is BlockSlime) {
            event.cancelEvent()
            when (modeValue.get().lowercase(Locale.getDefault())) {
                "set" -> mc.thePlayer.motionY = motionValue.get().toDouble()
                "add" -> mc.thePlayer.motionY += motionValue.get().toDouble()
            }
        }
    }
}