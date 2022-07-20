package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.client.C16PacketClientStatus
import java.util.*

@ModuleInfo(name = "AutoLeave", ModuleCategory.COMBAT)
class AutoLeave : Module() {
    val timer = Timer()
    var check = true
    private val HealthValue = IntegerValue("Health", 5, 1, 20)
    private val KeepArmorValue = BoolValue("KeepArmor", true)

    @EventTarget
    fun move(item: Int, isArmorSlot: Boolean) {
        if (item != -1) {
            val openInventory = mc.currentScreen !is GuiInventory
            if (openInventory) mc.netHandler.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
            mc.playerController.windowClick(
                mc.thePlayer.inventoryContainer.windowId,
                if (isArmorSlot) item else if (item < 9) item + 36 else item,
                0,
                1,
                mc.thePlayer
            )
            if (openInventory) mc.netHandler.addToSendQueue(C0DPacketCloseWindow())
        }
    }

    fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer.health <= HealthValue.get()) {
            if(KeepArmorValue.get()) {
                for (i in 0..3) {
                    val armorSlot = 3 - i
                    move(8 - armorSlot, true)
                }
            }
            if (check == true) {
                check = false
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        mc.thePlayer.sendChatMessage("/hub")
                    }
                }, 50)
            }
            LiquidBounce.hud.addNotification(
                Notification(
                    "AutoLeave",
                    "Trigger AutoLeave",
                    NotifyType.WARNING, 3
                )
            )
        }
    }

    /**
     * Shift+Left clicks the specified item
     *
     * @param item        Slot of the item to click
     * @param isArmorSlot
     * @return True if it is unable to move the item
     */

    @EventTarget
    fun onWorld(event: WorldEvent?) {
        check = true
    }
}