package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.features.module.modules.world.Blockfly
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MoveUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.block.BlockUtils
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.timer.TimeHelper
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.BlockAir
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos

@ModuleInfo(name = "AntiVoid", category = ModuleCategory.PLAYER)
class AntiVoid : Module() {
    private val modeValue = ListValue("Mode", arrayOf("WatchDog","Blink", "TPBack", "MotionFlag", "PacketFlag", "GroundSpoof", "OldHypixel", "Jartex", "OldCubecraft"), "Blink")
    private val pullbackTime = FloatValue("PullbackTime", 800F, 800F, 1800F).displayable{modeValue.equals("WatchDog")}
    private val maxFallDistValue = FloatValue("MaxFallDistance", 10F, 5F, 20F).displayable{!modeValue.equals("WatchDog")}
    private val resetMotionValue = BoolValue("ResetMotion", false).displayable { modeValue.equals("Blink")}
    private val startFallDistValue = FloatValue("BlinkStartFallDistance", 2F, 0F, 5F).displayable { modeValue.equals("Blink") }
    private val autoScaffoldValue = BoolValue("BlinkAutoScaffold", true).displayable { modeValue.equals("Blink") }
    private val voidOnlyValue = BoolValue("OnlyVoid", true).displayable{!modeValue.equals("WatchDog")}

    private val packetCache = ArrayList<C03PacketPlayer>()
    private var blink = false
    private var canBlink = false
    private var canSpoof = false
    private var tried = false
    private var flagged = false

    private var posX = 0.0
    private var posY = 0.0
    private var posZ = 0.0
    private var motionX = 0.0
    private var motionY = 0.0
    private var motionZ = 0.0
    private var lastRecY = 0.0

    private var flyable = false
    protected val fly: Fly
        get() = LiquidBounce.moduleManager[Fly::class.java]!!
    private val flytimer = MSTimer()

    //flux
    var timer: TimeHelper = TimeHelper()
    var lastGroundPos = DoubleArray(3)
    var packets = ArrayList<C03PacketPlayer>()

    override fun onEnable() {
        blink = false
        canBlink = false
        canSpoof = false
        lastRecY = mc.thePlayer.posY
        tried = false
        flagged = false
        if ((!voidOnlyValue.get() || checkVoid()) && modeValue.get() == "Vulcan"){
            mc.netHandler.addToSendQueue(
                C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY + 3.35,
                    mc.thePlayer.posZ,
                    false
                )
            )
            mc.netHandler.addToSendQueue(
                C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY,
                    mc.thePlayer.posZ,
                    false
                )
            )
            mc.netHandler.addToSendQueue(
                C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY,
                    mc.thePlayer.posZ,
                    true
                )
            )
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionY = 0.0
            mc.thePlayer.motionZ = 0.0
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ)
            flyable = true
            timer.reset()
            fly.launchY += 0.42
        }
    }

    @EventTarget
    open fun isInVoid(): Boolean {
        for (i in 0..128) {
            if (MoveUtils.isOnGround(i.toDouble())) {
                return false
            }
        }
        return true
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.onGround) {
            tried = false
            flagged = false
        }

        when (modeValue.get().lowercase()) {
            "groundspoof" -> {
                if (!voidOnlyValue.get() || checkVoid()) {
                    canSpoof = mc.thePlayer.fallDistance > maxFallDistValue.get()
                }
            }

            "motionflag" -> {
                if (!voidOnlyValue.get() || checkVoid()) {
                    if (mc.thePlayer.fallDistance > maxFallDistValue.get() && !tried) {
                        mc.thePlayer.motionY += 1
                        mc.thePlayer.fallDistance = 0.0F
                        tried = true
                    }
                }
            }

            "packetflag" -> {
                if (!voidOnlyValue.get() || checkVoid()) {
                    if (mc.thePlayer.fallDistance > maxFallDistValue.get() && !tried) {
                        mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 1, mc.thePlayer.posY + 1, mc.thePlayer.posZ + 1, false))
                        tried = true
                    }
                }
            }

            "tpback" -> {
                if (mc.thePlayer.onGround && BlockUtils.getBlock(BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)) !is BlockAir) {
                    posX = mc.thePlayer.prevPosX
                    posY = mc.thePlayer.prevPosY
                    posZ = mc.thePlayer.prevPosZ
                }
                if (!voidOnlyValue.get() || checkVoid()) {
                    if (mc.thePlayer.fallDistance > maxFallDistValue.get() && !tried) {
                        mc.thePlayer.setPositionAndUpdate(posX, posY, posZ)
                        mc.thePlayer.fallDistance = 0F
                        mc.thePlayer.motionX = 0.0
                        mc.thePlayer.motionY = 0.0
                        mc.thePlayer.motionZ = 0.0
                        tried = true
                    }
                }
            }

            "jartex" -> {
                canSpoof = false
                if (!voidOnlyValue.get() || checkVoid()) {
                    if (mc.thePlayer.fallDistance> maxFallDistValue.get() && mc.thePlayer.posY <lastRecY + 0.01 && mc.thePlayer.motionY <= 0 && !mc.thePlayer.onGround && !flagged) {
                        mc.thePlayer.motionY = 0.0
                        mc.thePlayer.motionZ *= 0.838
                        mc.thePlayer.motionX *= 0.838
                        canSpoof = true
                    }
                }
                lastRecY = mc.thePlayer.posY
            }

            "oldcubecraft" -> {
                canSpoof = false
                if (!voidOnlyValue.get() || checkVoid()) {
                    if (mc.thePlayer.fallDistance> maxFallDistValue.get() && mc.thePlayer.posY <lastRecY + 0.01 && mc.thePlayer.motionY <= 0 && !mc.thePlayer.onGround && !flagged) {
                        mc.thePlayer.motionY = 0.0
                        mc.thePlayer.motionZ = 0.0
                        mc.thePlayer.motionX = 0.0
                        mc.thePlayer.jumpMovementFactor = 0.00f
                        canSpoof = true
                        if (!tried) {
                            tried = true
                            mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, (32000.0).toDouble(), mc.thePlayer.posZ, false))
                        }
                    }
                }
                lastRecY = mc.thePlayer.posY
            }

            "blink" -> {
                if (!blink) {
                    val collide = FallingPlayer(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, 0.0, 0.0, 0.0, 0F, 0F, 0F, 0F).findCollision(60)
                    if (canBlink && (collide == null || (mc.thePlayer.posY - collide.y)> startFallDistValue.get())) {
                        posX = mc.thePlayer.posX
                        posY = mc.thePlayer.posY
                        posZ = mc.thePlayer.posZ
                        motionX = mc.thePlayer.motionX
                        motionY = mc.thePlayer.motionY
                        motionZ = mc.thePlayer.motionZ

                        packetCache.clear()
                        blink = true
                    }

                    if (mc.thePlayer.onGround) {
                        canBlink = true
                    }
                } else {
                    if (mc.thePlayer.fallDistance> maxFallDistValue.get()) {
                        mc.thePlayer.setPositionAndUpdate(posX, posY, posZ)
                        if (resetMotionValue.get()) {
                            mc.thePlayer.motionX = 0.0
                            mc.thePlayer.motionY = 0.0
                            mc.thePlayer.motionZ = 0.0
                            mc.thePlayer.jumpMovementFactor = 0.00f
                        } else {
                            mc.thePlayer.motionX = motionX
                            mc.thePlayer.motionY = motionY
                            mc.thePlayer.motionZ = motionZ
                            mc.thePlayer.jumpMovementFactor = 0.00f
                        }

                        if (autoScaffoldValue.get()) {
                            LiquidBounce.moduleManager[Blockfly::class.java]!!.state = true
                        }

                        packetCache.clear()
                        blink = false
                        canBlink = false
                    } else if (mc.thePlayer.onGround) {
                        blink = false

                        for (packet in packetCache) {
                            mc.netHandler.addToSendQueue(packet)
                        }
                    }
                }
            }
        }
    }

    private fun checkVoid(): Boolean {
        var i = (-(mc.thePlayer.posY-1.4857625)).toInt()
        var dangerous = true
        while (i <= 0) {
            dangerous = mc.theWorld.getCollisionBoxes(mc.thePlayer.entityBoundingBox.offset(mc.thePlayer.motionX * 0.5, i.toDouble(), mc.thePlayer.motionZ * 0.5)).isEmpty()
            i++
            if (!dangerous) break
        }
        return dangerous
    }

    @EventTarget
    fun onPacket(event: PacketEvent,e: PacketSendEvent) {
        val packet = event.packet

        when (modeValue.get().lowercase()) {
            "WatchDog" -> {
                if (!packets.isEmpty() && mc.thePlayer.ticksExisted < 100) packets.clear()
                if (e.getPacket() is C03PacketPlayer) {
                    val packet = e.getPacket() as C03PacketPlayer
                    if (isInVoid()) {
                        e.setCancelled(true)
                        packets.add(packet)
                        if (timer.isDelayComplete(pullbackTime.get().toDouble())) {
                            ClientUtils.displayChatMessage("发送数据包")
                            PacketUtils.sendPacketNoEvent(
                                C03PacketPlayer.C04PacketPlayerPosition(
                                    lastGroundPos[0], lastGroundPos[1] - 1,
                                    lastGroundPos[2], true
                                )
                            )
                        }
                    } else {
                        lastGroundPos[0] = mc.thePlayer.posX
                        lastGroundPos[1] = mc.thePlayer.posY
                        lastGroundPos[2] = mc.thePlayer.posZ
                        if (!packets.isEmpty()) {
                            ClientUtils.displayChatMessage("释放数据包 - " + packets.size)
                            for (p in packets) PacketUtils.sendPacketNoEvent(p)
                            packets.clear()
                        }
                        timer.reset()
                    }
                }
            }

            "groundspoof" -> {
                if (canSpoof && (packet is C03PacketPlayer)) {
                    packet.onGround = true
                }
            }

            "vulcan" -> {
                if (packet is C03PacketPlayer) {
                    packet.onGround = true
                }
            }

            "jartex" -> {
                if (canSpoof && (packet is C03PacketPlayer)) {
                    packet.onGround = true
                }
                if (canSpoof && (packet is S08PacketPlayerPosLook)) {
                    flagged = true
                }
            }

            "oldcubecraft" -> {
                if (canSpoof && (packet is C03PacketPlayer)) {
                    if (packet.y < 1145.141919810) event.cancelEvent()
                }
                if (canSpoof && (packet is S08PacketPlayerPosLook)) {
                    flagged = true
                }
            }

            "oldhypixel" -> {
                if (packet is S08PacketPlayerPosLook && mc.thePlayer.fallDistance> 3.125) mc.thePlayer.fallDistance = 3.125f

                if (packet is C03PacketPlayer) {
                    if (voidOnlyValue.get() && mc.thePlayer.fallDistance >= maxFallDistValue.get() && mc.thePlayer.motionY <= 0 && checkVoid()) {
                        packet.y += 11.0
                    }
                    if (!voidOnlyValue.get() && mc.thePlayer.fallDistance >= maxFallDistValue.get()) packet.y += 11.0
                }
            }
        }
    }
    fun onBlockBB(event: BlockBBEvent) {
        if(modeValue.get() == "Vulcan"){
            if (event.block is BlockAir && event.y <= fly.launchY) {
                event.boundingBox = AxisAlignedBB.fromBounds(event.x.toDouble(), event.y.toDouble(), event.z.toDouble(), event.x + 1.0, fly.launchY, event.z + 1.0)
            }
        }
    }

    fun onJump(event: JumpEvent) {
        if(modeValue.get() == "Vulcan"){
            event.cancelEvent()
        }
    }
    @EventTarget
    fun onRevPacket(e: PacketReceiveEvent) {
        if (e.getPacket() is S08PacketPlayerPosLook && packets.size > 1) {
            ClientUtils.displayChatMessage("检测到回调，清除数据包列表！")
            packets.clear()
        }
    }
}
