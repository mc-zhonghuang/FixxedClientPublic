package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S2FPacketSetSlot
import net.minecraft.util.IChatComponent
import java.util.*
import kotlin.arrayOf
import kotlin.concurrent.schedule

@ModuleInfo(name = "AutoGG", category = ModuleCategory.PLAYER)
class AutoPlay : Module() {
    private var clickState = 0
    private val modeValue = ListValue("Server", arrayOf("RedeSky", "BlocksMC", "Minemora", "Hypixel", "HuaYuTingGG"), "HuaYuTingGG")
    private val delayValue = IntegerValue("JoinDelay", 3, 0, 7)

    private var clicking = false
    private var queued = false
    override fun onEnable() {
        clickState = 0
        clicking = false
        queued = false
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        when (modeValue.get().toLowerCase()) {
            "redesky" -> {
                if (clicking && (packet is C0EPacketClickWindow || packet is C07PacketPlayerDigging)) {
                    event.cancelEvent()
                    return
                }
                if (clickState == 2 && packet is S2DPacketOpenWindow) {
                    event.cancelEvent()
                }
            }
            "hypixel" -> {
                if (clickState == 1 && packet is S2DPacketOpenWindow) {
                    event.cancelEvent()
                }
            }
        }

        if (packet is S2FPacketSetSlot) {
            val item = packet.func_149174_e() ?: return
            val windowId = packet.func_149175_c()
            val slot = packet.func_149173_d()
            val itemName = item.unlocalizedName
            val displayName = item.displayName

            when (modeValue.get().toLowerCase()) {
                "redesky" -> {
                    if (clickState == 0 && windowId == 0 && slot == 42 && itemName.contains("paper", ignoreCase = true) && displayName.contains("Jogar novamente", ignoreCase = true)) {
                        clickState = 1
                        clicking = true
                        queueAutoPlay {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(6))
                            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(item))
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                            clickState = 2
                        }
                    } else if (clickState == 2 && windowId != 0 && slot == 11 && itemName.contains("enderPearl", ignoreCase = true)) {
                        Timer().schedule(500L) {
                            clicking = false
                            clickState = 0
                            mc.netHandler.addToSendQueue(C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919))
                        }
                    }
                }
                "blocksmc", "hypixel" -> {
                    if (clickState == 0 && windowId == 0 && slot == 43 && itemName.contains("paper", ignoreCase = true)) {
                        queueAutoPlay {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(7))
                            repeat(2) {
                                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(item))
                            }
                        }
                        clickState = 1
                    }
                    if (modeValue.equals("hypixel") && clickState == 1 && windowId != 0 && itemName.equals("item.fireworks", ignoreCase = true)) {
                        mc.netHandler.addToSendQueue(C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919))
                        mc.netHandler.addToSendQueue(C0DPacketCloseWindow(windowId))
                    }
                }
            }
        } else if (packet is S02PacketChat) {
            val text = packet.chatComponent.unformattedText
            when (modeValue.get().toLowerCase()) {
                "minemora" -> {
                    if (text.contains("Has click en alguna de las siguientes opciones", true)) {
                        queueAutoPlay {
                            mc.thePlayer.sendChatMessage("/join")
                        }
                    }
                }
                "blocksmc" -> {
                    if (clickState == 1 && text.contains("Only VIP players can join full servers!", true)) {
                        LiquidBounce.hud.addNotification(Notification(name,"Join failed try again", NotifyType.WARNING))
                        // connect failed so try to join again
                        Timer().schedule(1500L) {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(7))
                            repeat(2) {
                                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
                            }
                        }
                    }
                }
                "huayutinggg" -> {
                    if (text.contains("      ϲ��      һ��      ��ϲ��", true)) {
                        LiquidBounce.hud.addNotification(Notification(name,"Game Over", NotifyType.INFO))
                        mc.thePlayer.sendChatMessage("@[FixxedClient]GG")
                    }
                }
                "hypixel" -> {
                    fun process(component: IChatComponent) {
                        val value = component.chatStyle.chatClickEvent?.value
                        if (value != null && value.startsWith("/play", true)) {
                            queueAutoPlay {
                                mc.thePlayer.sendChatMessage(value)
                            }
                        }
                        component.siblings.forEach {
                            process(it)
                        }
                    }
                    process(packet.chatComponent)
                }
            }
        }
    }

    private fun queueAutoPlay(runnable: () -> Unit) {
        if (queued) {
            return
        }
        queued = true
        if (this.state) {
            Timer().schedule(delayValue.get().toLong() * 1000) {
                if (state) {
                    runnable()
                }
            }
            LiquidBounce.hud.addNotification(Notification(name,"Sending you to next game in ${delayValue.get()}s...", NotifyType.INFO))
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        clicking = false
        clickState = 0
        queued = false
    }

    override val tag: String
        get() = modeValue.get()

    override fun handleEvents() = true
}

/*
@ModuleInfo(name = "AutoGG", category = ModuleCategory.PLAYER)
class AutoGG : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Hypixel", "HuaYuTing"), "HuaYuTing")
    private val customchatmessage = TextValue(LiquidBounce.CLIENT_NAME, "[" + LiquidBounce.CLIENT_NAME + "]GG")
    private val strings = arrayOf(
        "1st Killer - ",
        "1st Place - ",
        "You died! Want to play again? Click here!",
        "You won! Want to play again? Click here!",
        " - Damage Dealt - ",
        "1st - ",
        "Winning Team - ",
        "Winners: ",
        "Winner: ",
        "Winning Team: ",
        " win the game!",
        "1st Place: ",
        "Last team standing!",
        "Winner #1 (",
        "Top Survivors",
        "Winners - "
    )

    @EventTarget
    fun onPacket(event: PacketEvent) {
        Intrinsics.checkNotNullParameter(event, "event")
        val packet = event.packet
        val text = modeValue.get()
        if (packet is S02PacketChat) {
            val ichatcomponent = packet.chatComponent
            Intrinsics.checkNotNullExpressionValue(ichatcomponent, "packet.chatComponent")
            val string = ichatcomponent.unformattedText
            var s = modeValue.get()
                ?: throw NullPointerException("null cannot be cast to non-null type java.lang.String")
            val s1 = s.lowercase(Locale.getDefault())
            Intrinsics.checkNotNullExpressionValue(s1, "(this as java.lang.String).toLowerCase()")
            s = Boolean.parseBoolean(s1).toString()
            when (s.hashCode()) {
                -1777040898 -> if (s == "huayuting") {
                    Intrinsics.checkExpressionValueIsNotNull(text, "text")
                    if (text.contains("��ϲ")) {
                        `access$getMc$p$s1046033730`().thePlayer.sendChatMessage("@[" + LiquidBounce.CLIENT_NAME + "]GG")
                        LiquidBounce.hud.addNotification(
                            Notification(
                                "AutoGG",
                                  "GameOver,Trigger AutoGG",
                                NotifyType.INFO, 3
                            )
                        )
                    }
                }
                1381910549 -> if (s == "Hypixel") {
                    val astring = strings
                    val i = astring.size
                    var j = 0
                    while (j < i) {
                        val s2 = astring[j]
                        Intrinsics.checkNotNullExpressionValue(string, "string")
                        if (string.contains(s2)) {
                            if (string.contains(strings[3])) {
                                sendGG()
                                LiquidBounce.hud.addNotification(
                                    Notification(
                                        "AutoGG",
                                        "GameOver,Trigger AutoGG",
                                        NotifyType.INFO, 3
                                    )
                                )
                            }
                            break
                        }
                        ++j
                    }
                }
            }
        }
    }

    private fun sendGG() {
        mc.thePlayer.sendChatMessage(customchatmessage.get())
    }

    companion object {
        fun `access$getMc$p$s1046033730`(): Minecraft {
            return mc
        }
    }
}

 */