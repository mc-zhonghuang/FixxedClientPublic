package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Tick

@ModuleInfo(name = "AntiLagBack", category = ModuleCategory.MOVEMENT)

class AntiLagBack : Module() {
    private final val ticks: Int = 0
    override fun onEnable() {
        var ticks: Int = 0
    }
    fun onUpdate(event: UpdateEvent?){
        if(ticks > 1000){
            mc.thePlayer.isOnLadder()
            mc.gameSettings.keyBindJump.pressed
            mc.thePlayer.motionY = 0.11
        }
        if(ticks > 2000){
            val ticks: Int = 0
        }else{
            val ticks: Int = ticks + 1
        }
    }
}
