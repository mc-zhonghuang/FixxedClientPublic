package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.player.FastUse
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.C03PacketPlayer

@ModuleInfo(name = "InfinitePot", category = ModuleCategory.MOVEMENT)
class InfinitePot : Module(){
    var isDrink = false
    var count = 0
    var offsetY = 0
    var DisablerFastUse = false;
    private val durValue = IntegerValue("DurTick", 32, 0, 32)
    private val AutodisFastuseValue = BoolValue("AutoDis Fastuse", false)

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        val fastuse = LiquidBounce.moduleManager[FastUse::class.java] as FastUse
        if (isDrink) {
            count++
            mc.thePlayer.rotationPitch = 90F
            if ((count + 4) == durValue.get()) {
                val posX = mc.thePlayer.posX
                val posY = mc.thePlayer.posY
                val posZ = mc.thePlayer.posZ
                mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 4, posZ, true))
                LiquidBounce.hud.addNotification(
                    Notification(
                        "InfinitePot",
                        "Trigger infinite Potion",
                        NotifyType.SUCCESS, 3
                    )
                )
            }
        }
        if(DisablerFastUse && !isDrink){
            fastuse.state = true
            DisablerFastUse = false
        }
        if (mc.thePlayer.isUsingItem()) {
            val playeritem = mc.thePlayer.itemInUse.item
            if (!isDrink) {
                if (playeritem is ItemPotion) {
                    if(fastuse.state == true && AutodisFastuseValue.get()){
                        fastuse.state = false
                        DisablerFastUse = true
                    }
                    mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch
                    LiquidBounce.hud.addNotification(
                        Notification(
                            "InfinitePot",
                            "Enter infinite Pot countdown.",
                            NotifyType.SUCCESS,
                            3,
                        )
                    )
                    isDrink = true
                }
            }
        } else {
            if (isDrink) {
                isDrink = false
                offsetY = 0
                count = 0
            }
        }
    }

    override val tag: String?
        get() = " HytPacket"
}