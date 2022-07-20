package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue

class BlocksmcHopTest : SpeedMode("BlocksmcHopTest"){
    private val timerSpeedValue =  FloatValue("TimerSpeed",1.11f,0f,3f)
    override fun onPreMotion() {
        if (mc.thePlayer.isInWater) return

        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump()
            } else{
                MovementUtils.strafe(MovementUtils.getSpeed() * timerSpeedValue.get())
            }
        } else {
            mc.thePlayer.speedInAir = 1f
            mc.timer.timerSpeed = 1f
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        }
    }
}