package net.ccbluex.liquidbounce.features.module.modules.unstable

import net.ccbluex.liquidbounce.LiquidBounce.moduleManager
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker.find
import net.ccbluex.liquidbounce.utils.ClientUtils2
import net.ccbluex.liquidbounce.utils.block.BlockUtils.getBlock
import net.ccbluex.liquidbounce.utils.block.BlockUtils.getCenterDistance
import net.minecraft.block.Block
import net.minecraft.util.BlockPos

@ModuleInfo(name = "NoFucker", category = ModuleCategory.UNSTABLE)
class NoFucker : Module() {
    var pos: BlockPos? = null
    var BedPos: BlockPos? = null
    var Change = true
    var msg = true
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (pos == null || Block.getIdFromBlock(getBlock(pos)) != 26 || getCenterDistance(pos!!) > 4) {
            pos = find(26)
        }
        if (pos != null) {
            if (Change) {
                BedPos = pos
                ClientUtils2.displayChatMessage("[NoFucker] 为你锁定床的坐标 : $BedPos")
                Change = false
            }
        }
        if (BedPos != null) {
            if (Math.abs(BlockPos(mc.thePlayer).x - BedPos!!.x) >= 4) {
                if (pos != null) {
                    moduleManager.getModule(Fucker::class.java)!!.state = getCenterDistance(pos!!) < 3.6
                } else {
                    moduleManager.getModule(Fucker::class.java)!!.state = false
                }
            } else {
                moduleManager.getModule(Fucker::class.java)!!.state = false
            }
        }
        if (pos == null) {
            moduleManager.getModule(Fucker::class.java)!!.state = false
        }
    }
}