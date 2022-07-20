package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import me.glass20.utils.arWingUtils

@ModuleInfo(name = "Wings", category = ModuleCategory.RENDER)
class Wings : Module() {
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val renderWings = arWingUtils()
        renderWings.renderWings(event.partialTicks)
    }
}