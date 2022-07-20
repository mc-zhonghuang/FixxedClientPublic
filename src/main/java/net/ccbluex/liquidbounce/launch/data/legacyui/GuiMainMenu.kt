/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */

package net.ccbluex.liquidbounce.launch.data.legacyui

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.GuiBackground
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.slib.Guis.GuiLogin
import net.ccbluex.liquidbounce.utils.extensions.drawCenteredString
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import java.awt.Color

import org.lwjgl.opengl.GL11

class GuiMainMenu : GuiScreen(), GuiYesNoCallback {

    val bigLogo = ResourceLocation("fixxed/imgs/logo.png")

    var slideX : Float = 0F
    var fade : Float = 0F

    var sliderX : Float = 0F

    var lastAnimTick: Long = 0L
    var alrUpdate = false

    var lastXPos = 0F

    companion object {
        var useParallax = true
    }

    override fun initGui() {
        slideX = 0F
        fade = 0F
        sliderX = 0F
        //lastAnimTick = System.currentTimeMillis()
        //alrUpdate = false
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        
        if (!alrUpdate) {
            lastAnimTick = System.currentTimeMillis()
            alrUpdate = true
        }
        drawBackground(0)
        GL11.glPushMatrix()
        renderSwitchButton()
        Fonts.font40.drawStringWithShadow(" ${LiquidBounce.VERSIONTYPE} ${LiquidBounce.CLIENT_VERSION}",2F,10F,-1)
        Fonts.font35.drawStringWithShadow("${LiquidBounce.DEV_TIPS}", 4F,20F,-1)

        Fonts.font40.drawStringWithShadow("${LiquidBounce.CLIENT_NAME}  ${LiquidBounce.CLIENT_VERSION}", 2F, height - 12F, -1)
        Fonts.font40.drawStringWithShadow(LiquidBounce.DEV_SAY, width - 3F - Fonts.font40.getStringWidth(LiquidBounce.DEV_SAY), height - 12F, -1)
        if (useParallax) moveMouseEffect(mouseX, mouseY, 10F)
        GlStateManager.disableAlpha()
        RenderUtils.drawImage2(bigLogo, width / 2F - 50F, height / 2F - 90F, 100, 100)
        GlStateManager.enableAlpha()
        renderBar(mouseX, mouseY, partialTicks)
        GL11.glPopMatrix()
        super.drawScreen(mouseX, mouseY, partialTicks)

        if (!LiquidBounce.mainMenuPrep) {
            val animProgress = ((System.currentTimeMillis() - lastAnimTick).toFloat() / 2000F).coerceIn(0F, 1F)
            RenderUtils.drawRect(0F, 0F, width.toFloat(), height.toFloat(), Color(0F, 0F, 0F, 1F - animProgress))
            if (animProgress >= 1F)
                LiquidBounce.mainMenuPrep = true
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (!LiquidBounce.mainMenuPrep || mouseButton != 0) return

        if (isMouseHover(2F, height - 22F, 28F, height - 12F, mouseX, mouseY))
            useParallax = !useParallax

        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20F
        var index: Int = 0
        for (icon in ImageButton.values()) {
            if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY))
                when (index) {
                    0 -> mc.displayGuiScreen(GuiSelectWorld(this))
                    1 -> mc.displayGuiScreen(GuiMultiplayer(this))
                    2 -> mc.displayGuiScreen(GuiAltManager(this))
                    3 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
                    4 -> mc.displayGuiScreen(GuiBackground(this))
                    5 -> mc.shutdown()
                }

            index++
        }

        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun moveMouseEffect(mouseX: Int, mouseY: Int, strength: Float) {
        val mX = mouseX - width / 2
        val mY = mouseY - height / 2
        val xDelta = mX.toFloat() / (width / 2).toFloat()
        val yDelta = mY.toFloat() / (height / 2).toFloat()

        GL11.glTranslatef(xDelta * strength, yDelta * strength, 0F)
    }

    fun renderSwitchButton() {
        sliderX += if (useParallax) 2F else -2F
        if (sliderX > 12F) sliderX = 12F
        else if (sliderX < 0F) sliderX = 0F
        Fonts.font40.drawStringWithShadow("Animation", 28F, height - 25F, -1)
        RenderUtils.drawRoundedCornerRect(4F, height - 24F, 22F, height - 18F, 3F, if (useParallax) Color(0, 111, 255, 255).rgb else Color(140, 140, 140, 255).rgb)
        RenderUtils.drawRoundedCornerRect(2F + sliderX, height - 26F, 12F + sliderX, height - 16F, 5F, Color.white.rgb)
    }

    fun renderBar(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20F

        RenderUtils.drawRoundedCornerRect(staticX, staticY, staticX + 240F, staticY + 20F, 10F, Color(255, 255, 255, 100).rgb)

        var index: Int = 0
        var shouldAnimate = false
        var displayString: String? = null
        var moveX = 0F
        for (icon in ImageButton.values()) {
            if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY)) {
                shouldAnimate = true
                displayString = icon.buttonName
                moveX = staticX + 40F * index
            }
            index++
        }

        if (displayString != null)
            Fonts.font35.drawCenteredString(displayString!!, width / 2F, staticY + 30F, Color(0,255,0).rgb)
        else
            Fonts.font35.drawCenteredString(LiquidBounce.CLIENT_NAME, width / 2F, staticY + 30F, Color(255, 154, 31).rgb)
        Fonts.font35.drawCenteredString(LiquidBounce.CHOICE_TIPS, width / 2F, staticY + 40F, Color(255, 154, 31).rgb)

        if (shouldAnimate) {
            if (fade == 0F)
                slideX = moveX
            else
                slideX = AnimationUtils.animate(moveX, slideX, 0.5F * (1F - partialTicks))

            lastXPos = moveX

            fade += 10F
            if (fade >= 100F) fade = 100F
        } else {
            fade -= 10F
            if (fade <= 0F) fade = 0F

            slideX = AnimationUtils.animate(lastXPos, slideX, 0.5F * (1F - partialTicks))
        }

        if (fade != 0F)
            RenderUtils.drawRoundedCornerRect(slideX, staticY, slideX + 40F, staticY + 20F, 10F, Color(1F, 1F, 1F, fade / 100F * 0.6F).rgb)

        index = 0
        GlStateManager.disableAlpha()
        for (i in ImageButton.values()) {
            RenderUtils.drawImage2(i.texture, staticX + 40F * index + 11F, staticY + 1F, 18, 18)
            index++
        }
        GlStateManager.enableAlpha()
    }

    fun isMouseHover(x: Float, y: Float, x2: Float, y2: Float, mouseX: Int, mouseY: Int): Boolean = mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2

    enum class ImageButton(val buttonName: String, val texture: ResourceLocation) {
        Single("Singleplayer", ResourceLocation("fixxed/menu/singleplayer.png")),
        Multi("Multiplayer", ResourceLocation("fixxed/menu/multiplayer.png")),
        Alts("Alts", ResourceLocation("fixxed/menu/alt.png")),
        Settings("Settings", ResourceLocation("fixxed/menu/settings.png")),
        Mods("Background", ResourceLocation("fixxed/menu/mods.png")),
        Exit("Exit", ResourceLocation("fixxed/menu/exit.png"))
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}
/*
package net.ccbluex.liquidbounce.launch.data.legacyui

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.GuiBackground
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.i18n.LanguageManager
import net.ccbluex.liquidbounce.utils.extensions.drawCenteredString
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.*
import net.minecraft.client.resources.I18n
import net.minecraftforge.fml.client.GuiModList
import java.awt.Color

class GuiMainMenu : GuiScreen(), GuiYesNoCallback {
    override fun initGui() {
        val defaultHeight = (this.height / 3.5).toInt()

        this.buttonList.add(GuiButton(1, this.width / 2 - 50, defaultHeight, 100, 20, I18n.format("menu.singleplayer")))
        this.buttonList.add(GuiButton(2, this.width / 2 - 50, defaultHeight + 24, 100, 20, I18n.format("menu.multiplayer")))
        this.buttonList.add(GuiButton(100, this.width / 2 - 50, defaultHeight + 24 * 2, 100, 20, "%ui.altmanager%"))
        this.buttonList.add(GuiButton(103, this.width / 2 - 50, defaultHeight + 24 * 3, 100, 20, "%ui.mods%"))
        this.buttonList.add(GuiButton(102, this.width / 2 - 50, defaultHeight + 24 * 4, 100, 20, "%ui.background%"))
        this.buttonList.add(GuiButton(0, this.width / 2 - 50, defaultHeight + 24 * 5, 100, 20, I18n.format("menu.options")))
        this.buttonList.add(GuiButton(4, this.width / 2 - 50, defaultHeight + 24 * 6, 100, 20, I18n.format("menu.quit")))

        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)

        val bHeight = (this.height / 3.5).toInt()

        Gui.drawRect(width / 2 - 60, bHeight - 30, width / 2 + 60, bHeight + 174, Integer.MIN_VALUE)

        mc.fontRendererObj.drawCenteredString(LiquidBounce.CLIENT_NAME, (width / 2).toFloat(), (bHeight - 20).toFloat(), Color.WHITE.rgb, false)
        mc.fontRendererObj.drawString(LiquidBounce.CLIENT_VERSION, 3F, (height - mc.fontRendererObj.FONT_HEIGHT - 2).toFloat(), 0xffffff, false)
        "§cWebsite: §fhttps://${LiquidBounce.CLIENT_WEBSITE}/".also { str ->
            mc.fontRendererObj.drawString(str, (this.width - mc.fontRendererObj.getStringWidth(str) - 3).toFloat(), (height - mc.fontRendererObj.FONT_HEIGHT - 2).toFloat(), 0xffffff, false)
        }

        if(LiquidBounce.latest.isNotEmpty()) {
            val str = LanguageManager.getAndFormat("ui.update.released", LiquidBounce.latest)
            val start = width / 2f - (mc.fontRendererObj.getStringWidth(str) / 2f)
            RenderUtils.drawRect(start, 15f, start + mc.fontRendererObj.getStringWidth(str), 15f + mc.fontRendererObj.FONT_HEIGHT, Color.BLACK.rgb)
            mc.fontRendererObj.drawString(str, start, 15f, Color.WHITE.rgb, false)
        }

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            1 -> mc.displayGuiScreen(GuiSelectWorld(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            4 -> mc.shutdown()
            100 -> mc.displayGuiScreen(GuiAltManager(this))
            102 -> mc.displayGuiScreen(GuiBackground(this))
            103 -> mc.displayGuiScreen(GuiModList(this))
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}


 */



/*package net.ccbluex.liquidbounce.launch.data.legacyui

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.HUD.genshinImpactAnim
import net.ccbluex.liquidbounce.font.FontLoaders
import net.ccbluex.liquidbounce.ui.btn.TestBtn
import net.ccbluex.liquidbounce.ui.client.GuiBackground
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.i18n.LanguageManager
import net.ccbluex.liquidbounce.utils.FDP4nt1Sk1dUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.ccbluex.liquidbounce.utils.misc.MiscUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.*
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.GuiModList
import java.awt.Color
import java.io.File

class GuiMainMenu : GuiScreen(), GuiYesNoCallback {
    var drawed=false;
    var clicked=false;
    var displayed=false;
    fun drawBtns(){
        this.buttonList.add(TestBtn(1, (this.width / 2) - (130 / 2), this.height / 2 - 20, 130, 23,  I18n.format("menu.singleplayer"), null, 2,
            Color(20, 20, 20, 130)))

        this.buttonList.add(TestBtn(2, (this.width / 2) - (130 / 2), this.height / 2 + 10, 130, 23,  I18n.format("menu.multiplayer"), null, 2,
            Color(20, 20, 20, 130)))

        this.buttonList.add(TestBtn(100, (this.width / 2) - (130 / 2), this.height / 2 + 40, 130, 23,  LanguageManager.get("ui.altmanager"), null, 2,
            Color(20, 20, 20, 130)))

        this.buttonList.add(TestBtn(103, (this.width / 2) - (130 / 2), this.height / 2 + 70, 130, 23,  LanguageManager.get("ui.mods"), null, 2,
            Color(20, 20, 20, 130)))


        this.buttonList.add(TestBtn(4, this.width - 35, 10, 25, 25, I18n.format("menu.quit"), ResourceLocation("fdpclient/imgs/icon/quit.png"), 2,
            Color(20, 20, 20, 130)))

        this.buttonList.add(TestBtn(0, this.width - 65, 10, 25, 25, I18n.format("menu.options"), ResourceLocation("fdpclient/imgs/icon/setting.png"), 2,
            Color(20, 20, 20, 130)))


        this.buttonList.add(TestBtn(104, this.width - 95, 10, 25, 25, I18n.format("ui.background"), ResourceLocation("fdpclient/imgs/icon/wallpaper.png"), 2,
            Color(20, 20, 20, 130)))

        this.buttonList.add(TestBtn(102, this.width - 125, 10, 25, 25, "Announcement", ResourceLocation("fdpclient/imgs/icon/announcement.png"), 2,
            Color(20, 20, 20, 130)))

        this.buttonList.add(TestBtn(514, this.width - 155, 10, 25, 25, "Discord", ResourceLocation("fdpclient/imgs/icon/discord.png"), 2,
            Color(20, 20, 20, 130)))

        this.buttonList.add(TestBtn(114, this.width - 185, 10, 25, 25, "Website", ResourceLocation("fdpclient/imgs/icon/website.png"), 2,
            Color(20, 20, 20, 130)))


        this.buttonList.add(TestBtn(191, 20, 10, 25, 25, "Change exterior", ResourceLocation("fdpclient/imgs/icon/moon-night.png"), 2,
            Color(20, 20, 20, 130)))

        drawed=true;
    }
    /* For modification, please keep "Designed by XiGua" */

    override fun initGui() {
        /*
        val defaultHeight = (this.height / 3.5).toInt()
        try {
            LiquidBounce.VERIFY = FDP4nt1Sk1dUtils.decrypt(File("./", "FDPProtect").readText())
        }catch (e:Exception){
            System.out.println("Cant load FDPProtect")
        }
        */

        //我急了，写破防了，写了7个小时没写好
        Thread {
            if(LiquidBounce.CLIENTTEXT.contains("Waiting") || LiquidBounce.CLIENTTEXT.contains("Oops")) {
                try {
                    LiquidBounce.CLIENTTEXT = HttpUtils.get(LiquidBounce.url_changelogs)
                } catch (e: Exception) {
                    try {
                        LiquidBounce.CLIENTTEXT = HttpUtils.get(LiquidBounce.url_changelogs)
                    } catch (e: Exception) {
                        LiquidBounce.CLIENTTEXT = "What's up? :(\$Fuck gitee.com\$140\$80"
                    }
                }
            }
        }.start()

        drawBtns()
        //this.buttonList.add(TestBtn(102, this.width - 95, 10, 25, 25, LanguageManager.get("ui.background"), ResourceLocation("fdpclient/imgs/icon/wallpaper.png"), 2,
        //    Color(20, 20, 20, 130)))


        /*
        this.buttonList.add(GuiButton(1, this.width / 2 - 50, defaultHeight, 100, 20, I18n.format("menu.singleplayer")))
        this.buttonList.add(GuiButton(2, this.width / 2 - 50, defaultHeight + 24, 100, 20, I18n.format("menu.multiplayer")))
        this.buttonList.add(GuiButton(100, this.width / 2 - 50, defaultHeight + 24 * 2, 100, 20, "%ui.altmanager%"))
        this.buttonList.add(GuiButton(103, this.width / 2 - 50, defaultHeight + 24 * 3, 100, 20, "%ui.mods%"))
        this.buttonList.add(GuiButton(102, this.width / 2 - 50, defaultHeight + 24 * 4, 100, 20, "%ui.background%"))
        this.buttonList.add(GuiButton(0, this.width / 2 - 50, defaultHeight + 24 * 5, 100, 20, I18n.format("menu.options")))
        this.buttonList.add(GuiButton(4, this.width / 2 - 50, defaultHeight + 24 * 6, 100, 20, I18n.format("menu.quit")))
        */
        super.initGui()
        }

override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
    drawBackground(0)
    val defaultHeight = (this.height).toFloat()
    val defaultWidth = (this.width).toFloat()
    //RenderUtils.drawCircle(defaultWidth/2,defaultHeight/2 + 60F, 150F,Color(0,0,0,100).rgb);
    val i=0;
    val defaultHeight1 = (this.height).toDouble()
    val defaultWidth1 = (this.width).toDouble()
    if (genshinImpactAnim.get()) RenderUtils.drawImage(LiquidBounce.venti,defaultWidth1.toInt()-(0.6*defaultWidth1).toInt() ,defaultHeight1.toInt()-(0.3*defaultWidth1).toInt(),(0.6*defaultWidth1).toInt(),(0.3*defaultWidth1).toInt())
    FontLoaders.F40.drawCenteredString(LiquidBounce.CLIENT_NAME,this.width.toDouble()/2,this.height.toDouble()/2 - 60,Color(255,255,255,200).rgb)
    
    /* For modification, please keep "Designed by XiGua" */
    //FDPProtect.setVerify("1")

    FontLoaders.F16.drawString(LiquidBounce.hudtip_right,10f,this.height-15f,Color(255,255,255,170).rgb)
    FontLoaders.F16.drawString(LiquidBounce.VERIFY,10f,this.height-25f,if(LiquidBounce.VERIFY.contains("Insecure")) Color(255,58,58,170).rgb else Color(255,255,255,170).rgb)
    FontLoaders.F16.drawString(LiquidBounce.versionMsg,this.width - FontLoaders.F16.getStringWidth(LiquidBounce.versionMsg) - 10F,this.height-15f,Color(255,255,255,170).rgb)

    //
    /*val bHeight = (this.height / 3.5).toInt()

    Gui.drawRect(width / 2 - 60, bHeight - 30, width / 2 + 60, bHeight + 174, Integer.MIN_VALUE)

    mc.fontRendererObj.drawCenteredString(LiquidBounce.CLIENT_NAME, (width / 2).toFloat(), (bHeight - 20).toFloat(), Color.WHITE.rgb, false)
    mc.fontRendererObj.drawString(LiquidBounce.CLIENT_VERSION, 3F, (height - mc.fontRendererObj.FONT_HEIGHT - 2).toFloat(), 0xffffff, false)
    "§cWebsite: §fhttps://${LiquidBounce.CLIENT_WEBSITE}/".also { str ->
        mc.fontRendererObj.drawString(str, (this.width - mc.fontRendererObj.getStringWidth(str) - 3).toFloat(), (height - mc.fontRendererObj.FONT_HEIGHT - 2).toFloat(), 0xffffff, false)
    }

    if(LiquidBounce.latest != LiquidBounce.CLIENT_VERSION && LiquidBounce.latest.isNotEmpty()) {
        val str = LanguageManager.getAndFormat("ui.update.released", LiquidBounce.latest)
        val start = width / 2f - (mc.fontRendererObj.getStringWidth(str) / 2f)
        RenderUtils.drawRect(start, 15f, start + mc.fontRendererObj.getStringWidth(str), 15f + mc.fontRendererObj.FONT_HEIGHT, Color.BLACK.rgb)
        mc.fontRendererObj.drawString(str, start, 15f, Color.WHITE.rgb, false)
    }*/


    //displayed = false
    try {
        if (!displayed) {
            var back = Layer.draw(
                defaultWidth.toInt(),
                defaultHeight1.toInt(),
                LiquidBounce.CLIENTTEXT.split("$")[2].toFloat(),
                LiquidBounce.CLIENTTEXT.split("$")[3].toFloat(),
                LiquidBounce.CLIENTTEXT.split("$")[0],
                LiquidBounce.CLIENTTEXT.split("$")[1],
                255,
                mouseX,
                mouseY,
                clicked
            )
            if (back == 1) {
                drawed = false;
                buttonList.removeAll(buttonList)
            } else if (back == 2) {
                displayed = true
                drawBtns()
            }
            if (drawed && back != 1) {
                //drawBtns()
            }
            clicked = false;
        }else{
            if(!drawed){
                drawBtns()
            }
        }
    }catch (e:Exception){
        //e.printStackTrace()
    }
    super.drawScreen(mouseX, mouseY, partialTicks)
}
override fun mouseClicked(p_mouseClicked_1_: Int, i2: Int, i3: Int) {
    clicked=true;
    super.mouseClicked(p_mouseClicked_1_,i2 ,i3)
}
override fun actionPerformed(button: GuiButton) {
when (button.id) {
    0 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
    1 -> mc.displayGuiScreen(GuiSelectWorld(this))
    2 -> mc.displayGuiScreen(GuiMultiplayer(this))
    4 -> mc.shutdown()
    100 -> mc.displayGuiScreen(GuiAltManager(this))
    102 -> displayed=false
    103 -> mc.displayGuiScreen(GuiModList(this))
    104 -> mc.displayGuiScreen(GuiBackground(this))
    514 -> MiscUtils.showURL("https://${LiquidBounce.CLIENT_WEBSITE}/discord.html")
    114 -> MiscUtils.showURL("https://${LiquidBounce.CLIENT_WEBSITE}")
    191 -> LiquidBounce.Darkmode=!LiquidBounce.Darkmode
}
}

override fun keyTyped(typedChar: Char, keyCode: Int) {}
}


 */