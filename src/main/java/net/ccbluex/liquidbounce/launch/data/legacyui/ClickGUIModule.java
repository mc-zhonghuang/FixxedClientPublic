/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.launch.data.legacyui;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.ClickGui;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.*;
import net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.novoline.ClickyUI;
import net.ccbluex.liquidbounce.launch.options.LegacyUiLaunchOption;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.CLIENT, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUIModule extends Module {
    private final ListValue styleValue = new ListValue("Style", new String[] {"Fixxed", "LiquidBounce", "Null", "Slowly", "Black", "Novoline(BUG)"}, "Fixxed") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };

    public final FloatValue scaleValue = new FloatValue("Scale", 0.75F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);

    public static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public static final IntegerValue colorRedValue = (IntegerValue) new IntegerValue("R", 248, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorGreenValue = (IntegerValue) new IntegerValue("G", 248, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorBlueValue = (IntegerValue) new IntegerValue("B", 255, 0, 255).displayable(() -> !colorRainbow.get());

    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.INSTANCE.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    @Override
    public void onEnable() {
        if(styleValue.get().contains("Novoline(BUG)")) {
            mc.displayGuiScreen(new ClickyUI());
            this.setState(false);
        }else {
            updateStyle();
            mc.displayGuiScreen(LegacyUiLaunchOption.clickGui);
        }
    }

    private void updateStyle() {
        switch(styleValue.get().toLowerCase()) {
            case "fixxed":
                LegacyUiLaunchOption.clickGui.style = new FixxedStyle();
                break;
            case "liquidbounce":
                LegacyUiLaunchOption.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                LegacyUiLaunchOption.clickGui.style = new NullStyle();
                break;
            case "slowly":
                LegacyUiLaunchOption.clickGui.style = new SlowlyStyle();
                break;
            case "black":
                LegacyUiLaunchOption.clickGui.style = new BlackStyle();
                break;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();

        if (packet instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ClickGui) {
            event.cancelEvent();
        }
    }
}