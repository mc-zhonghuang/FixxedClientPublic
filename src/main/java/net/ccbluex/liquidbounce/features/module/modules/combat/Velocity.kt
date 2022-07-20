/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.utils.DebugUtil
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C0BPacketEntityAction
import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minecraft.network.play.server.S27PacketExplosion
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.concurrent.ThreadLocalRandom

@ModuleInfo(name = "Velocity",category = ModuleCategory.COMBAT)
class Velocity : Module() {

    /**
     * OPTIONS
     */
    private val horizontalValue = FloatValue("Horizontal", 0F, 0F, 1F)
    private val verticalValue = FloatValue("Vertical", 0F, 0F, 1F)
    private val alerts = BoolValue("alerts", true)
    private val velocityTickValue = IntegerValue("VelocityTick", 1, 0, 10)
    private val hyttickVluae = IntegerValue("HytTick", 2, 0, 100).displayable{modeValue.get().equals("HytTick", true)}
    private val pushResetValue = FloatValue("PushXZReduce", 2F, 0.0F, 3F).displayable{modeValue.get().equals("Push", true)}
    private val pushYResetValue = FloatValue("PushYReduce", 0.15F, 0.0F, 1F).displayable{modeValue.get().equals("Push", true)}
    private val modeValue = ListValue("Mode", arrayOf(
        "Cancel",
        "PacketPhase",
        "Spoof",
        "Tick",
        "Vanilla",
        "Clean",
        "Hypixel",
        "Huayuting",
        "HuayutingJump",
        "AAC4",
        "AAC5", "AAC5.2.0", "AAC5vanilla",
        "AAC4Reduce", "AAC5Reduce",
        "MatrixReduce", "MatrixVanilla", "MatrixGround",
        "OnLag",
        "Reverse", "SmoothReverse",
        "Phase", "PacketPhase", "Glitch", "Fake",
        "Legit","Kiana","AriaCraft","AAC","Huayuting2", "Thursday","HytSpoof", "Hyt","HytTick","Vulcan","Push"
    ), "Cancel")
    private val phaseHeightValue = FloatValue("PhaseHeight", 0.5F, 0F, 1F)
    private val phaseOnlyGround = BoolValue("PhaseOnlyGround", true)
    private val onlyCombatValue = BoolValue("OnlyCombat", false)
    private val onlyGroundValue = BoolValue("OnlyGround", false)
    private val noFireValue = BoolValue("noFire", false)

    // Reverse
    private val reverseStrengthValue = FloatValue("ReverseStrength", 1F, 0.1F, 1F).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    private val reverse2StrengthValue = FloatValue("SmoothReverseStrength", 0.05F, 0.02F, 0.1F).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})

    // AAC Push
    private val aacPushXZReducerValue = FloatValue("AACPushXZReducer", 2F, 1F, 3F).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    private val aacPushYReducerValue = BoolValue("AACPushYReducer", true).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    private val aacYReduceVale = BoolValue("AACYReduce", false).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    private val onLagReduceVale = BoolValue("OnLag",false).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    private val aac5onlag = BoolValue("AAC",false).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})

    // phase
    // legit
    private val legitStrafeValue = BoolValue("LegitStrafe", false).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    private val legitFaceValue = BoolValue("LegitFace", true).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    private val onlyHitVelocityValue = BoolValue("OnlyHitVelocity",false).displayable({ !modeValue.equals("Cancel") && !modeValue.equals("PacketPhase") && !modeValue.equals("Spoof") && !modeValue.equals("Tick") && !modeValue.equals("Vanilla") && !modeValue.equals("Clean")})
    // Revers
    // Legit

    /**
     * VALUES
     */
    private var velocityCalcTimer = MSTimer()
    private var velocityAirTick = 0

    // SmoothReverse
    private var reverseHurt = false

    // AACPush
    private var jump = false

    // Legit

    private var redeCount = 24

    private var templateX = 0
    private var templateY = 0
    private var templateZ = 0

    private var isMatrixOnGround = false

    private var pos:BlockPos?=null

    private var huayutingjumpflag = false
    /**
     * VALUES
     */
    private var velocityTimer = MSTimer()
    private var velocityInput = false

    // SmoothReverse
    private var velocityTick = 0
    // AACPush
    private var var0 = false;//hurt
    override val tag: String
        get() = modeValue.get()


    override fun onDisable() {
        mc.thePlayer?.speedInAir = 0.02F
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if(velocityInput) {
            velocityTick++
        }else velocityTick = 0

        if (mc.thePlayer.isInWater || mc.thePlayer.isInLava || mc.thePlayer.isInWeb)
            return
        if ((onlyGroundValue.get() && !mc.thePlayer.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }
        if (noFireValue.get() && mc.thePlayer.isBurning) return
        when (modeValue.get().toLowerCase()) {
            "huayuting2" -> {
                if(mc.thePlayer.hurtTime > 0 && velocityInput) {
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.motionX *= (mc.thePlayer.motionX * (0.56 * Math.random()))
                        mc.thePlayer.motionY *= (mc.thePlayer.motionX * (0.77 * Math.random()))
                        mc.thePlayer.motionZ *= (mc.thePlayer.motionX * (0.56 * Math.random()))
                    } else {
                        mc.thePlayer.motionX *= (mc.thePlayer.motionX * (0.77 * Math.random()))
                        mc.thePlayer.motionZ *= (mc.thePlayer.motionZ * (0.77 * Math.random()))
                    }
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING))
                    velocityInput = false
                }
            }

            "thursday" -> {
                if(mc.thePlayer.hurtTime > 0 && velocityInput) {
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.motionX *= (mc.thePlayer.motionX * (0.56 * Math.random()))
                        mc.thePlayer.motionY *= (mc.thePlayer.motionX * (0.77 * Math.random()))
                        mc.thePlayer.motionZ *= (mc.thePlayer.motionX * (0.56 * Math.random()))
                        mc.thePlayer.onGround = false
                    } else {
                        mc.thePlayer.motionX *= (mc.thePlayer.motionX * (0.77 * Math.random()))
                        mc.thePlayer.onGround = true
                        mc.thePlayer.motionZ *= (mc.thePlayer.motionZ * (0.77 * Math.random()))
                    }
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING))
                    velocityInput = false
                }
            }

            "hyttick" -> {
                if(velocityTick > hyttickVluae.get()) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "hyt" -> {
                if(velocityTick > 100) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "hytspoof" -> {
                if (mc.thePlayer.hurtTime <= 0 && !velocityInput)
                    return

                mc.thePlayer.motionX *= 0.91111166007777
                mc.thePlayer.motionZ *= 0.87681111233666
                mc.thePlayer.onGround = !mc.thePlayer.onGround
                mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING))
                velocityInput = false
            }

            "push" -> {
                if (mc.thePlayer.hurtTime <= 0 && !velocityInput)
                    return

                val reduce = pushResetValue.get()
                if (jump) {
                    jump = !mc.thePlayer.onGround
                } else {
                    mc.thePlayer.motionY -= pushYResetValue.get()
                }
                mc.thePlayer.motionX /= reduce
                mc.thePlayer.motionZ /= reduce
            }

            "vulcan" -> {
                if(velocityTick > 10) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "tick" -> {
                if(velocityTick > velocityTickValue.get()) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "aac4" -> {
                if (!mc.thePlayer.onGround) {
                    if (mc.thePlayer.hurtTime != 0 && var0) {
                        if (alerts.get()) {
                            DebugUtil.log(
                                "Velocity",
                                ThreadLocalRandom.current().nextInt(1000, 10000).toString()
                            )
                        }
                        mc.thePlayer.motionX *= 0.6;
                        mc.thePlayer.motionZ *= 0.6;
                    }
                } else if (velocityTimer.hasTimePassed(80)) {
                    var0 = false
                }
            }

            "simplefix" -> {
                if(velocityTick > velocityTickValue.get()) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "huayutingjump" -> {
                if(mc.thePlayer.hurtTime > 0 && huayutingjumpflag) {
                    if(mc.thePlayer.onGround){
                        mc.thePlayer.hurtTime <= 6
                        mc.thePlayer.motionX *= 0.600151164
                        mc.thePlayer.motionZ *= 0.600151164
                        mc.thePlayer.hurtTime <= 4
                        mc.thePlayer.motionX *= 0.700151164
                        mc.thePlayer.motionZ *= 0.700151164
                    }else if(mc.thePlayer.hurtTime <= 9) {
                        mc.thePlayer.motionX *= 0.6001421204
                        mc.thePlayer.motionZ *= 0.6001421204
                    }
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING))
                    huayutingjumpflag = false
                }
            }

            "huayutingfix" -> {
                if(velocityTick > velocityTickValue.get()) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "aac5.2.0.14" ->{
                if(aac5onlag.get()) {
                    if(mc.thePlayer.motionY > 0) mc.thePlayer.motionY = 0.0
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.00001f
                    velocityInput = false
                }
                if(mc.thePlayer.onGround && velocityTick > 1) {
                    velocityInput = false
                }
            }

            "jump" -> if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.42
            }

            "reverse" -> {
                if (!velocityInput) {
                    return
                }

                if (!mc.thePlayer.onGround) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * reverseStrengthValue.get())
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                }
            }

            "smoothreverse" -> {
                if (!velocityInput) {
                    mc.thePlayer.speedInAir = 0.02F
                    return
                }

                if (mc.thePlayer.hurtTime > 0) {
                    reverseHurt = true
                }

                if (!mc.thePlayer.onGround) {
                    if (reverseHurt) {
                        mc.thePlayer.speedInAir = reverse2StrengthValue.get()
                    }
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false
                    reverseHurt = false
                }
            }

            "nikoniko" -> {
                if (mc.thePlayer.hurtTime>1 && velocityInput){
                    mc.thePlayer.motionX *= 0.81
                    mc.thePlayer.motionZ *= 0.81
                }
                if(velocityInput && (mc.thePlayer.hurtTime<5 || mc.thePlayer.onGround) && velocityTimer.hasTimePassed(120L)) {
                    velocityInput = false
                }
            }

            "aac5" -> {
                if (mc.thePlayer.hurtTime>0){
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.motionX *= 0
                        mc.thePlayer.motionZ *= 0
                        if(aacYReduceVale.get()){
                            mc.thePlayer.motionY *= verticalValue.get()
                        }
                    }
                    else{
                        mc.thePlayer.motionX *= 0.8
                        mc.thePlayer.motionZ *= 0.8
                    }
                }
            }

            "aac4reduce" -> {
                if (mc.thePlayer.hurtTime> 0 && !mc.thePlayer.onGround && velocityInput && velocityTimer.hasTimePassed(80L)) {
                    mc.thePlayer.motionX *= 0.62
                    mc.thePlayer.motionZ *= 0.62
                }
                if (velocityInput && (mc.thePlayer.hurtTime <4 || mc.thePlayer.onGround) && velocityTimer.hasTimePassed(120L)) {
                    velocityInput = false
                }
            }

            "aac5reduce" -> {
                if (mc.thePlayer.hurtTime> 1 && velocityInput) {
                    mc.thePlayer.motionX *= 0.81
                    mc.thePlayer.motionZ *= 0.81
                }
                if (velocityInput && (mc.thePlayer.hurtTime <5 || mc.thePlayer.onGround) && velocityTimer.hasTimePassed(120L)) {
                    velocityInput = false
                }
            }

            "aac5.2.0combat" -> {
                if (mc.thePlayer.hurtTime> 0 && velocityInput) {
                    velocityInput = false
                    mc.thePlayer.motionX = 0.0
                    mc.thePlayer.motionZ = 0.0
                    mc.thePlayer.motionY = 0.0
                    mc.thePlayer.jumpMovementFactor = -0.002f
                    mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true))
                }
                if (velocityTimer.hasTimePassed(80L) && velocityInput) {
                    velocityInput = false
                    mc.thePlayer.motionX = templateX / 8000.0
                    mc.thePlayer.motionZ = templateZ / 8000.0
                    mc.thePlayer.motionY = templateY / 8000.0
                    mc.thePlayer.jumpMovementFactor = -0.002f
                }
            }

            "aac" -> if (velocityInput && velocityTimer.hasTimePassed(80L)) {
                mc.thePlayer.motionX *= horizontalValue.get()
                mc.thePlayer.motionZ *= horizontalValue.get()
                //mc.thePlayer.motionY *= verticalValue.get() ?
                velocityInput = false
            }

            "aacpush" -> {
                if (jump) {
                    if (mc.thePlayer.onGround) {
                        jump = false
                    }
                } else {
                    // Strafe
                    if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.motionX != 0.0 && mc.thePlayer.motionZ != 0.0) {
                        mc.thePlayer.onGround = true
                    }

                    // Reduce Y
                    if (mc.thePlayer.hurtResistantTime > 0 && aacPushYReducerValue.get() &&
                        !LiquidBounce.moduleManager[Speed::class.java]!!.state) {
                        mc.thePlayer.motionY -= 0.014999993
                    }
                }

                // Reduce XZ
                if (mc.thePlayer.hurtResistantTime >= 19) {
                    val reduce = aacPushXZReducerValue.get()

                    mc.thePlayer.motionX /= reduce
                    mc.thePlayer.motionZ /= reduce
                }
            }
            "matrixreduce" -> {
                if (mc.thePlayer.hurtTime > 0) {
                    if (mc.thePlayer.onGround) {
                        if (mc.thePlayer.hurtTime <= 6) {
                            mc.thePlayer.motionX *= 0.70
                            mc.thePlayer.motionZ *= 0.70
                        }
                        if (mc.thePlayer.hurtTime <= 5) {
                            mc.thePlayer.motionX *= 0.80
                            mc.thePlayer.motionZ *= 0.80
                        }
                    } else if (mc.thePlayer.hurtTime <= 10) {
                        mc.thePlayer.motionX *= 0.60
                        mc.thePlayer.motionZ *= 0.60
                    }
                }
            }

            "matrixground" -> {
                isMatrixOnGround = mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown
                if (isMatrixOnGround) mc.thePlayer.onGround = false
            }

            "glitch" -> {
                mc.thePlayer.noClip = velocityInput

                if (mc.thePlayer.hurtTime == 7) {
                    mc.thePlayer.motionY = 0.4
                }

                velocityInput = false
            }

            "aaczero" -> {
                if (mc.thePlayer.hurtTime > 0) {
                    if (!velocityInput || mc.thePlayer.onGround || mc.thePlayer.fallDistance > 2F) {
                        return
                    }

                    mc.thePlayer.addVelocity(0.0, -1.0, 0.0)
                    mc.thePlayer.onGround = true
                } else {
                    velocityInput = false
                }
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is S12PacketEntityVelocity) {
            if (mc.thePlayer == null || (mc.theWorld?.getEntityByID(packet.entityID) ?: return) != mc.thePlayer)
                return

            velocityTimer.reset()
            if ((onlyGroundValue.get() && !mc.thePlayer.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
                return
            }
            if (noFireValue.get() && mc.thePlayer.isBurning) return
            velocityTimer.reset()
            velocityTick = 0
            when (modeValue.get().toLowerCase()) {
                "huayuting2" -> {
                    event.cancelEvent()
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING))
                }

                "thursday" -> {
                    if(packet.motionX in -150..150 || packet.motionY in -150..150 || packet.motionZ in -150..150){
                        packet.motionX = (packet.motionX * 0.0).toInt()
                        packet.motionY = (packet.motionY * 0.0).toInt()
                        packet.motionZ = (packet.motionZ * 0.0).toInt()
                    } else {
                        event.cancelEvent()
                        mc.netHandler.addToSendQueue(C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING))
                    }

                }

                "hyttick" -> event.cancelEvent()

                "hyt" -> {
                    velocityInput = true
                    val horizontal = 0F
                    val vertical = 0F

                    if (horizontal == 0F && vertical == 0F) {
                        event.cancelEvent()
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }

                "vulcan" -> {
                    velocityInput = true
                    val horizontal = 0F
                    val vertical = 0F

                    if (horizontal == 0F && vertical == 0F) {
                        event.cancelEvent()
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }

                "hytspoof" -> {
                    if (mc.thePlayer.hurtTime != 0) {
                        event.cancelEvent()
                    } else {
                        packet.motionX = (packet.getMotionX() * 0.0).toInt()
                        packet.motionY = (packet.getMotionY() * 0.0).toInt()
                        packet.motionZ = (packet.getMotionZ() * 0.0).toInt()
                    }
                }

                "tick" -> {
                    velocityInput = true
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == 0F && vertical == 0F) {
                        event.cancelEvent()
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "vanilla" -> {
                    event.cancelEvent()
                    if (alerts.get()) {
                        DebugUtil.log(
                            "Velocity",
                            ThreadLocalRandom.current().nextInt(1000, 10000).toString()
                        )
                    }
                }
                "spoof" -> {
                    event.cancelEvent()
                    mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + packet.motionX / 8000.0, mc.thePlayer.posY + packet.motionY / 8000.0, mc.thePlayer.posZ + packet.motionZ / 8000.0, false))
                }
                "packetphase" -> {
                    if (!mc.thePlayer.onGround && phaseOnlyGround.get()) {
                        return
                    }

//                    chat("MOTX=${packet.motionX}, MOTZ=${packet.motionZ}")
                    if (packet.motionX <500 && packet.motionY <500) {
                        return
                    }

                    mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - phaseHeightValue.get(), mc.thePlayer.posZ, false))
                    event.cancelEvent()
                    packet.motionX = 0
                    packet.motionY = 0
                    packet.motionZ = 0
                }
                "aac4" -> {
                    velocityTimer.reset();
                    var0 = true;
                }
                "clean"->{
                    if (alerts.get()) {
                        DebugUtil.log(
                            "Velocity",
                            ThreadLocalRandom.current().nextInt(1000, 10000).toString()
                        )
                    }
                    if (0 < mc.thePlayer.hurtTime) {
                        if (packet is S12PacketEntityVelocity) {
                            if(packet is C03PacketPlayer) {
                                packet.y += 0.11451419198
                            }
                        }
                        if (packet is S12PacketEntityVelocity) {
                            packet.motionX = 0 * packet.getMotionX();
                            packet.motionY = 0 * packet.getMotionY();
                            packet.motionZ = 0 * packet.getMotionZ()
                        }
                    } else {
                        if (packet is S12PacketEntityVelocity) {
                            packet.motionX = 0 * packet.getMotionX();
                            packet.motionY = 0 * packet.getMotionY();
                            packet.motionZ = 0 * packet.getMotionZ()
                        }
                        if (packet is S12PacketEntityVelocity) {
                            if(packet is C03PacketPlayer) {
                                packet.y += 0.11451419198
                            }
                        }
                    }
                }
                "cancel" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()
                    if (alerts.get()) {
                        DebugUtil.log(
                            "Velocity",
                            ThreadLocalRandom.current().nextInt(1000, 10000).toString()
                        )
                    }
                    if (horizontal == 0F && vertical == 0F)
                        event.cancelEvent()
                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "huayutingjump" -> {
                    if(packet is S12PacketEntityVelocity) {
                        huayutingjumpflag = true
                        if(mc.thePlayer.hurtTime != 0) {
                            event.cancelEvent()
                            packet.motionX = 0
                            packet.motionY = 0
                            packet.motionZ = 0
                        }
                    }
                }
                "huayutingfix" -> {
                    val  horizontal = horizontalValue.get()
                    val  vertical = verticalValue.get()

                    if (horizontal ==  -1.0E-5F && vertical == -0F) {
                        event.cancelEvent()
                    }
                    packet.motionX = (packet.getMotionX() * vertical).toInt()
                    packet.motionY = (packet.getMotionY() * horizontal).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "simplefix" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (aac5onlag.get()) {
                        if (horizontal == -2.1E-9F && vertical == 0F){
                            event.cancelEvent()
                        }
                        packet.motionX = (packet.getMotionX() * vertical).toInt()
                        packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                        packet.motionY = (packet.getMotionY() * horizontal).toInt()
                    }
                }
                "onlag" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (onLagReduceVale.get()){
                        if (horizontal == -1.011F && vertical == 0F) {
                            event.cancelEvent()
                        }
                        packet.motionY = (packet.getMotionY() * vertical).toInt()
                        packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                        packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    }
                }
                "huayuting" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == -1250.0F && vertical == 0F) {
                        event.cancelEvent()
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "normal" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == 0F && vertical == 0F)
                        event.cancelEvent()

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "aac5simple" -> {
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == -223.0F && vertical == 0F) {
                        event.cancelEvent()
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "simple" -> {
                    //velocityInput = true
                    val horizontal = horizontalValue.get()
                    val vertical = verticalValue.get()

                    if (horizontal == 0F && vertical == 0F) {
                        event.cancelEvent()
                    }

                    packet.motionX = (packet.getMotionX() * horizontal).toInt()
                    packet.motionY = (packet.getMotionY() * vertical).toInt()
                    packet.motionZ = (packet.getMotionZ() * horizontal).toInt()
                }
                "matrixsimple" -> {
                    packet.motionX = (packet.getMotionX() * 0.36).toInt()
                    packet.motionZ = (packet.getMotionZ() * 0.36).toInt()
                    if (mc.thePlayer.onGround) {
                        packet.motionX = (packet.getMotionX() * 0.9).toInt()
                        packet.motionZ = (packet.getMotionZ() * 0.9).toInt()
                    }
                }

                "matrixground" -> {
                    packet.motionX = (packet.getMotionX() * 0.36).toInt()
                    packet.motionZ = (packet.getMotionZ() * 0.36).toInt()
                    if (isMatrixOnGround) {
                        packet.motionY = (-628.7).toInt()
                        packet.motionX = (packet.getMotionX() * 0.6).toInt()
                        packet.motionZ = (packet.getMotionZ() * 0.6).toInt()
                    }
                }

                "aac4reduce" -> {
                    velocityInput = true
                    packet.motionX = (packet.getMotionX() * 0.6).toInt()
                    packet.motionZ = (packet.getMotionZ() * 0.6).toInt()
                }

                "aac5.2.0" -> {
                    event.cancelEvent()
                    mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true))
                }

                "NikoNiko","aac5reduce","aac",  "reverse", "smoothreverse", "aaczero" -> velocityInput = true

                "phase" -> {
                    if (!mc.thePlayer.onGround && phaseOnlyGround.get()) {
                        return
                    }

                    velocityInput = true
                    mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY - phaseHeightValue.get(), mc.thePlayer.posZ)
                    event.cancelEvent()
                    packet.motionX = 0
                    packet.motionY = 0
                    packet.motionZ = 0
                }

                "aac5.2.0combat" -> {
                    event.cancelEvent()
                    velocityInput = true
                    templateX = packet.motionX
                    templateZ = packet.motionZ
                    templateY = packet.motionY
                }

                "glitch" -> {
                    if (!mc.thePlayer.onGround) {
                        return
                    }

                    velocityInput = true
                    event.cancelEvent()
                }

                "legit" -> {
                    pos = BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
                }
                "nekojump" ->{
                    event.cancelEvent()
                    mc.thePlayer.motionY = 0.42

                }
            }
        }

        if (packet is S27PacketExplosion) {
            // TODO: Support velocity for explosions
            event.cancelEvent()
        }
    }

    @EventTarget
    fun onStrafe(event: StrafeEvent) {
        if ((onlyGroundValue.get() && !mc.thePlayer.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }

        when (modeValue.get().toLowerCase()) {
            "legit" -> {
                if (pos == null || mc.thePlayer.hurtTime <= 0) {
                    return
                }

                val rot = RotationUtils.getRotations(pos!!.x.toDouble(), pos!!.y.toDouble(), pos!!.z.toDouble())
                if (legitFaceValue.get()) {
                    RotationUtils.setTargetRotation(rot)
                }
                val yaw = rot.yaw
                if (legitStrafeValue.get()) {
                    val speed = MovementUtils.getSpeed()
                    val yaw1 = Math.toRadians(yaw.toDouble())
                    mc.thePlayer.motionX = -sin(yaw1) * speed
                    mc.thePlayer.motionZ = cos(yaw1) * speed
                } else {
                    var strafe = event.strafe
                    var forward = event.forward
                    val friction = event.friction

                    var f = strafe * strafe + forward * forward

                    if (f >= 1.0E-4F) {
                        f = MathHelper.sqrt_float(f)

                        if (f < 1.0F) {
                            f = 1.0F
                        }

                        f = friction / f
                        strafe *= f
                        forward *= f

                        val yawSin = MathHelper.sin((yaw * Math.PI / 180F).toFloat())
                        val yawCos = MathHelper.cos((yaw * Math.PI / 180F).toFloat())

                        mc.thePlayer.motionX += strafe * yawCos - forward * yawSin
                        mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin
                    }
                }
            }
        }
    }


    @EventTarget
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer == null || mc.thePlayer.isInWater || mc.thePlayer.isInLava || mc.thePlayer.isInWeb)
            return
        if ((onlyGroundValue.get() && !mc.thePlayer.onGround) || (onlyCombatValue.get() && !LiquidBounce.combatManager.inCombat)) {
            return
        }
        when (modeValue.get().toLowerCase()) {
            "aacpush" -> {
                jump = true

                if (!mc.thePlayer.isCollidedVertically) {
                    event.cancelEvent()
                }
            }

            "aaczero" -> if (mc.thePlayer.hurtTime > 0) {
                event.cancelEvent()
            }

            "push" -> {
                jump = true

                if (!mc.thePlayer.isCollidedVertically)
                    event.cancelEvent()
            }
        }
    }
}
