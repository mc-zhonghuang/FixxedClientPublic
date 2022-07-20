/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */

package net.ccbluex.liquidbounce.utils.misc

import com.google.common.io.ByteStreams
import net.ccbluex.liquidbounce.utils.ClientUtils
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
object HttpUtils {

    const val DEFAULT_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"

    init {
        HttpURLConnection.setFollowRedirects(true)
    }

    fun make(
        url: String,
        method: String,
        data: String = "",
        agent: String = DEFAULT_AGENT
    ): HttpURLConnection {
        val httpConnection = URL(url).openConnection() as HttpURLConnection

        httpConnection.requestMethod = method
        httpConnection.connectTimeout = 30 * 1000
        httpConnection.readTimeout = 30 * 1000

        httpConnection.setRequestProperty("User-Agent", agent)

        httpConnection.instanceFollowRedirects = true
        httpConnection.doOutput = true

        if (data.isNotEmpty()) {
            val dataOutputStream = DataOutputStream(httpConnection.outputStream)
            dataOutputStream.writeBytes(data)
            dataOutputStream.flush()
        }

        return httpConnection
    }

    fun request(
        url: String,
        method: String,
        data: String = "",
        agent: String = DEFAULT_AGENT
    ): String {
        val connection = make(url, method, data, agent)

        return connection.inputStream.reader().readText()
    }

    fun requestStream(
        url: String,
        method: String,
        agent: String = DEFAULT_AGENT
    ): InputStream? {
        val connection = make(url, method, agent)

        return connection.inputStream
    }

    fun download(url: String, file: File) {
        ClientUtils.logWarn("Downloading $url to ${file.absolutePath}")
        FileOutputStream(file).use { ByteStreams.copy(make(url, "GET").inputStream, it) }
    }

    fun get(url: String) = request(url, "GET")

    fun post(url: String, data: String) = request(url, "POST", data = data)
}