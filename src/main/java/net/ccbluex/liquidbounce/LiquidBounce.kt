/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce

//import org.json.JSONObject
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.glass20.idk.DownLoadFileUtils
import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.macro.MacroManager
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.CombatManager
import net.ccbluex.liquidbounce.features.special.ServerSpoof
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.file.config.ConfigManager
import net.ccbluex.liquidbounce.launch.EnumLaunchFilter
import net.ccbluex.liquidbounce.launch.LaunchFilterInfo
import net.ccbluex.liquidbounce.launch.LaunchOption
import net.ccbluex.liquidbounce.launch.data.GuiLaunchOptionSelectMenu
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.ui.cape.GuiCapeManager
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.keybind.KeyBindManager
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.ui.font.FontsGC
import net.ccbluex.liquidbounce.ui.i18n.LanguageManager
import net.ccbluex.liquidbounce.ui.sound.TipSoundManager
import net.ccbluex.liquidbounce.utils.ClassUtils
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.Display
import java.io.File
import java.util.*
import javax.swing.JOptionPane
import kotlin.system.exitProcess

object LiquidBounce {


    // Client information
    const val CLIENT_NAME = "FixxedClient"
    const val CLIENT_NAME_SHORT = "Fixxed"
    const val VERSIONTYPE = "Beta" //Preview Beta Release
    const val CLIENT_VERSION = "1.6"

    @JvmStatic
    var VERIFY = "$CLIENT_NAME $VERSIONTYPE Version-$CLIENT_VERSION"
    var CLIENTTEXT = "Waiting"
    var Darkmode = true

    var COLORED_NAME = "§c§lFixxed§6§lClient"
    var CLIENT_CREATOR = "GLASS20 & MC_ZhongHuang"
    var CLIENT_WEBSITE = "Fixxed.tk"
    var CLIENT_WEBSITE2 = "Fixxed,tk"
    const val MINECRAFT_VERSION = "1"
    var DEV_TIPS = "Don't go at the red light"
    var BUILDTIPS = "Hacked by Dimples#1337"
    var QQ_GROUP = 879803291
    var DEV_SAY = "Powered by by GLASS20 & ZhongHuang"
    var CHOICE_TIPS = "Good Luck"

    val venti = ResourceLocation("fixxed/imgs/GenshinImpact/venti.png")
    val lumine = ResourceLocation("fixxed/imgs/GenshinImpact/lumine.png")

    @JvmField

    val gitInfo = Properties().also {
        val inputStream = LiquidBounce::class.java.classLoader.getResourceAsStream("git.properties")
        if(inputStream != null) {
            it.load(inputStream)
        } else {
            it["git.branch"] = "unofficial" // 用默认值填充，否则会出现空指针异常
        }
    }


    @JvmField
    val CLIENT_BRANCH = (gitInfo["git.branch"] ?: "unknown").let {
        if(it == "main") "Main Reborn" else it
    }

    var isStarting = true
    var isLoadingConfig = true
    var mainMenuPrep = false

    // Update information
    private var latestver = ""
    private var isDisabled = "true"
    // Managers
    lateinit var moduleManager: ModuleManager
    @JvmStatic
    //var fdpProtectManager = FDPProtectManager()
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var tipSoundManager: TipSoundManager
    lateinit var combatManager: CombatManager
    lateinit var macroManager: MacroManager
    lateinit var configManager: ConfigManager

    // Some UI things
    lateinit var hud: HUD
    lateinit var mainMenu: GuiScreen
    lateinit var keyBindManager: KeyBindManager

    // Menu Background
    var background: ResourceLocation? = ResourceLocation("fixxed/imgs/background.jpg")

    val launchFilters = mutableListOf<EnumLaunchFilter>()
    private val dynamicLaunchOptions: Array<LaunchOption>
        get() = ClassUtils.resolvePackage("${LaunchOption::class.java.`package`.name}.options", LaunchOption::class.java)
            .filter {
                val annotation = it.getDeclaredAnnotation(LaunchFilterInfo::class.java)
                if (annotation != null) {
                    return@filter annotation.filters.toMutableList() == launchFilters
                }
                false
            }
            .map { try { it.newInstance() } catch (e: IllegalAccessException) { ClassUtils.getObjectInstance(it) as LaunchOption } }.toTypedArray()

    /**
     * 你看看Coding.net正不正常吧
     */
    fun downloadnewversion(){
        Display.setTitle(VERIFY+"　Trying to download the latest version...")

        val file1 = File("C:/FixxedClient/Download/$VERSIONTYPE/");
        if (file1.mkdirs()) {
            ClientUtils.logInfo("Folder created successfully-C:/FixxedClient/Download/$VERSIONTYPE/")
        }else{
            ClientUtils.logInfo("Unable to create new folder-C:/FixxedClient/Download/$VERSIONTYPE/")
        }

        //val filePath = "https://glass20.coding.net/p/FixxedClient/d/FixxedClient/git/raw/master/$VERSIONTYPE/FixxedClient-$latestver.jar"
        val filePath = "https://glass20.coding.net/p/FixxedClient/d/FixxedClientDownload/git/raw/master/$VERSIONTYPE/FixxedClient-$latestver.jar"
        val isdownload = DownLoadFileUtils.downLoadByUrl(filePath, "C:/FixxedClient/Download/$VERSIONTYPE/")
        if(!isdownload){
            Display.setTitle("$VERIFY Unable to download jar file")
            ClientUtils.logError("Unable to download jar file")
            JOptionPane.showConfirmDialog(null,"无法下载最新版本","提示",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
        }else{
            ClientUtils.logInfo("Successfully downloaded the latest jar from the cloud to the local!")
            Display.setTitle("$VERIFY | Please use the latest version")
            Runtime.getRuntime().exec("cmd /c start explorer C:\\FixxedClient\\Download\\$VERSIONTYPE\\");
            /*
            while(true){
                Thread.sleep(1000);
            }
             */
        }
    }

    fun checkupdate(){
        ClientUtils.logDebug("[DisabledCheck] Start")
        var fuckerbug = false
        try{
            val jsonObj = JsonParser()
                .parse(HttpUtils.get("https://fixxed.tk/FixxedCloud/"+VERSIONTYPE+"Disabled.json"))
            if (jsonObj is JsonObject && jsonObj.has(CLIENT_VERSION)) {
                latestver = jsonObj[CLIENT_VERSION].toString()
                ClientUtils.logDebug("[DisabledCheck] latestver = $latestver")
                if(latestver == "true") {
                    fuckerbug = true
                    VERIFY = "$VERIFY　Your version has been deactivated"
                    Display.setTitle(VERIFY)
                    JOptionPane.showConfirmDialog(null,"你的版本已停用","提示",JOptionPane.OK_OPTION);
                    stopClient()
                }
            }
        }catch (exception: Throwable){
            if(fuckerbug == false){
                JOptionPane.showConfirmDialog(null,"无法连接至服务器","提示",JOptionPane.OK_OPTION);
            }
            stopClient()
        }

        try {
            // 从云读取json版本
            val jsonObj = JsonParser()
                .parse(HttpUtils.get("https://fixxed.tk/FixxedCloud/version.json"))

            // 检查json是否为有效对象并具有当前的minecraft版本
            if (jsonObj is JsonObject && jsonObj.has(VERSIONTYPE)) {
                // 获取官方最新客户端版本
                latestver = jsonObj[VERSIONTYPE].toString()
                ClientUtils.logDebug("Build from cloud: " + jsonObj[VERSIONTYPE].toString())
                ClientUtils.logDebug("latestver = " + latestver)
                if(latestver != CLIENT_VERSION) {
                    VERIFY = "$VERIFY　You are using an older version -Latest version:$latestver"
                    Display.setTitle(VERIFY)
                    ClientUtils.logInfo("Trying to download the latest jar")
                    downloadnewversion()
                }
                else{
                    VERIFY = "$VERIFY　You are using the latest version"
                    Display.setTitle(VERIFY)
                }
            }

        } catch (exception: Throwable) { // Print throwable to console
            VERIFY = "$VERIFY　Unable to get the latest updates"
            Display.setTitle(VERIFY)
            ClientUtils.logError("Failed to check for new updates.", exception)
        }
    }

    private fun updateclientdate(){
        try {
            // 从云读取json
            val jsonObj = JsonParser()
                .parse(HttpUtils.get("https://fixxed.tk/FixxedCloud/FixxedClient.json"))

            if (jsonObj is JsonObject && jsonObj.has("COLORED_NAME")) {
                COLORED_NAME = jsonObj["COLORED_NAME"].toString().replace("\"","")
                ClientUtils.logDebug("Set <COLORED_NAME> = $COLORED_NAME")
            }
            if (jsonObj is JsonObject && jsonObj.has("CLIENT_CREATOR")) {
                CLIENT_CREATOR = jsonObj["CLIENT_CREATOR"].toString().replace("\"","")
                ClientUtils.logDebug("Set <CLIENT_CREATOR> = $CLIENT_CREATOR")
            }
            if (jsonObj is JsonObject && jsonObj.has("CLIENT_WEBSITE")) {
                CLIENT_WEBSITE = jsonObj["CLIENT_WEBSITE"].toString().replace("\"","")
                ClientUtils.logDebug("Set <CLIENT_WEBSITE> = $CLIENT_WEBSITE")
            }
            if (jsonObj is JsonObject && jsonObj.has("CLIENT_WEBSITE2")) {
                CLIENT_WEBSITE2 = jsonObj["CLIENT_WEBSITE2"].toString().replace("\"","")
                ClientUtils.logDebug("Set <CLIENT_WEBSITE2> = $CLIENT_WEBSITE2")
            }
            if (jsonObj is JsonObject && jsonObj.has("DEV_TIPS")) {
                DEV_TIPS = jsonObj["DEV_TIPS"].toString().replace("\"","")
                ClientUtils.logDebug("Set <DEV_TIPS> = $DEV_TIPS")
            }
            if (jsonObj is JsonObject && jsonObj.has("BUILDTIPS")) {
                BUILDTIPS = jsonObj["BUILDTIPS"].toString().replace("\"","")
                ClientUtils.logDebug("Set <BUILDTIPS> = $BUILDTIPS")
            }
            if (jsonObj is JsonObject && jsonObj.has("QQ_GROUP")) {
                QQ_GROUP = Integer.parseInt(jsonObj["QQ_GROUP"].toString())
                ClientUtils.logDebug("Set <QQ_GROUP> = $QQ_GROUP")
            }
            if (jsonObj is JsonObject && jsonObj.has("DEV_SAY")) {
                DEV_SAY = jsonObj["DEV_SAY"].toString().replace("\"","")
                ClientUtils.logDebug("Set <DEV_SAY> = $DEV_SAY")
            }
            if (jsonObj is JsonObject && jsonObj.has("CHOICE_TIPS")) {
                CHOICE_TIPS = jsonObj["CHOICE_TIPS"].toString().replace("\"","")
                ClientUtils.logDebug("Set <CHOICE_TIPS> = $CHOICE_TIPS")
            }


        } catch (exception: Throwable) { // Print throwable to console
            ClientUtils.logError("Can't git cloud Client Date", exception)
        }
    }


    private fun checkVersionDisabled(){
        try {
            // 从云读取json
            val jsonObj = JsonParser()
                .parse(HttpUtils.get("https://fixxed.tk/FixxedCloud/VersionDisabled.json"))

            if (jsonObj is JsonObject && jsonObj.has(VERSIONTYPE)) {
                isDisabled = jsonObj[VERSIONTYPE].toString()
                ClientUtils.logDebug("[checkVersionDisabled] " + jsonObj[VERSIONTYPE].toString())
                ClientUtils.logDebug("[checkVersionDisabled] latestver = $latestver")
                if(isDisabled == "false") {
                    stopClient()
                }
            }

        } catch (exception: Throwable) { // 可丢弃到控制台的打印
            ClientUtils.logError("CheckVersionDisabled Error!", exception)
            stopClient()
        }

    }

    /**
     * Execute if client will be started
     */
    fun initClient() {

        updateclientdate()

        ClientUtils.logInfo("Loading $CLIENT_NAME $CLIENT_VERSION, by $CLIENT_CREATOR")
        val startTime = System.currentTimeMillis()

        // 创建文件管理器
        fileManager = FileManager()
        configManager = ConfigManager()

        // 创建事件管理器
        eventManager = EventManager()

        // 加载语言
        LanguageManager.switchLanguage(Minecraft.getMinecraft().gameSettings.language)

        // 注册侦听器
        eventManager.registerListener(RotationUtils())
        eventManager.registerListener(AntiForge)
        eventManager.registerListener(InventoryUtils)
        eventManager.registerListener(ServerSpoof)

        // 创建命令管理器
        commandManager = CommandManager()

        fileManager.loadConfigs(fileManager.accountsConfig, fileManager.friendsConfig, fileManager.specialConfig)

        // 加载客户端字体
        Fonts.loadFonts()
        eventManager.registerListener(FontsGC)

        macroManager = MacroManager()
        eventManager.registerListener(macroManager)

        // 设置模块管理器和注册模块
        moduleManager = ModuleManager()
        moduleManager.registerModules()

        try {
            // ScriptManager，启用脚本时将延迟加载重新映射
            scriptManager = ScriptManager()
            scriptManager.loadScripts()
            scriptManager.enableScripts()
        } catch (throwable: Throwable) {
            ClientUtils.logError("Failed to load scripts.", throwable)
        }

        // Register commands
        commandManager.registerCommands()

        tipSoundManager = TipSoundManager()

        // KeyBindManager
        keyBindManager = KeyBindManager()

        // bstats.org user count display
        ClientUtils.buildMetrics()

        combatManager = CombatManager()
        eventManager.registerListener(combatManager)

        GuiCapeManager.load()

        //mainMenu = GuiLogin() //GuiLaunchOptionSelectMenu() HWID CHECK hehe
        mainMenu = GuiLaunchOptionSelectMenu()

        // Set HUD
        hud = HUD.createDefault()

        fileManager.loadConfigs(fileManager.hudConfig, fileManager.xrayConfig)


        checkVersionDisabled()
        checkupdate()

        ClientUtils.logInfo("$CLIENT_NAME $CLIENT_VERSION loaded in ${(System.currentTimeMillis() - startTime)}ms!")
    }
    /**
     * Execute if client ui type is selected
     */
    fun startClient() {
        dynamicLaunchOptions.forEach {
            it.start()
        }

        // Load configs
        configManager.loadLegacySupport()
        configManager.loadConfigSet()

        // Set is starting status
        isStarting = false
        isLoadingConfig = false

        ClientUtils.logInfo("$CLIENT_NAME $CLIENT_VERSION started!")
    }

    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        if (!isStarting && !isLoadingConfig) {
            ClientUtils.logInfo("Shutting down $CLIENT_NAME $CLIENT_VERSION!")

            // Call client shutdown
            eventManager.callEvent(ClientShutdownEvent())

            // Save all available configs
            GuiCapeManager.save()
            configManager.save(true, true)
            fileManager.saveAllConfigs()

            dynamicLaunchOptions.forEach {
                it.stop()
            }
        }
    }
}
