/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.network.play.server.S0BPacketAnimation
import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minecraft.network.play.server.S20PacketEntityProperties
import net.minecraft.stats.StatList

@ModuleInfo(name = "Criticals", category = ModuleCategory.COMBAT)
class Criticals : Module() {

    val modeValue = ListValue("Mode", arrayOf("NewPacket","Packet","Vulcan" ,"NCPPacket", "MiPacket", "Watchdog","Blcoksmc", "VulcanSemi", "AACPacket", "AAC4.3.11OldHYT", "AAC5.0.4", "Hypixels", "NoGround", "TPHop", "FakeCollide", "Mineplex", "More", "TestMinemora", "hop", "tphop","jump","NanoPacket","NON-Calculable","Invalid","VerusSmart","Visual","Horzion","Exhibition","JigSaw","Nov","ETB","Remix","Sigma","Motion", "Hover", "custom"), "packet")
    // Other Lists
    private val motionValue = ListValue("MotionMode", arrayOf("Redesky","RedeSkyLowHop", "Hop", "Jump", "LowJump", "MinemoraTest", "TPHop", "TestMotion", "FakeJump"), "Jump")
    private val hoverValue = ListValue("HoverMode", arrayOf("AAC4", "AAC4Other", "VeryLow", "OldRedesky", "Normal1", "Normal2", "Normal3","Minis", "Minis2", "TPCollide", "2b2t"), "AAC4")
    private val customValue = ListValue("CustomMode", arrayOf("C04", "Motion"), "C04")
    private val hypixelValue = ListValue("HypixelMode", arrayOf("A", "B", "C", "D", "E"), "A").displayable { modeValue.equals("Hypixels")}
    // Hover
    private val hoverNoFall = BoolValue("HoverNoFall", true).displayable { modeValue.equals("Hover") }
    private val hoverCombat = BoolValue("HoverOnlyCombat", true).displayable { modeValue.equals("Hover") }
    // Custom
    private val c04xValue = FloatValue("C04-X Offset", 0f, 0f, 1f).displayable {modeValue.equals("Custom") && customValue.equals("C04")}
    private val c04yValue = FloatValue("C04-Y Offset", 0.02f, 0f, 1f).displayable {modeValue.equals("Custom") && customValue.equals("C04")}
    private val c04zValue = FloatValue("C04-Z Offset", 0f, 0f, 1f).displayable {modeValue.equals("Custom") && customValue.equals("C04")}
    private val c04groundValue = BoolValue("C04-OnGround", false).displayable {modeValue.equals("Custom") && customValue.equals("C04")}
    private val c04repeatValue = IntegerValue("C04-RepeatTimes", 1, 1, 5).displayable {modeValue.equals("Custom") && customValue.equals("C04")}
    private val customMotionValue = FloatValue("CustomMotion", 0.42f, 0f, 1f).displayable {modeValue.equals("Custom") && customValue.equals("Motion")}
    // Bypass
    private val delayValue = IntegerValue("Delay", 0, 0, 1000)
    private val s08FlagValue = BoolValue("FlagPause", true)
    private val s08DelayValue = IntegerValue("FlagPauseTime", 100, 0, 5000).displayable { s08FlagValue.get() }
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 20)
    private val lookValue = BoolValue("UseC06Packet", false)
    // Debug
    private val debugValue = ListValue("Debug", arrayOf("Off","ChatA","ChatB","ChatC","ChatD","NotiA","NotiB","NotiC","NotiD"), "Off")
    // NoGround
    private val rsNofallValue = BoolValue("NofallHelper",true).displayable { modeValue.equals("AACNoGround") }
    private val badGroundValue = BoolValue("BadGround", false).displayable { modeValue.equals("NoGround") || modeValue.equals("AACNoGround") }
    // Other
    private val attackTimesValue = IntegerValue("AttackTimes", 0, 5, 10).displayable { modeValue.equals("VerusSmart") || modeValue.equals("Vulcan") || modeValue.equals("TestPacket") }
    private val resetMotionValue = BoolValue("ResetMotion", false)
    //jump custom
    private val jumpHeightValue = FloatValue("JumpHeight", 0.42F, 0.1F, 0.42F)
    private val downYValue = FloatValue("DownY", 0f, 0f, 0.1F)
    //other aura
    private val onlyAuraValue = BoolValue("OnlyAura", false)

    private val msTimer = MSTimer()
    
    private val flagTimer = MSTimer()

    private var readyCrits = false
    private var canCrits = true
    private var target = 0
    var jState = 0
    var aacLastState = false
    var attacks = 0
    private var counter = 0

    override fun onEnable() {
        if (modeValue.equals("NoGround")) {
            mc.thePlayer.jump()
        }
        jState = 0
        attacks = 0
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        // only Aura
        if (onlyAuraValue.get() && !LiquidBounce.moduleManager[Aura::class.java]!!.state && !LiquidBounce.moduleManager[InfiniteAura::class.java]!!.state) return

        if (event.targetEntity is EntityLivingBase) {
            val entity = event.targetEntity
            target = entity.entityId

            if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder || mc.thePlayer.isInWeb || mc.thePlayer.isInWater ||
                mc.thePlayer.isInLava || mc.thePlayer.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() ||
                !msTimer.hasTimePassed(delayValue.get().toLong())) {
                return
            }
            
            if(s08FlagValue.get() && !flagTimer.hasTimePassed(s08DelayValue.get().toLong()))
                return

            fun sendCriticalPacket(xOffset: Double = 0.0, yOffset: Double = 0.0, zOffset: Double = 0.0, ground: Boolean) {
                val x = mc.thePlayer.posX + xOffset
                val y = mc.thePlayer.posY + yOffset
                val z = mc.thePlayer.posZ + zOffset
                if (lookValue.get()) {
                    mc.netHandler.addToSendQueue(C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, ground))
                } else {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, ground))
                }
            }

            fun sendCrit(xv: Double = 0.0, yv: Double = 0.0, zv: Double = 0.0, gv: Boolean) {
                val x = mc.thePlayer.posX + xv
                val y = mc.thePlayer.posY + yv
                val z = mc.thePlayer.posZ + zv
                if (lookValue.get()) {
                    mc.netHandler.addToSendQueue(C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, gv))
                } else {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, gv))
                }
            }


            val x = mc.thePlayer.posX
            val y = mc.thePlayer.posY
            val z = mc.thePlayer.posZ

            when (modeValue.get().lowercase()) {
                "newpacket" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.05250000001304, z, true))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.00150000001304, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.01400000001304, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.00150000001304, z, false))
                    mc.thePlayer.onCriticalHit(entity)
                }

                "packet" -> {
                    sendCriticalPacket(yOffset = 0.0625, ground = true)
                    sendCriticalPacket(ground = false)
                    sendCriticalPacket(yOffset = 1.1E-5, ground = false)
                    sendCriticalPacket(ground = false)
                }

                "Vulcan" -> {
                    sendCriticalPacket(yOffset = 0.1216, ground = false)
                }

                "ncppacket" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.11, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.1100013579, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.0000013579, z, false))
                    mc.thePlayer.onCriticalHit(entity)
                }

                "hop" -> {
                    mc.thePlayer.motionY = 0.1
                    mc.thePlayer.fallDistance = 0.1f
                    mc.thePlayer.onGround = false
                }

                "tphop" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.02, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.01, z, false))
                    mc.thePlayer.setPosition(x, y + 0.01, z)
                }

                "jump" -> {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = jumpHeightValue.get().toDouble()
                    } else {
                        mc.thePlayer.motionY -= downYValue.get()
                    }
                }

                "nanopacket" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.00973333333333, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.001, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y - 0.01200000000007, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y - 0.0005, z, false))
                }

                "non-calculable" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 1E-5, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 1E-7, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y - 1E-6, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y - 1E-4, z, false))
                }

                "invalid" -> {
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 1E+27, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y - 1E+68, z, false))
                    mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 1E+41, z, false))
                }

                "verussmart" -> {
                    counter++
                    if (counter == 1) {
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.001, z, true))
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
                    }
                    if (counter >= 5)
                        counter = 0
                }

                "visual" -> mc.thePlayer.onCriticalHit(entity)
                
                "mipacket" -> {
                    sendCriticalPacket(yOffset = 0.0625, ground = false)
                    sendCriticalPacket(ground = false)
                }
                
                "aac5.0.4" -> { //aac5.0.4 moment but with bad cfg(cuz it will flag for timer)
                    sendCriticalPacket(yOffset = 0.00133545, ground = false)
                    sendCriticalPacket(yOffset = -0.000000433, ground = false)
                }

                "Watchdog" -> {
                    sendCriticalPacket(yOffset = 0.02, ground = false)
                    sendCriticalPacket(yOffset = 0.0016, ground = false)
                    sendCriticalPacket(yOffset = 0.03, ground = false)
                    sendCriticalPacket(yOffset = 0.0016, ground = false)
                }

                "aac4.3.11oldhyt" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.01400000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                }
                
                "vulcansemi" -> {
                    attacks++
                    if(attacks > 6) {
                        sendCriticalPacket(yOffset = 0.2, ground = false)
                        sendCriticalPacket(yOffset = 0.1216, ground = false)
                        attacks = 0
                    }
                }

                "mineplex" -> {
                    sendCriticalPacket(yOffset = 0.0000000000000045, ground = false)
                    sendCriticalPacket(ground = false)
                }

                "more" -> {
                    sendCriticalPacket(yOffset = 0.00000000001, ground = false)
                    sendCriticalPacket(ground = false)
                }

                // Minemora criticals without test
                "testminemora" -> {
                    sendCriticalPacket(yOffset = 0.0114514, ground = false)
                    sendCriticalPacket(yOffset = 0.0010999999940395355, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.0012016413, ground = false)
                }

                "aacpacket" -> {
                    sendCriticalPacket(yOffset = 0.05250000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.01400000001304, ground = false)
                    sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                }

                "hypixel" -> { // Glass = sb
                    when (hypixelValue.get().lowercase()) {
                        "a" -> {
                            sendCriticalPacket(yOffset = 0.02, ground = false)
                            sendCriticalPacket(yOffset = 0.0016, ground = false)
                            sendCriticalPacket(yOffset = 0.03, ground = false)
                            sendCriticalPacket(yOffset = 0.0016, ground = false)
                        }
                        "b" -> {
                            sendCriticalPacket(yOffset = 0.04132332, ground = false)
                            sendCriticalPacket(yOffset = 0.023243243674, ground = false)
                            sendCriticalPacket(yOffset = 0.01, ground = false)
                            sendCriticalPacket(yOffset = 0.0011, ground = false)
                        }
                        "c" -> {
                            sendCriticalPacket(yOffset = 0.05250000001304, ground = false)
                            sendCriticalPacket(yOffset = 0.00150000001304, ground = false)
                        }
                        "d" -> {
                            for (offset in doubleArrayOf(0.011, 0.02233445566, 0.056876574557, 0.096875875757)) {
                                sendCriticalPacket(yOffset = mc.thePlayer.posY + offset + Math.random() * 0.001, ground = false)
                            }
                        }
                        "e" -> {
                            for (offset in doubleArrayOf(0.05,0.0016,0.0018,0.0016,0.002,0.04,0.0011)) {
                                sendCriticalPacket(yOffset = mc.thePlayer.posY + offset, ground = false)
                            }
                        }
                    }
                }

                "blocksmc" -> {
                    counter++
                    if (counter == 1) {
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y + 0.001, z, true))
                        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
                    }
                    if (counter >= attackTimesValue.get())
                        counter = 0
                }

                "fakecollide" -> {
                    val motionX: Double
                    val motionZ: Double
                    if (MovementUtils.isMoving()) {
                        motionX = mc.thePlayer.motionX
                        motionZ = mc.thePlayer.motionZ
                    } else {
                        motionX = 0.00
                        motionZ = 0.00
                    }
                    mc.thePlayer.triggerAchievement(StatList.jumpStat)
                    sendCriticalPacket(xOffset = motionX / 3, yOffset = 0.20000004768372, zOffset = motionZ / 3, ground = false)
                    sendCriticalPacket(xOffset = motionX / 1.5, yOffset = 0.12160004615784, zOffset = motionZ / 1.5, ground = false)
                }

                "custom" -> {
                    when (customValue.get().lowercase()) {
                        "c04" -> {
                            val x2 = x + c04xValue.get().toDouble()
                            val y2 = y + c04yValue.get().toDouble()
                            val z2 = z + c04zValue.get().toDouble()
                            repeat(c04repeatValue.get()) {
                                mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(x2,y2,z2,c04groundValue.get()))
                            }
                        }
                        "motion" -> {
                            mc.thePlayer.motionY = customMotionValue.get().toDouble()
                        }
                    }
                }

                "motion" -> {
                    when (motionValue.get().lowercase()) {
                        "jump" -> mc.thePlayer.motionY = 0.42
                        "lowjump" -> mc.thePlayer.motionY = 0.3425
                        "redeskylowhop" -> mc.thePlayer.motionY = 0.35
                        "hop" -> {
                            mc.thePlayer.motionY = 0.1
                            mc.thePlayer.fallDistance = 0.1f
                            mc.thePlayer.onGround = false
                        }
                        "minemoratest" -> {
                            mc.timer.timerSpeed = 0.82f
                            mc.thePlayer.motionY = 0.124514
                        }
                        "fakejump" -> {
                            mc.thePlayer.jump()
                            mc.thePlayer.motionY = -0.4
                        }
                    }
                }

                "horzion" -> {
                    if (mc.thePlayer.motionX == 0.0 && mc.thePlayer.motionZ == 0.0) {
                        sendCrit(yv = 0.00000000255, gv = true)
                        sendCrit(gv = false)
                    }
                }

                "exhibition" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                }
                "jigsaw" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.1 - 5, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                }
                "nov" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624,mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                }
                "etb" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                }
                "remix" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.03 - 0.003, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.02 - 0.002, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                }
                "sigma" -> {
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0626, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00001, mc.thePlayer.posZ, true))
                    mc.thePlayer.sendQueue.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                }
            }
            msTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        
        if (packet is S08PacketPlayerPosLook) {
            flagTimer.reset()
            if (s08FlagValue.get()) {
                jState = 0
            }
        }
        
        if(s08FlagValue.get() && !flagTimer.hasTimePassed(s08DelayValue.get().toLong()))
            return

        if (packet is C03PacketPlayer) {
            when (modeValue.get().lowercase()) {
                "redesky" -> {
                    if (packet is C03PacketPlayer) {
                        val packetPlayer: C03PacketPlayer = packet as C03PacketPlayer
                        if(mc.thePlayer.onGround && canCrits) {
                            packetPlayer.y += 0.000001
                            packetPlayer.onGround = false
                        }
                        if(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(
                                0.0, (mc.thePlayer.motionY - 0.08) * 0.98, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) {
                            packetPlayer.onGround = true;
                        }
                    }
                    if(packet is C07PacketPlayerDigging) {
                        if(packet.status == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                            canCrits = false;
                        } else if(packet.status == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK || packet.status == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                            canCrits = true;
                        }
                    }
                }

                "noground" -> packet.onGround = false
                "motion" -> {
                    when (motionValue.get().lowercase()) {
                        "minemoratest" -> if (!LiquidBounce.combatManager.inCombat) mc.timer.timerSpeed = 1.00f
                    }
                }
                "hover" -> {
                    if (hoverCombat.get() && !LiquidBounce.combatManager.inCombat) return
                    packet.isMoving = true
                    when (hoverValue.get().lowercase()) {
                        "2b2t" -> {
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.02
                                    3 -> packet.y += 0.01
                                    4 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }
                        "minis2" -> {
                            if (mc.thePlayer.onGround && !aacLastState) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                if (jState % 2 == 0) {
                                    packet.y += 0.015625
                                } else if (jState> 100) {
                                    if (hoverNoFall.get()) packet.onGround = true
                                    jState = 1
                                }
                            } else jState = 0
                        }
                        "tpcollide" -> {
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.20000004768372
                                    3 -> packet.y += 0.12160004615784
                                    4 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }
                        "minis" -> {
                            if (mc.thePlayer.onGround && !aacLastState) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                                jState++
                                if (jState % 2 == 0) {
                                    packet.y += 0.0625
                                } else if (jState> 50) {
                                    if (hoverNoFall.get()) packet.onGround = true
                                    jState = 1
                                }
                            } else jState = 0
                        }
                        "normal1" -> {
                            if (mc.thePlayer.onGround) {
                                if (!(hoverNoFall.get() && jState == 0)) packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.001335979112147
                                    3 -> packet.y += 0.0000000131132
                                    4 -> packet.y += 0.0000000194788
                                    5 -> packet.y += 0.00000000001304
                                    6 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }
                        "aac4other" -> {
                            if (mc.thePlayer.onGround && !aacLastState && hoverNoFall.get()) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                packet.y += 0.00101
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            packet.y += 0.001
                            if (mc.thePlayer.onGround || !hoverNoFall.get()) packet.onGround = false
                        }
                        "aac4" -> {
                            if (mc.thePlayer.onGround && !aacLastState && hoverNoFall.get()) {
                                packet.onGround = mc.thePlayer.onGround
                                aacLastState = mc.thePlayer.onGround
                                packet.y += 0.000000000000136
                                return
                            }
                            aacLastState = mc.thePlayer.onGround
                            packet.y += 0.000000000000036
                            if (mc.thePlayer.onGround || !hoverNoFall.get()) packet.onGround = false
                        }
                        "normal2" -> {
                            if (mc.thePlayer.onGround) {
                                if (!(hoverNoFall.get() && jState == 0)) packet.onGround = false
                                jState++
                                when (jState) {
                                    2 -> packet.y += 0.00000000000667547
                                    3 -> packet.y += 0.00000000000045413
                                    4 -> packet.y += 0.000000000000036
                                    5 -> {
                                        if (hoverNoFall.get()) packet.onGround = true
                                        jState = 1
                                    }
                                    else -> jState = 1
                                }
                            } else jState = 0
                        }
                        "oldredesky" -> {
                            if (hoverNoFall.get() && mc.thePlayer.fallDistance> 0) {
                                packet.onGround = true
                                return
                            }

                            if (mc.thePlayer.onGround) {
                                packet.onGround = false
                            }
                        }
                    }
                }
            }
        }
        var off = 0
        if (packet is S0BPacketAnimation) {
            if (packet.animationType == 4 && packet.entityID == target) {
                when (debugValue.get().lowercase()) {
                    "off" -> off = 0
                    "chata" -> alert("Critical!")
                    "chatb" -> alert("Critical:" + RandomUtils.randomNumber(4)) // length���趨��ʵû������
                    "notia" -> LiquidBounce.hud.addNotification(Notification("Debug>>", "Critical!", NotifyType.INFO))
                    "notib" -> LiquidBounce.hud.addNotification(Notification("Debug>>", "Critical:" + RandomUtils.randomNumber(4), NotifyType.INFO))
                    "chatc" -> alert("Critical:" + RandomUtils.randomNumber(4) + "." + RandomUtils.randomNumber(4))
                    "chatd" -> alert("Critical:" + RandomUtils.randomNumber(3) + "." + RandomUtils.randomNumber(6))
                    "notic" -> LiquidBounce.hud.addNotification(Notification("Debug>>", "Critical:" + RandomUtils.randomNumber(4) + "." + RandomUtils.randomNumber(4), NotifyType.INFO))
                    "notid" -> LiquidBounce.hud.addNotification(Notification("Debug>>", "Critical:" + RandomUtils.randomNumber(3) + "." + RandomUtils.randomNumber(6), NotifyType.INFO))
                }
            }
        }
    }

    override val tag: String
        get() = modeValue.get()
}
