/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.event

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.model.ModelPlayer
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.Packet
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.world.IBlockAccess

/**
 * Called when player attacks other entity
 *
 * @param targetEntity Attacked entity
 */
class BlockRenderEvent2(val x : Float,val y : Float,val z : Float,val block: Block) : Event()
class AttackEvent2(val targetEntity: Entity?) : Event()
class BlockRenderSideEvent2(var world: IBlockAccess, var pos: BlockPos, var side: EnumFacing, var maxX: Double, var minX: Double, var maxY: Double, var minY: Double, var maxZ: Double, var minZ: Double) : CancellableEvent() {
    var isToRender2 : Boolean = true
    var state2 : IBlockState? = if (Minecraft.getMinecraft().theWorld != null) Minecraft.getMinecraft().theWorld.getBlockState(pos) else null
}
/**
 * Called when minecraft get bounding box of block
 *
 * @param blockPos block position of block
 * @param block block itself
 * @param boundingBox vanilla bounding box
 */
/**
 * Called when connecting
 */
data class ConnectingEvent2(val ip: String, val port: Int) : Event()

class EntityKilledEvent2(val targetEntity: EntityLivingBase) : Event()
class BlockBBEvent2(blockPos: BlockPos, val block: Block, var boundingBox: AxisAlignedBB?) : Event() {
    val x = blockPos.x
    val y = blockPos.y
    val z = blockPos.z
}
class PreUpdateEvent(var x: Double, var y: Double, var z: Double, val yaw: Float, private val pitch: Float, val isSneaking: Boolean, var isOnGround: Boolean) : Event() {

    fun fire() {
        prevYAW = YAW
        prevPITCH = PITCH
        YAW = yaw
        PITCH = pitch
        SNEAKING = isSneaking
        OnGround = isOnGround
    }

    fun getYaw(yaw: Float): Float {
        return yaw
    }

    fun getPitch(pitch: Float): Float {
        return pitch
    }

    companion object {
        var YAW = 0f
        var PITCH = 0f
        var prevYAW = 0f
        var prevPITCH = 0f
        var SNEAKING = false
        var OnGround = false
    }
}

/**
 * Called when player clicks a block
 */
class ClickBlockEvent2(val clickedBlock: BlockPos?, val enumFacing: EnumFacing?) : Event()

/**
 * Called when client is shutting down
 */
class ClientShutdownEvent2 : Event()

/**
 * Called when an other entity moves
 */
data class EntityMovementEvent2(val movedEntity: Entity) : Event()

/**
 * Called when player jumps
 *
 * @param motion jump motion (y motion)
 */
class JumpEvent2(var motion: Float) : CancellableEvent()

/**
 * Called when user press a key once
 *
 * @param key Pressed key
 */
class KeyEvent2(val key: Int) : Event()

/**
 * Called in "onUpdateWalkingPlayer"
 *
 * @param eventState PRE or POST
 */
class MotionEvent2(val eventState: EventState) : Event() {
    fun isPre() : Boolean {
        return eventState == EventState.PRE
    }
}
class MotionEven2(var x: Double, var y: Double, var z: Double, var yaw: Float, var pitch: Float, var onGround: Boolean) : Event() {
    var eventState: EventState = EventState.PRE
}
/**
 * Called in "onLivingUpdate" when the player is using a use item.
 *
 * @param strafe the applied strafe slow down
 * @param forward the applied forward slow down
 */
class SlowDownEvent2(var strafe: Float, var forward: Float) : Event()

/**
 * Called in "moveFlying"
 */
class StrafeEvent2(val strafe: Float, val forward: Float, val friction: Float) : CancellableEvent()

/**
 * Called when update da model
 */
class UpdateModelEvent2(val player: EntityPlayer, val model: ModelPlayer) : Event()

/**
 * Called when player moves
 *
 * @param x motion
 * @param y motion
 * @param z motion
 */

class MoveEvent2(var x: Double, var y: Double, var z: Double) : CancellableEvent() {
    var isSafeWalk = false

    fun zero() {
        x = 0.0
        y = 0.0
        z = 0.0
    }

    fun zeroXZ() {
        x = 0.0
        z = 0.0
    }
}

/**
 * Called when receive or send a packet
 */
class PacketEvent2(val packet: Packet<*>) : CancellableEvent()

/**
 * Called when a block tries to push you
 */
class PushOutEvent2 : CancellableEvent()

/**
 * Called when screen is going to be rendered
 */
class Render2DEvent2(val partialTicks: Float) : Event()

/**
 * Called when world is going to be rendered
 */
class Render3DEvent2(val partialTicks: Float) : Event()

/**
 * Called when entity is going to be rendered
 */
class RenderEntityEvent2(val entity: Entity, val x: Double, val y: Double, val z: Double, val entityYaw: Float,
                        val partialTicks: Float) : Event()

/**
 * Called when the screen changes
 */
class ScreenEvent2(val guiScreen: GuiScreen?) : Event()

/**
 * Called when the session changes
 */
class SessionEvent2 : Event()

/**
 * Called when player is going to step
 */
class StepEvent2(var stepHeight: Float) : Event()

/**
 * Called when player step is confirmed
 */
class StepConfirmEvent2 : Event()

/**
 * Called when a text is going to be rendered
 */
class TextEvent2(var text: String?) : Event()

/**
 * tick... tack... tick... tack
 */
class TickEvent2 : Event()

/**
 * Called when minecraft player will be updated
 */
class UpdateEvent2 : Event()

/**
 * Called when the world changes
 */
class WorldEvent2(val worldClient: WorldClient?) : Event()

/**
 * Called when window clicked
 */
class ClickWindowEvent2(val windowId: Int, val slotId: Int, val mouseButtonClicked: Int, val mode: Int) : CancellableEvent()