/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */

package net.ccbluex.liquidbounce.utils

import net.ccbluex.liquidbounce.event.MoveEvent
import net.minecraft.entity.Entity
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.potion.Potion
import net.minecraft.util.AxisAlignedBB
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object MovementUtils : MinecraftInstance() {


    val jumpMotion: Float
        get() {
            var mot = 0.42f
            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                mot += (mc.thePlayer.getActivePotionEffect(Potion.jump).amplifier + 1).toFloat() * 0.1f
            }
            return mot
        }

    val movingYaw: Float
        get() = (direction * 180f / Math.PI).toFloat()

    fun getSpeed(): Float {
        return sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ).toFloat()
    }

    fun isOnGround(height: Double): Boolean {
        return !mc.theWorld.getCollidingBoundingBoxes(
            mc.thePlayer,
            mc.thePlayer.entityBoundingBox.offset(0.0, -height, 0.0)
        ).isEmpty()
    }

    fun getSpeed2(): Double {
        return getSpeed2(mc.thePlayer)
    }

    fun getSpeed2(entity: Entity): Double {
        return Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ)
    }

    fun getSpeed3(): Float {
        return getSpeed3(mc.thePlayer.motionX, mc.thePlayer.motionZ).toFloat()
    }

    fun getSpeed3(motionX: Double, motionZ: Double): Double {
        return Math.sqrt(motionX * motionX + motionZ * motionZ)
    }

    fun getJumpBoostModifier(baseJumpHeight: Double, potionJumpHeight: Boolean): Double {
        var baseJumpHeight = baseJumpHeight
        if (mc.thePlayer.isPotionActive(Potion.jump) && potionJumpHeight) {
            val amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).amplifier
            baseJumpHeight += ((amplifier + 1).toFloat() * 0.1f).toDouble()
        }
        return baseJumpHeight
    }

    fun getSpeedEffect(): Int {
        return if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 else 0
    }

    fun getJumpEffect(): Int {
        return if (mc.thePlayer.isPotionActive(Potion.jump)) mc.thePlayer.getActivePotionEffect(Potion.jump).amplifier + 1 else 0
    }

    fun strafe() {
        strafe(getSpeed())
    }

    fun move() {
        move(getSpeed())
    }

    fun isMoving(): Boolean {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f)
    }

    fun hasMotion(): Boolean {
        return mc.thePlayer.motionX != 0.0 && mc.thePlayer.motionZ != 0.0 && mc.thePlayer.motionY != 0.0
    }

    fun strafe(speed: Float) {
        if (!isMoving()) return
        val yaw = direction
        mc.thePlayer.motionX = -sin(yaw) * speed
        mc.thePlayer.motionZ = cos(yaw) * speed
    }

    fun move(speed: Float) {
        if (!isMoving()) return
        val yaw = direction
        mc.thePlayer.motionX += -sin(yaw) * speed
        mc.thePlayer.motionZ += cos(yaw) * speed
    }

    fun limitSpeed(speed: Float) {
        val yaw = direction
        val maxXSpeed = -sin(yaw) * speed
        val maxZSpeed = cos(yaw) * speed
        if (mc.thePlayer.motionX > maxZSpeed) {
            mc.thePlayer.motionX = maxXSpeed
        }
        if (mc.thePlayer.motionZ > maxZSpeed) {
            mc.thePlayer.motionZ = maxZSpeed
        }
    }

    /**
     * make player move slowly like when using item
     * @author liulihaocai
     */
    fun limitSpeedByPercent(percent: Float) {
        mc.thePlayer.motionX *= percent
        mc.thePlayer.motionZ *= percent
    }

    fun forward(length: Double) {
        val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
        mc.thePlayer.setPosition(mc.thePlayer.posX + -sin(yaw) * length, mc.thePlayer.posY, mc.thePlayer.posZ + cos(yaw) * length)
    }

    val direction: Double
        get() {
            var rotationYaw = mc.thePlayer.rotationYaw
            if (mc.thePlayer.moveForward < 0f) rotationYaw += 180f
            var forward = 1f
            if (mc.thePlayer.moveForward < 0f) forward = -0.5f else if (mc.thePlayer.moveForward > 0f) forward = 0.5f
            if (mc.thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward
            if (mc.thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward
            return Math.toRadians(rotationYaw.toDouble())
        }

    var bps = 0.0
        private set
    private var lastX = 0.0
    private var lastY = 0.0
    private var lastZ = 0.0

    fun setMotion(speed: Double) {
        var forward = mc.thePlayer.movementInput.moveForward.toDouble()
        var strafe = mc.thePlayer.movementInput.moveStrafe.toDouble()
        var yaw = mc.thePlayer.rotationYaw
        if (forward == 0.0 && strafe == 0.0) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (if (forward > 0.0) -45 else 45).toFloat()
                } else if (strafe < 0.0) {
                    yaw += (if (forward > 0.0) 45 else -45).toFloat()
                }
                strafe = 0.0
                if (forward > 0.0) {
                    forward = 1.0
                } else if (forward < 0.0) {
                    forward = -1.0
                }
            }
            val cos = cos(Math.toRadians((yaw + 90.0f).toDouble()))
            val sin = sin(Math.toRadians((yaw + 90.0f).toDouble()))
            mc.thePlayer.motionX = (forward * speed * cos +
                    strafe * speed * sin)
            mc.thePlayer.motionZ = (forward * speed * sin -
                    strafe * speed * cos)
        }
    }

    fun setMotion2(e: MoveEvent, speed: Double) {
        var forward = mc.thePlayer.movementInput.moveForward.toDouble()
        var strafe = mc.thePlayer.movementInput.moveStrafe.toDouble()
        var rotationYaw = mc.thePlayer.rotationYaw
        if (mc.thePlayer.moveForward < 0f) rotationYaw += 180f
        if (mc.thePlayer.moveStrafing > 0f) rotationYaw -= (90f * forward).toFloat()
        if (mc.thePlayer.moveStrafing < 0f) rotationYaw += (90f * forward).toFloat()
        var yaw = mc.thePlayer.rotationYaw.toDouble()
        if (forward == 0.0 && strafe == 0.0) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (if (forward > 0.0) -44 else 44).toDouble()
                } else if (strafe < 0.0) {
                    yaw += (if (forward > 0.0) 44 else -44).toDouble()
                }
                strafe = 0.0
                if (forward > 0.0) {
                    forward = 1.0
                } else if (forward < 0.0) {
                    forward = -1.0
                }
            }
            e.x = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)))
            e.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)))
        }
    }

    fun updateBlocksPerSecond() {
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 1) {
            bps = 0.0
        }
        val distance = mc.thePlayer.getDistance(lastX, lastY, lastZ)
        lastX = mc.thePlayer.posX
        lastY = mc.thePlayer.posY
        lastZ = mc.thePlayer.posZ
        bps = distance * (20 * mc.timer.timerSpeed)
    }

    fun setSpeed(moveEvent: MoveEvent, moveSpeed: Double, pseudoYaw: Float, pseudoStrafe: Double, pseudoForward: Double) {
        var forward = pseudoForward
        var strafe = pseudoStrafe
        var yaw = pseudoYaw
        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.z = 0.0
            moveEvent.x = 0.0
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (if (forward > 0.0) -45 else 45).toFloat()
                } else if (strafe < 0.0) {
                    yaw += (if (forward > 0.0) 45 else -45).toFloat()
                }
                strafe = 0.0
                if (forward > 0.0) {
                    forward = 1.0
                } else if (forward < 0.0) {
                    forward = -1.0
                }
            }
            val cos = Math.cos(Math.toRadians((yaw + 90.0f).toDouble()))
            val sin = Math.sin(Math.toRadians((yaw + 90.0f).toDouble()))
            moveEvent.x = forward * moveSpeed * cos + strafe * moveSpeed * sin
            moveEvent.z = forward * moveSpeed * sin - strafe * moveSpeed * cos
        }
    }

    fun calculateGround(): Double {
        val playerBoundingBox = mc.thePlayer.entityBoundingBox
        var blockHeight = 1.0
        var ground = mc.thePlayer.posY
        while (ground > 0.0) {
            val customBox = AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ)
            if (mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight
                ground += blockHeight
                blockHeight = 0.05
            }
            ground -= blockHeight
        }
        return 0.0
    }

    fun handleVanillaKickBypass() {
        val ground = calculateGround()
        run {
            var posY = mc.thePlayer.posY
            while (posY > ground) {
                mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true))
                if (posY - 8.0 < ground) break // Prevent next step
                posY -= 8.0
            }
        }
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, ground, mc.thePlayer.posZ, true))
        var posY = ground
        while (posY < mc.thePlayer.posY) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true))
            if (posY + 8.0 > mc.thePlayer.posY) break // Prevent next step
            posY += 8.0
        }
        mc.netHandler.addToSendQueue(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
    }

    fun getBaseMoveSpeed(): Double {
        var baseSpeed = mc.thePlayer.capabilities.walkSpeed * 2.873
        if (mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).amplifier + 1)
        }
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1)
        }
        return baseSpeed
    }

    fun setSpeed3(moveEvent: MoveEvent?, moveSpeed: Double) {
        setSpeed3(
            moveEvent!!, moveSpeed, mc.thePlayer.rotationYaw,
            mc.thePlayer.movementInput.moveStrafe.toDouble(), mc.thePlayer.movementInput.moveForward.toDouble()
        )
    }

    fun setSpeed3(moveSpeed: Double, yaw: Float, strafe: Double, forward: Double) {
        var yaw = yaw
        var strafe = strafe
        var forward = forward
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (if (forward > 0.0) -45 else 45).toFloat()
            } else if (strafe < 0.0) {
                yaw += (if (forward > 0.0) 45 else -45).toFloat()
            }
            strafe = 0.0
            if (forward > 0.0) {
                forward = 1.0
            } else if (forward < 0.0) {
                forward = -1.0
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0
        } else if (strafe < 0.0) {
            strafe = -1.0
        }
        val mx = Math.cos(Math.toRadians((yaw + 90.0f).toDouble()))
        val mz = Math.sin(Math.toRadians((yaw + 90.0f).toDouble()))
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx
    }

    fun setSpeed3(moveSpeed: Int) {
        setSpeed3(
            moveSpeed.toDouble(),
            mc.thePlayer.rotationYaw,
            mc.thePlayer.movementInput.moveStrafe.toDouble(),
            mc.thePlayer.movementInput.moveForward.toDouble()
        )
    }

    fun setSpeed3(moveEvent: MoveEvent, moveSpeed: Double, yaw: Float, strafe: Double, forward: Double) {
        var yaw = yaw
        var strafe = strafe
        var forward = forward
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (if (forward > 0.0) -45 else 45).toFloat()
            } else if (strafe < 0.0) {
                yaw += (if (forward > 0.0) 45 else -45).toFloat()
            }
            strafe = 0.0
            if (forward > 0.0) {
                forward = 1.0
            } else if (forward < 0.0) {
                forward = -1.0
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0
        } else if (strafe < 0.0) {
            strafe = -1.0
        }
        val mx = Math.cos(Math.toRadians((yaw + 90.0f).toDouble()))
        val mz = Math.sin(Math.toRadians((yaw + 90.0f).toDouble()))
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx
    }
    fun getPredictedMotionY(motionY: Double): Double {
        return (motionY - 0.08) * 0.98f
    }
}