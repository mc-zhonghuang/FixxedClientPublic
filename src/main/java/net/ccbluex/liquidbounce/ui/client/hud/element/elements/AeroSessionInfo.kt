package net.ccbluex.liquidbounce.ui.client.hud.element.elements


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.ui.font.Fonts.*
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.server.S03PacketTimeUpdate
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList


@ElementInfo(name = "AeroSessionInfo",blur = true)
class AeroSessionInfo(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F, side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.UP)) : Element(x, y, scale, side),Listenable {

    private val redValue = IntegerValue("BackgroundRed", 255, 0, 255)
    private val greenValue = IntegerValue("BackgroundGreen", 255, 0, 255)
    private val blueValue = IntegerValue("BackgroundBlue", 255, 0, 255)
    private val alpha = IntegerValue("BackgroundAlpha", 20, 0, 255)
    private val textredValue = IntegerValue("TextRed", 255, 0, 255)
    private val textgreenValue = IntegerValue("TextRed", 244, 0, 255)
    private val textblueValue = IntegerValue("TextBlue", 255, 0, 255)
    private val fontValue = FontValue("Font", font40)

    private var SessionInfoRows = 0

    private val packetHistoryTime = CopyOnWriteArrayList<Long>()

    private var kills = 0
    private val attackEntities = CopyOnWriteArrayList<EntityLivingBase>()

    private var bps = 0.0
    private var lastX = 0.0
    private var lastY = 0.0
    private var lastZ = 0.0

    private val startTime: Long

    override fun drawElement(partialTicks: Float): Border? {
        RenderUtils.drawRect(0F, this.SessionInfoRows * 18F + 10, 176F, this.SessionInfoRows * 18F + 12, Color(redValue.get(), greenValue.get(), blueValue.get(), 255).rgb)
        RenderUtils.drawRect(0F, this.SessionInfoRows * 18F + 12F, 176F, 80F, Color(40,39,36,160).rgb)
        Fonts.Icons.drawString("q", 6F, this.SessionInfoRows * 18F + 17, Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font40.drawStringWithShadow("Session Info", 18F, this.SessionInfoRows * 18F + 16, Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.Icons.drawString("a", 6F, this.SessionInfoRows * 18F + 29, Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font40.drawStringWithShadow("Kills: $kills",18F, this.SessionInfoRows * 18F + 29, Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.Icons.drawString("f", 6F, this.SessionInfoRows * 18F+43, Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font40.drawStringWithShadow("HurtTime: ${mc.thePlayer.hurtTime}",18F,this.SessionInfoRows*18F+42,Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.Icons.drawString("b", 6F, this.SessionInfoRows * 18F+56, Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font40.drawStringWithShadow("Speed: ${bps}",18F,this.SessionInfoRows*18F+55,Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.Hanabi.drawString("3", 6F, this.SessionInfoRows * 18F+70, Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font40.drawStringWithShadow("Playing Time: ${getTime(System.currentTimeMillis()-startTime)}",18F,this.SessionInfoRows*18F+66,Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        return Border(0F, this.SessionInfoRows * 18F + 12F, 176F, 80F)
    }
    init {
        LiquidBounce.eventManager.registerListener(this)
        startTime = System.currentTimeMillis()
    }

    override fun handleEvents(): Boolean = true
    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet
        if (packet is S03PacketTimeUpdate){
            packetHistoryTime.add(System.currentTimeMillis())
        }
    }
    @EventTarget
    fun onAttack(event: AttackEvent){
        val entity = event.targetEntity ?: return
        if (entity !is EntityLivingBase || entity.isDead || entity.health == 0F) return
        if (entity !in attackEntities) attackEntities.add(entity)
    }



    /////
    override fun updateElement() {
        //Kills
        attackEntities.forEach {if(it.isDead || it.health == 0F){kills+=1;attackEntities.remove(it)}}
        //BPS
        updateBPS()
    }

    private fun getTime(time:Long): String{
        val h = (time / 1000) / 60 / 60
        val m = (time / 1000) / 60 % 60
        val s = (time / 1000) % 60
        return "${h}h ${m}m ${s}s"
        Fonts.font40.drawStringWithShadow("${h}h ${m}m ${s}s",167F,this.SessionInfoRows*18F+42,Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
    }

    private fun updateBPS(){
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 1) bps=0.0
        val distance = mc.thePlayer.getDistance(lastX, lastY, lastZ)
        lastX = mc.thePlayer.posX
        lastY = mc.thePlayer.posY
        lastZ = mc.thePlayer.posZ
        bps = String.format("%.2f",distance * (20 * mc.timer.timerSpeed)).toDouble()
    }

}