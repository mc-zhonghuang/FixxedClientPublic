/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.value

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.minecraft.client.gui.FontRenderer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

abstract class ValueLB<T>(val name: String, protected var value: T, var canDisplay: () -> Boolean) {

    fun set(newValue: T) {
        if (newValue == value) return

        val oldValue = getLB()

        try {
            onChange(oldValue, newValue)
            changeValueLB(newValue)
            onChanged(oldValue, newValue)
            LiquidBounce.configManager.smartSave()
        } catch (e: Exception) {
            ClientUtils.logError("[ValueSystem ($name)]: ${e.javaClass.name} (${e.message}) [$oldValue >> $newValue]")
        }
    }

    fun getLB() = value

    open fun changeValueLB(value: T) {
        this.value = value
    }

    abstract fun toJsonLB(): JsonElement?
    abstract fun fromJsonLB(element: JsonElement)

    protected open fun onChange(oldValue: T, newValue: T) {}
    protected open fun onChanged(oldValue: T, newValue: T) {}

}

/**
 * Bool value represents a value with a boolean
 */
open class BoolValueLB(name: String, value: Boolean, displayable: () -> Boolean) : ValueLB<Boolean>(name, value, displayable) {

    constructor(name: String, value: Boolean): this(name, value, { true } )

    override fun toJsonLB() = JsonPrimitive(value)

    override fun fromJsonLB(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asBoolean || element.asString.equals("true", ignoreCase = true)
    }

}

/**
 * Integer value represents a value with a integer
 */
open class IntegerValueLB(name: String, value: Int, val minimum: Int = 0, val maximum: Int = Integer.MAX_VALUE, val suffix: String, displayable: () -> Boolean)
    : ValueLB<Int>(name, value, displayable) {

    constructor(name: String, value: Int, minimum: Int, maximum: Int, displayable: () -> Boolean): this(name, value, minimum, maximum, "", displayable)
    constructor(name: String, value: Int, minimum: Int, maximum: Int, suffix: String): this(name, value, minimum, maximum, suffix, { true } )
    constructor(name: String, value: Int, minimum: Int, maximum: Int): this(name, value, minimum, maximum, { true } )

    fun set(newValue: Number) {
        set(newValue.toInt())
    }

    override fun toJsonLB() = JsonPrimitive(value)

    override fun fromJsonLB(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asInt
    }

}

/**
 * Float value represents a value with a float
 */
open class FloatValueLB(name: String, value: Float, val minimum: Float = 0F, val maximum: Float = Float.MAX_VALUE, val suffix: String, displayable: () -> Boolean)
    : ValueLB<Float>(name, value, displayable) {

    constructor(name: String, value: Float, minimum: Float, maximum: Float, displayable: () -> Boolean): this(name, value, minimum, maximum, "", displayable)
    constructor(name: String, value: Float, minimum: Float, maximum: Float, suffix: String): this(name, value, minimum, maximum, suffix, { true } )
    constructor(name: String, value: Float, minimum: Float, maximum: Float): this(name, value, minimum, maximum, { true } )

    fun set(newValue: Number) {
        set(newValue.toFloat())
    }

    override fun toJsonLB() = JsonPrimitive(value)

    override fun fromJsonLB(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asFloat
    }

}

/**
 * Text value represents a value with a string
 */
open class TextValueLB(name: String, value: String, displayable: () -> Boolean) : ValueLB<String>(name, value, displayable) {

    constructor(name: String, value: String): this(name, value, { true } )

    override fun toJsonLB() = JsonPrimitive(value)

    override fun fromJsonLB(element: JsonElement) {
        if (element.isJsonPrimitive)
            value = element.asString
    }
}

/**
 * Font value represents a value with a font
 */
class FontValueLB(valueName: String, value: FontRenderer, displayable: () -> Boolean) : ValueLB<FontRenderer>(valueName, value, displayable) {

    constructor(valueName: String, value: FontRenderer): this(valueName, value, { true } )

    override fun toJsonLB(): JsonElement? {
        val fontDetails = Fonts.getFontDetails(value) ?: return null
        val valueObject = JsonObject()
        valueObject.addProperty("fontName", fontDetails[0] as String)
        valueObject.addProperty("fontSize", fontDetails[1] as Int)
        return valueObject
    }

    override fun fromJsonLB(element: JsonElement) {
        if (!element.isJsonObject) return
        val valueObject = element.asJsonObject
        value = Fonts.getFontRenderer(valueObject["fontName"].asString, valueObject["fontSize"].asInt)
    }
}

/**
 * Block value represents a value with a block
 */
class BlockValueLB(name: String, value: Int, displayable: () -> Boolean) : IntegerValueLB(name, value, 1, 197, displayable) {
    constructor(name: String, value: Int): this(name, value, { true } )
}

/**
 * List value represents a selectable list of values
 */
open class ListValueLB(name: String, val values: Array<String>, value: String, displayable: () -> Boolean) : ValueLB<String>(name, value, displayable) {

    constructor(name: String, values: Array<String>, value: String): this(name, values, value, { true } )

    @JvmField
    var openList = false

    init {
        this.value = value
    }

    operator fun contains(string: String?): Boolean {
        return Arrays.stream(values).anyMatch { s: String -> s.equals(string, ignoreCase = true) }
    }

    override fun changeValueLB(value: String) {
        for (element in values) {
            if (element.equals(value, ignoreCase = true)) {
                this.value = element
                break
            }
        }
    }

    override fun toJsonLB() = JsonPrimitive(value)

    override fun fromJsonLB(element: JsonElement) {
        if (element.isJsonPrimitive) changeValueLB(element.asString)
    }


}