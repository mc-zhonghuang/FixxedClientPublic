package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TickTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S2FPacketSetSlot
import java.util.*
import kotlin.concurrent.schedule

@ModuleInfo(name = "AutoKit", category = ModuleCategory.MISC)
class AutoKit : Module() {

    private val kitNameValue = TextValue("Kit-Name", "Armorer")

    // for easier selection
    private val editMode = BoolValue("Edit-Mode", false)
    private val debugValue = BoolValue("Debug", false)

    private var clickStage = 0

    private var listening = false
    private var expectSlot = -1

    private var timeoutTimer = TickTimer()
    private var delayTimer = MSTimer()

    private fun debug(s: String) {
        if (debugValue.get()) ClientUtils.displayChatMessage("§7[§4§l自动套件§7] §r$s")
    }

    override fun onEnable() {
        clickStage = 0
        listening = false
        expectSlot = -1

        timeoutTimer.reset()
        delayTimer.reset()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (clickStage == 1) { // 重复s2f数据包的最低要求
            if (!delayTimer.hasTimePassed(1000L)) return
            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(expectSlot - 36))
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer.getSlot(expectSlot).getStack()))
            clickStage = 2
            delayTimer.reset()
            debug("单击套件选择器")
        } else if (!listening) {
            delayTimer.reset()
        }

        if (clickStage == 2) {
            timeoutTimer.update()
            if (timeoutTimer.hasTimePassed(40)) {
                // close the things and notify
                clickStage = 0
                listening = false
                mc.netHandler.addToSendQueue(C0DPacketCloseWindow())
                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                LiquidBounce.hud.addNotification(Notification(this.name,"套件检查器超时。请使用正确的套件名称。", NotifyType.ERROR,3500))
                debug("找不到任何具有该名称的工具包")
            }
        } else {
            timeoutTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (!editMode.get() && listening && packet is S2DPacketOpenWindow) {
            event.cancelEvent()
            debug("正在侦听so取消打开窗口数据包")
            return
        }

        if (packet is C0DPacketCloseWindow && editMode.get()) {
            editMode.set(false)
            LiquidBounce.hud.addNotification(Notification(this.name,"编辑模式中止。", NotifyType.INFO,3500))
            debug("中止编辑模式")
            return
        }

        if (packet is S2FPacketSetSlot) {
            val item = packet.func_149174_e() ?: return
            val windowId = packet.func_149175_c()
            val slot = packet.func_149173_d()
            val itemName = item.unlocalizedName
            val displayName = item.displayName

            if (clickStage == 0 && windowId == 0 && slot >= 36 && slot <= 44 && itemName.contains("bow", true) && displayName.contains("kit selector", true)) { // dynamic for solo/teams
                if (editMode.get()) {
                    listening = true
                    debug("已找到项目，正在收听编辑模式下的套件选择cuz")
                    return
                } else {
                    listening = true
                    clickStage = 1
                    expectSlot = slot
                    debug("已找到项，已发送触发器")
                    return
                }
            }

            if (clickStage == 2 && displayName.contains(kitNameValue.get(), true)) {
                timeoutTimer.reset()
                clickStage = 3
                debug("检测到的套件选择")
                Timer().schedule(250L) {
                    mc.netHandler.addToSendQueue(C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919))
                    //mc.netHandler.addToSendQueue(C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919))
                    mc.netHandler.addToSendQueue(C0DPacketCloseWindow(windowId))
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                    debug("挑选出来的")
                }
                return
            }
        }

        if (packet is S02PacketChat) {
            val text = packet.chatComponent.unformattedText

            if (text.contains("已选择套件", true)) {
                if (editMode.get()) {
                    val kitName = text.replace(" 已选择套件！", "")
                    kitNameValue.set(kitName)
                    editMode.set(false)
                    clickStage = 0
                    listening = false
                    LiquidBounce.hud.addNotification(Notification(this.name,"已成功检测并添加 $kitName 配套元件", NotifyType.INFO,3500))
                    debug("完成的检测套件")
                    return
                } else {
                    listening = false
                    event.cancelEvent()
                    LiquidBounce.hud.addNotification(Notification(this.name,"已成功选择 ${kitNameValue.get()} 配套元件", NotifyType.INFO,3500))
                    debug("已完成")
                    return
                }
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        clickStage = 0
        listening = false
        expectSlot = -1

        timeoutTimer.reset()
        delayTimer.reset()
    }

    override val tag: String
        get() = if (editMode.get() && listening) "听..." else kitNameValue.get()
}
