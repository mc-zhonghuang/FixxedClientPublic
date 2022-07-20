package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class RenderUtils8 extends MinecraftInstance {

    private static final Map glCapMap = new HashMap();
    private static int counts = 0;
    public static int deltaTime;
    public static int deltaTimer2;
    private static final int[] DISPLAY_LISTS_2D = new int[4];
    private static Frustum frustrum;

    public static void drawRect2(float x, float y, float x2, float y2, int color, boolean shadow) {
        float f = 1.0F;

        if (shadow) {
            for (int i = 0; i < 2; ++i) {
                drawRect(x - f, y - f, x2 + f, y2 + f, (new Color(0, 0, 0, 50)).getRGB());
                f = (float) ((double) f - 0.5D);
            }
        }

        drawRect(x, y, x2, y2, color);
    }

    public static void quickDrawHead(ResourceLocation skin, int x, int y, int width, int height) {
        RenderUtils.mc.getTextureManager().bindTexture(skin);
        drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
        drawScaledCustomSizeModalRect(x, y, 40.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
    }

    public static void Gamesense(double x, double y, double x1, double y1, double size, float color1, float color2, float color3) {
        rectangleBordered(x, y, x1 + size, y1 + size, 0.5D, Colors2.getColor(90), Colors2.getColor(0));
        rectangleBordered(x + 1.0D, y + 1.0D, x1 + size - 1.0D, y1 + size - 1.0D, 1.0D, Colors2.getColor(90), Colors2.getColor(61));
        rectangleBordered(x + 2.5D, y + 2.5D, x1 + size - 2.5D, y1 + size - 2.5D, 0.5D, Colors2.getColor(61), Colors2.getColor(0));
        drawGradientSideways(x + size * 3.0D, y + 3.0D, x1 - size * 2.0D, y + 4.0D, (int) color2, (int) color3);
    }

    public static int height() {
        return (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight();
    }

    public static void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = -150.0F;
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        Minecraft.getMinecraft();
        renderItem.renderItemOverlays(RenderUtils.mc.fontRendererObj, stack, x, y);
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        double s = 0.5D;

        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.popMatrix();
    }

    public static void drawEntityOtherBox(Entity entity, Color color, boolean outline) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        Timer timer = RenderUtils.mc.timer;

        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        disableGlCap(new int[] { 3553, 2929});
        GL11.glDepthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) timer.renderPartialTicks - renderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) timer.renderPartialTicks - renderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) timer.renderPartialTicks - renderManager.renderPosZ;
        AxisAlignedBB entityBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.minX - entity.posX + x - 0.05D, entityBox.minY - entity.posY + y, entityBox.minZ - entity.posZ + z - 0.05D, entityBox.maxX - entity.posX + x + 0.05D, entityBox.maxY - entity.posY + y + 0.15D, entityBox.maxZ - entity.posZ + z + 0.05D);

        if (outline) {
            GL11.glLineWidth(1.0F);
            enableGlCap(2848);
            glColor(color.getRed(), color.getGreen(), color.getBlue(), 100);
            drawSelectionBoundingBox(axisAlignedBB);
        }

        glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        resetCaps();
    }
    private void drawCircle(EntityLivingBase entity, float partialTicks, double rad) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GLUtils.startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0F);
        GL11.glBegin(3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - RenderUtils.mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - RenderUtils.mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - RenderUtils.mc.getRenderManager().viewerPosZ;

        for (int i = 0; i <= 360; ++i) {
            Color rainbow = new Color(Color.HSBtoRGB((float) ((double) RenderUtils.mc.thePlayer.ticksExisted / 70.0D + Math.sin((double) i / 50.0D * 1.75D)) % 1.0F, 0.7F, 1.0F));

            GL11.glColor3f((float) rainbow.getRed() / 255.0F, (float) rainbow.getGreen() / 255.0F, (float) rainbow.getBlue() / 255.0F);
            GL11.glVertex3d(x + rad * Math.cos((double) i * 6.283185307179586D / 45.0D), y, z + rad * Math.sin((double) i * 6.283185307179586D / 45.0D));
        }

        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GLUtils.endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawEntityBox(Entity entity, Color color) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        Timer timer = RenderUtils.mc.timer;

        GlStateManager.pushMatrix();
        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        disableGlCap(new int[] { 3553, 2929});
        GL11.glDepthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) timer.renderPartialTicks - renderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) timer.renderPartialTicks - renderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) timer.renderPartialTicks - renderManager.renderPosZ;
        AxisAlignedBB entityBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.minX - entity.posX + x - 0.05D, entityBox.minY - entity.posY + y, entityBox.minZ - entity.posZ + z - 0.05D, entityBox.maxX - entity.posX + x + 0.05D, entityBox.maxY - entity.posY + y + 0.15D, entityBox.maxZ - entity.posZ + z + 0.05D);

        GL11.glTranslated(x, y, z);
        GL11.glRotated((double) (-entity.getRotationYawHead()), 0.0D, 1.0D, 0.0D);
        GL11.glTranslated(-x, -y, -z);
        GL11.glLineWidth(3.0F);
        enableGlCap(2848);
        glColor(0, 0, 0, 255);
        drawSelectionBoundingBox(axisAlignedBB);
        GL11.glLineWidth(1.0F);
        enableGlCap(2848);
        glColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
        drawSelectionBoundingBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        resetCaps();
        GlStateManager.popMatrix();
    }

    public static void drawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        float f4 = (float) (col2 >> 24 & 255) / 255.0F;
        float f5 = (float) (col2 >> 16 & 255) / 255.0F;
        float f6 = (float) (col2 >> 8 & 255) / 255.0F;
        float f7 = (float) (col2 & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static void kaMark(Entity entity, Color color) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        Timer timer = RenderUtils.mc.timer;
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) timer.renderPartialTicks - renderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) timer.renderPartialTicks - renderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) timer.renderPartialTicks - renderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

        drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.minX - 0.025D, axisAlignedBB.maxY - 0.35D, axisAlignedBB.minZ - 0.025D, axisAlignedBB.maxX + 0.025D, axisAlignedBB.maxY - 0.275D, axisAlignedBB.maxZ + 0.025D), color);
    }

    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float) time + (1.0F + count) * 2.0E8F) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16);
        Color c = new Color((int) color);

        return new Color((float) c.getRed() / 255.0F * fade, (float) c.getGreen() / 255.0F * fade, (float) c.getBlue() / 255.0F * fade, (float) c.getAlpha() / 255.0F);
    }

    public static void drawFace(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation exception = target.getLocationSkin();

            RenderUtils.mc.getTextureManager().bindTexture(exception);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public static void quickRenderCircle(double x, double y, double start, double end, double w, double h) {
        double i;

        if (start > end) {
            i = end;
            end = start;
            start = i;
        }

        GL11.glBegin(6);
        GL11.glVertex2d(x, y);

        for (i = end; i >= start; i -= 4.0D) {
            double ldx = Math.cos(i * 3.141592653589793D / 180.0D) * w;
            double ldy = Math.sin(i * 3.141592653589793D / 180.0D) * h;

            GL11.glVertex2d(x + ldx, y + ldy);
        }

        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static void rectTexture(float x, float y, float w, float h, ResourceLocation texture, int color) {
        if (texture != null) {
            GlStateManager.color(0.0F, 0.0F, 0.0F);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
            x = (float) Math.round(x);
            w = (float) Math.round(w);
            y = (float) Math.round(y);
            h = (float) Math.round(h);
            float f = (float) (color >> 24 & 255) / 255.0F;
            float f1 = (float) (color >> 16 & 255) / 255.0F;
            float f2 = (float) (color >> 8 & 255) / 255.0F;
            float f3 = (float) (color & 255) / 255.0F;

            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(f1, f2, f3, f);
            GL11.glEnable(3042);
            GL11.glEnable(3553);
            RenderUtils.mc.getTextureManager().bindTexture(texture);
            float tw = w / w / (w / w);
            float th = h / h / (h / h);

            GL11.glBegin(7);
            GL11.glTexCoord2f(0.0F, 0.0F);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(0.0F, th);
            GL11.glVertex2f(x, y + h);
            GL11.glTexCoord2f(tw, th);
            GL11.glVertex2f(x + w, y + h);
            GL11.glTexCoord2f(tw, 0.0F);
            GL11.glVertex2f(x + w, y);
            GL11.glEnd();
            GL11.glDisable(3553);
            GL11.glDisable(3042);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    public static void drawCircleRect(float x, float y, float x1, float y1, float radius, int color) {
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f1, f2, f3, f);
        quickRenderCircle((double) (x1 - radius), (double) (y1 - radius), 0.0D, 90.0D, (double) radius, (double) radius);
        quickRenderCircle((double) (x + radius), (double) (y1 - radius), 90.0D, 180.0D, (double) radius, (double) radius);
        quickRenderCircle((double) (x + radius), (double) (y + radius), 180.0D, 270.0D, (double) radius, (double) radius);
        quickRenderCircle((double) (x1 - radius), (double) (y + radius), 270.0D, 360.0D, (double) radius, (double) radius);
        quickDrawRect(x + radius, y + radius, x1 - radius, y1 - radius);
        quickDrawRect(x, y + radius, x + radius, y1 - radius);
        quickDrawRect(x1 - radius, y + radius, x1, y1 - radius);
        quickDrawRect(x + radius, y, x1 - radius, y + radius);
        quickDrawRect(x + radius, y1 - radius, x1 - radius, y1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutLineRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawOutLineRect(int x, int y, int x1, int y1, int width, int internalColor, int borderColor) {
        drawRect((float) (x + width), (float) (y + width), (float) (x1 - width), (float) (y1 - width), internalColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect((float) (x + width), (float) y, (float) (x1 - width), (float) (y + width), borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect((float) x, (float) y, (float) (x + width), (float) y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect((float) (x1 - width), (float) y, (float) x1, (float) y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawRect((float) (x + width), (float) (y1 - width), (float) (x1 - width), (float) y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawBorderRect(float x, float y, float x2, float y2, float round, int color) {
        drawRect(x += round / 2.0F + 0.5F, y += round / 2.0F + 0.5F, x2 -= round / 2.0F + 0.5F, y2 -= round / 2.0F + 0.5F, color);
        circle(x2 - round / 2.0F, y + round / 2.0F, round, color);
        circle(x + round / 2.0F, y2 - round / 2.0F - 0.2F, round, color);
        circle(x + round / 2.0F, y + round / 2.0F, round, color);
        circle(x2 - round / 2.0F, y2 - round / 2.0F - 0.2F, round, color);
        drawRect(x - round / 2.0F - 0.5F, y + round / 2.0F, x2, y2 - round / 2.0F, color);
        drawRect(x, y + round / 2.0F, x2 + round / 2.0F + 0.5F, y2 - round / 2.0F, color);
        drawRect(x + round / 2.0F, y - round / 2.0F - 0.5F, x2 - round / 2.0F, y2 - round / 2.0F, color);
        drawRect(x + round / 2.0F, y, x2 - round / 2.0F, y2 + round / 2.0F + 0.5F, color);
    }

    public static int SkyRainbow(int i, float st, float bright) {
        double v1 = Math.ceil((double) (System.currentTimeMillis() + (long) (i * 109))) / 5.0D;

        return Color.getHSBColor((double) ((float) ((v1 %= 360.0D) / 360.0D)) < 0.5D ? -((float) (v1 / 360.0D)) : (float) (v1 / 360.0D), st, bright).getRGB();
    }

    public static Color skyRainbow(int i, float st, float bright) {
        double v1 = Math.ceil((double) (System.currentTimeMillis() + (long) (i * 109))) / 5.0D;

        return Color.getHSBColor((double) ((float) ((v1 %= 360.0D) / 360.0D)) < 0.5D ? -((float) (v1 / 360.0D)) : (float) (v1 / 360.0D), st, bright);
    }

    public static Color skyRainbow(int i, float bright, float st, double speed) {
        double v1 = Math.ceil((double) System.currentTimeMillis() / speed + (double) ((long) i * 109L)) / 5.0D;

        return Color.getHSBColor((double) ((float) ((v1 %= 360.0D) / 360.0D)) < 0.5D ? -((float) (v1 / 360.0D)) : (float) (v1 / 360.0D), st, bright);
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0F);
        GL11.glBegin(9);

        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double) x + Math.sin((double) i * 3.141592653589793D / 180.0D) * (double) radius, (double) y + Math.cos((double) i * 3.141592653589793D / 180.0D) * (double) radius);
        }

        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void quickDrawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        float f4 = (float) (col2 >> 24 & 255) / 255.0F;
        float f5 = (float) (col2 >> 16 & 255) / 255.0F;
        float f6 = (float) (col2 >> 8 & 255) / 255.0F;
        float f7 = (float) (col2 & 255) / 255.0F;

        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + (float) uWidth) * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + (float) uWidth) * f), (double) (v * f1)).endVertex();
        worldrenderer.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        tessellator.draw();
    }

    public static void drawTriangle(float x, float y, float x2, float y2, float x3, float y3, Color color) {
        GL11.glEnable(2848);
        glColor(color);
        GL11.glBegin(4);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x3, y3);
        GL11.glEnd();
        GL11.glDisable(2848);
    }

    public static void drawTriAngle(float cx, float cy, float r, float n, int color) {
        GL11.glPushMatrix();
        cx = (float) ((double) cx * 2.0D);
        cy = (float) ((double) cy * 2.0D);
        double b = 6.283185307179586D / (double) n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        double x = (double) r * 2.0D;
        double y = 0.0D;

        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GlStateManager.resetColor();
        glColor114514(color);
        GL11.glBegin(2);

        for (int ii = 0; (float) ii < n; ++ii) {
            GL11.glVertex2d(x + (double) cx, y + (double) cy);
            double t = x;

            x = p * x - s * y;
            y = s * t + p * y;
        }

        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase entity) {
        GlStateManager.pushMatrix();
        GlStateManager.enableColorMaterial();
        GlStateManager.translate((double) posX, (double) posY, 50.0D);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0D, 0.0D, 0.0D);
        RenderManager rendermanager = RenderUtils.mc.getRenderManager();

        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawEntityOnScreen(int posX, int posY, float targetHeight, float mouseX, float mouseY, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 40.0F);
        GlStateManager.scale(-targetHeight, targetHeight, targetHeight);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;

        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * -14.0F;
        ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * -14.0F;
        ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 15.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();

        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static int RedRainbow() {
        ++RenderUtils8.counts;
        int RainbowColor = rainbow(System.nanoTime(), (float) RenderUtils8.counts, 0.5F).getRGB();
        Color col = new Color(RainbowColor);

        return (new Color((float) col.getRed() / 150.0F, 0.09803922F, 0.09803922F)).getRGB();
    }

    public static void drawBlockBox(BlockPos blockPos, Color color, boolean outline) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        Timer timer = RenderUtils.mc.timer;
        double x = (double) blockPos.getX() - renderManager.renderPosX;
        double y = (double) blockPos.getY() - renderManager.renderPosY;
        double z = (double) blockPos.getZ() - renderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
        Block block = BlockUtils.getBlock(blockPos);

        if (block != null) {
            EntityPlayerSP player = RenderUtils.mc.thePlayer;
            double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;

            axisAlignedBB = block.getSelectedBoundingBox(RenderUtils.mc.theWorld, blockPos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-posX, -posY, -posZ);
        }

        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        disableGlCap(new int[] { 3553, 2929});
        GL11.glDepthMask(false);
        glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (outline ? 26 : 35));
        drawFilledBox(axisAlignedBB);
        if (outline) {
            GL11.glLineWidth(1.0F);
            enableGlCap(2848);
            glColor(color);
            drawSelectionBoundingBox(axisAlignedBB);
        }

        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        resetCaps();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        GL11.glEnable(2848);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
    }

    public static int reAlpha(int n, float n2) {
        Color color = new Color(n);

        return (new Color(0.003921569F * (float) color.getRed(), 0.003921569F * (float) color.getGreen(), 0.003921569F * (float) color.getBlue(), n2)).getRGB();
    }

    public static void drawEntityBox(Entity entity, Color color, boolean outline) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        Timer timer = RenderUtils.mc.timer;

        GL11.glBlendFunc(770, 771);
        enableGlCap(3042);
        disableGlCap(new int[] { 3553, 2929});
        GL11.glDepthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) timer.renderPartialTicks - renderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) timer.renderPartialTicks - renderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) timer.renderPartialTicks - renderManager.renderPosZ;
        AxisAlignedBB entityBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.minX - entity.posX + x - 0.05D, entityBox.minY - entity.posY + y, entityBox.minZ - entity.posZ + z - 0.05D, entityBox.maxX - entity.posX + x + 0.05D, entityBox.maxY - entity.posY + y + 0.15D, entityBox.maxZ - entity.posZ + z + 0.05D);

        if (outline) {
            GL11.glLineWidth(1.0F);
            enableGlCap(2848);
            glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            drawSelectionBoundingBox(axisAlignedBB);
        }

        glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        resetCaps();
    }

    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        glColor(color);
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    public static void drawPlatform(double y, Color color, double size) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        double renderY = y - renderManager.renderPosY;

        drawAxisAlignedBB(new AxisAlignedBB(size, renderY + 0.02D, size, -size, renderY, -size), color);
    }

    public static void drawPlatform(Entity entity, Color color) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        Timer timer = RenderUtils.mc.timer;
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) timer.renderPartialTicks - renderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) timer.renderPartialTicks - renderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) timer.renderPartialTicks - renderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

        drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.maxY + 0.2D, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + 0.26D, axisAlignedBB.maxZ), color);
    }

    public static void drawFilledBox(AxisAlignedBB axisAlignedBB) {
        GL11.glEnable(2848);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
    }

    public static void quickDrawRect(float x, float y, float x2, float y2) {
        GL11.glBegin(7);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void quickDrawRect(float x, float y, float x2, float y2, int color) {
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float x2, float y2, Color color) {
        drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        drawRect(x, y, x2, y2, color2);
        drawBorder(x, y, x2, y2, width, color1);
    }

    public static void drawBorder(float x, float y, float x2, float y2, float width, int color1) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        glColor(color1);
        GL11.glLineWidth(width);
        GL11.glBegin(2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void quickDrawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        quickDrawRect(x, y, x2, y2, color2);
        glColor(color1);
        GL11.glLineWidth(width);
        GL11.glBegin(2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
    }

    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; ++i) {
            int rot = (int) (System.nanoTime() / 5000000L * (long) i % 360L);

            drawCircle(x, y, (float) (i * 10), rot - 180, rot);
        }

    }

    public static void drawCircle2(double x, double y, double radius, int c) {
        float f2 = (float) (c >> 24 & 255) / 255.0F;
        float f22 = (float) (c >> 16 & 255) / 255.0F;
        float f3 = (float) (c >> 8 & 255) / 255.0F;
        float f4 = (float) (c & 255) / 255.0F;

        GlStateManager.alphaFunc(516, 0.001F);
        GlStateManager.color(f22, f3, f4, f2);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tes = Tessellator.getInstance();

        for (double i = 0.0D; i < 360.0D; ++i) {
            double f5 = Math.sin(i * 3.141592653589793D / 180.0D) * radius;
            double f6 = Math.cos(i * 3.141592653589793D / 180.0D) * radius;

            GL11.glVertex2d((double) f3 + x, (double) f4 + y);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        GL11.glEnable(2881);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(Color.WHITE);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(3);

        for (float i = (float) end; i >= (float) start; i -= 4.0F) {
            GL11.glVertex2f((float) ((double) x + Math.cos((double) i * 3.141592653589793D / 180.0D) * (double) (radius * 1.001F)), (float) ((double) y + Math.sin((double) i * 3.141592653589793D / 180.0D) * (double) (radius * 1.001F)));
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glDisable(2881);
    }

    public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
        byte sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushAttrib(8192);
        GL11.glEnable(2881);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));

            GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
            GL11.glVertex2f((float) xx + x, (float) yy + y);
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glDisable(2881);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtils.mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation image, Color color) {
        GL11.glDisable(2839);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float) (color.getRed() / 255), (float) (color.getGreen() / 255), (float) (color.getBlue() / 255), (float) (color.getAlpha() / 255));
        RenderUtils.mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2839);
    }

    public static void drawtargethudRect(double d, double e, double g, double h, int color, int i) {
        drawRect(d + 1.0D, e, g - 1.0D, h, color);
        drawRect(d, e + 1.0D, d + 1.0D, h - 1.0D, color);
        drawRect(d + 1.0D, e + 1.0D, d + 0.5D, e + 0.5D, color);
        drawRect(d + 1.0D, e + 1.0D, d + 0.5D, e + 0.5D, color);
        drawRect(g - 1.0D, e + 1.0D, g - 0.5D, e + 0.5D, color);
        drawRect(g - 1.0D, e + 1.0D, g, h - 1.0D, color);
        drawRect(d + 1.0D, h - 1.0D, d + 0.5D, h - 0.5D, color);
        drawRect(g - 1.0D, h - 1.0D, g - 0.5D, h - 0.5D, color);
    }

    public static void drawFace(int x, int y, float scale, AbstractClientPlayer target) {
        try {
            ResourceLocation e = target.getLocationSkin();

            Minecraft.getMinecraft().getTextureManager().bindTexture(e);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, (int) scale, (int) scale, 64.0F, 64.0F);
            GL11.glDisable(3042);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public static void glColor(int red, int green, int blue, int alpha) {
        GlStateManager.color((float) red / 255.0F, (float) green / 255.0F, (float) blue / 255.0F, (float) alpha / 255.0F);
    }

    public static void glColor(Color color) {
        float red = (float) color.getRed() / 255.0F;
        float green = (float) color.getGreen() / 255.0F;
        float blue = (float) color.getBlue() / 255.0F;
        float alpha = (float) color.getAlpha() / 255.0F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0F;
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor114514(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0F;
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void draw2D(EntityLivingBase entity, double posX, double posY, double posZ, int color, int backgroundColor) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.rotate(-RenderUtils.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-0.1D, -0.1D, 0.1D);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GlStateManager.depthMask(true);
        glColor(color);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[0]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[1]);
        GlStateManager.translate(0.0D, 21.0D + -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0D, 0.0D);
        glColor(color);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[2]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[3]);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
    }

    public static void draw2D(BlockPos blockPos, int color, int backgroundColor) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();
        double posX = (double) blockPos.getX() + 0.5D - renderManager.renderPosX;
        double posY = (double) blockPos.getY() - renderManager.renderPosY;
        double posZ = (double) blockPos.getZ() + 0.5D - renderManager.renderPosZ;

        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GlStateManager.rotate(-RenderUtils.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-0.1D, -0.1D, 0.1D);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GlStateManager.depthMask(true);
        glColor(color);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[0]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[1]);
        GlStateManager.translate(0.0F, 9.0F, 0.0F);
        glColor(color);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[2]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils8.DISPLAY_LISTS_2D[3]);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
    }

    public static int Astolfo(int i, float st, float bright) {
        double currentColor = Math.ceil((double) (System.currentTimeMillis() + (long) (i * 130))) / 6.0D;

        return Color.getHSBColor((double) ((float) ((currentColor %= 360.0D) / 360.0D)) < 0.5D ? -((float) (currentColor / 360.0D)) : (float) (currentColor / 360.0D), st, bright).getRGB();
    }

    public static void renderNameTag(String string, double x, double y, double z) {
        RenderManager renderManager = RenderUtils.mc.getRenderManager();

        GL11.glPushMatrix();
        GL11.glTranslated(x - renderManager.renderPosX, y - renderManager.renderPosY, z - renderManager.renderPosZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderUtils.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderUtils.mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-0.05F, -0.05F, 0.05F);
        setGlCap(2896, false);
        setGlCap(2929, false);
        setGlCap(3042, true);
        GL11.glBlendFunc(770, 771);
        int width = Fonts.font35.getStringWidth(string) / 2;

        Gui.drawRect(-width - 1, -1, width + 1, Fonts.font35.FONT_HEIGHT, Integer.MIN_VALUE);
        Fonts.font35.drawString(string, (float) (-width), 1.5F, Color.WHITE.getRGB(), true);
        resetCaps();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public static void glColor2(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0F;
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void makeScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scaledResolution = new ScaledResolution(RenderUtils.mc);
        int factor = scaledResolution.getScaleFactor();

        GL11.glScissor((int) (x * (float) factor), (int) (((float) scaledResolution.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void resetCaps() {
        RenderUtils8.glCapMap.forEach(RenderUtils8::setGlState);
    }

    private static void setGlState(Object o, Object o1) {

    }

    public static void enableGlCap(int cap) {
        setGlCap(cap, true);
    }

    public static void enableGlCap(int... caps) {
        int[] aint = caps;
        int i = caps.length;

        for (int j = 0; j < i; ++j) {
            int cap = aint[j];

            setGlCap(cap, true);
        }

    }

    public static void disableGlCap(int cap) {
        setGlCap(cap, true);
    }

    public static void disableGlCap(int... caps) {
        int[] aint = caps;
        int i = caps.length;

        for (int j = 0; j < i; ++j) {
            int cap = aint[j];

            setGlCap(cap, false);
        }

    }

    public static void setGlCap(int cap, boolean state) {
        RenderUtils8.glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
        setGlState(cap, state);
    }

    public static void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }

    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01D * speed);

        if (animation < finalState) {
            if (animation + (double) add < finalState) {
                animation += (double) add;
            } else {
                animation = finalState;
            }
        } else if (animation - (double) add > finalState) {
            animation -= (double) add;
        } else {
            animation = finalState;
        }

        return animation;
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        float f4 = (float) (col2 >> 24 & 255) / 255.0F;
        float f5 = (float) (col2 >> 16 & 255) / 255.0F;
        float f6 = (float) (col2 >> 8 & 255) / 255.0F;
        float f7 = (float) (col2 & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void circle(float x, float y, float radius, int fill) {
        arc(x, y, 0.0F, 360.0F, radius, fill);
    }

    public static void circle(float x, float y, float radius, Color fill) {
        arc(x, y, 0.0F, 360.0F, radius, fill);
    }

    public static void circle2(float x, float y, float radius, int color) {
        arc(x, y, 180.0F, 360.0F, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, Color color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void drawFace(ResourceLocation Texture, int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, int tileWidth, int tileHeight) {
        RenderUtils.mc.getTextureManager().bindTexture(Texture);
        GL11.glEnable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, (float) tileWidth, (float) tileHeight);
        GL11.glDisable(3042);
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawHLine(float par1, float par2, float par3, int par4) {
        if (par2 < par1) {
            float f = par1;

            par1 = par2;
            par2 = f;
        }

        drawRect(par1, par3, par2 + 1.0F, par3 + 1.0F, par4);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float f = y;

            y = x1;
            x1 = f;
        }

        drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void setColor(int colorHex) {
        float alpha = (float) (colorHex >> 24 & 255) / 255.0F;
        float red = (float) (colorHex >> 16 & 255) / 255.0F;
        float green = (float) (colorHex >> 8 & 255) / 255.0F;
        float blue = (float) (colorHex & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
    }

    public static void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtils.mc.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
    }

    public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtils.mc.getTextureManager().bindTexture(skin);
        drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
        drawScaledCustomSizeModalRect(x, y, 40.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
    }

    public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
        GL11.glPushMatrix();
        GL11.glTranslated(posX, posY, posZ);
        GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        setColor(rgb);
        enableGL3D(1.0F);
        Cylinder c = new Cylinder();

        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        c.setDrawStyle(100011);
        c.draw(0.5F, 0.5F, entity.height + 0.1F, 18, 1);
        disableGL3D();
        GL11.glPopMatrix();
    }
    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = width;
        double y1 = height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + +(Math.sin((i * Math.PI / 180)) * (radius * -1)), y + radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + (Math.sin((i * Math.PI / 180)) * (radius * -1)), y1 - radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y1 - radius + (Math.cos((i * Math.PI / 180)) * radius));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y + radius + (Math.cos((i * Math.PI / 180)) * radius));
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glScaled(2, 2, 2);
        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x *= 2.0F, (y *= 2.0F) + 1.0F, (y1 *= 2.0F) - 2.0F, borderC);
        drawVLine((x1 *= 2.0F) - 1.0F, y + 1.0F, y1 - 2.0F, borderC);
        drawHLine(x + 2.0F, x1 - 3.0F, y, borderC);
        drawHLine(x + 2.0F, x1 - 3.0F, y1 - 1.0F, borderC);
        drawHLine(x + 1.0F, x + 1.0F, y + 1.0F, borderC);
        drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
        drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
        drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
        drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static void drawCircle(double x, double y, double radius, int c) {
        float alpha = (float) (c >> 24 & 255) / 255.0F;
        float red = (float) (c >> 16 & 255) / 255.0F;
        float green = (float) (c >> 8 & 255) / 255.0F;
        float blue = (float) (c & 255) / 255.0F;
        boolean blend = GL11.glIsEnabled(3042);
        boolean line = GL11.glIsEnabled(2848);
        boolean texture = GL11.glIsEnabled(3553);

        if (!blend) {
            GL11.glEnable(3042);
        }

        if (!line) {
            GL11.glEnable(2848);
        }

        if (texture) {
            GL11.glDisable(3553);
        }

        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);

        for (int i = 0; i <= 180; ++i) {
            GL11.glVertex2d(x + Math.sin((double) i * 3.141526D / 180.0D) * radius, y + Math.cos((double) i * 3.141526D / 180.0D) * radius);
        }

        GL11.glEnd();
        if (texture) {
            GL11.glEnable(3553);
        }

        if (!line) {
            GL11.glDisable(2848);
        }

        if (!blend) {
            GL11.glDisable(3042);
        }

    }

    public static void drawSuperCircle(float x, float y, float radius, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();

        tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        int i;

        if (alpha > 0.5F) {
            GL11.glEnable(2881);
            GL11.glEnable(2848);
            GL11.glBlendFunc(770, 771);
            GL11.glBegin(3);

            for (i = 0; i <= 180; ++i) {
                GL11.glVertex2d((double) x + Math.sin((double) i * 3.141526D / 180.0D) * (double) radius, (double) y + Math.cos((double) i * 3.141526D / 180.0D) * (double) radius);
                GL11.glVertex2d((double) x + Math.sin((double) i * 3.141526D / 180.0D) * (double) radius, (double) y + Math.cos((double) i * 3.141526D / 180.0D) * (double) radius);
            }

            GL11.glEnd();
            GL11.glDisable(2848);
            GL11.glDisable(2881);
        }

        GL11.glBegin(6);

        for (i = 0; i <= 180; ++i) {
            GL11.glVertex2d((double) x + Math.sin((double) i * 3.141526D / 180.0D) * (double) radius, (double) y + Math.cos((double) i * 3.141526D / 180.0D) * (double) radius);
        }

        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        if (start > end) {
            float temp = end;

            end = start;
            start = temp;
        }

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();

        tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        float i;
        float ldx;
        float ldy;

        if (alpha > 0.5F) {
            GL11.glEnable(2881);
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0F);
            GL11.glBegin(3);

            for (i = end; i >= start; i -= 4.0F) {
                ldx = (float) Math.cos((double) i * 3.141592653589793D / 180.0D) * w * 1.001F;
                ldy = (float) Math.sin((double) i * 3.141592653589793D / 180.0D) * h * 1.001F;
                GL11.glVertex2f(x + ldx, y + ldy);
            }

            GL11.glEnd();
            GL11.glDisable(2848);
            GL11.glDisable(2881);
        }

        GL11.glBegin(6);

        for (i = end; i >= start; i -= 4.0F) {
            ldx = (float) Math.cos((double) i * 3.141592653589793D / 180.0D) * w;
            ldy = (float) Math.sin((double) i * 3.141592653589793D / 180.0D) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }

        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawIcon(float x, float y, int width, int height, ResourceLocation resourceLocation) {
        GL11.glPushMatrix();
        RenderUtils.mc.getTextureManager().bindTexture(resourceLocation);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glTranslatef(x, y, 0.0F);
        drawScaledRect(0, 0, 0.0F, 0.0F, width, height, width, height, (float) width, (float) height);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        float temp = 0.0F;

        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }

        Tessellator tessellator = Tessellator.getInstance();

        tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
        float i;
        float ldx;
        float ldy;

        if ((float) color.getAlpha() > 0.5F) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0F);
            GL11.glBegin(3);

            for (i = end; i >= start; i -= 4.0F) {
                ldx = (float) Math.cos((double) i * 3.141592653589793D / 180.0D) * w * 1.001F;
                ldy = (float) Math.sin((double) i * 3.141592653589793D / 180.0D) * h * 1.001F;
                GL11.glVertex2f(x + ldx, y + ldy);
            }

            GL11.glEnd();
            GL11.glDisable(2848);
        }

        GL11.glBegin(6);

        for (i = end; i >= start; i -= 4.0F) {
            ldx = (float) Math.cos((double) i * 3.141592653589793D / 180.0D) * w;
            ldy = (float) Math.sin((double) i * 3.141592653589793D / 180.0D) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }

        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawFilledCircle(double x, double y, double r, int c, int id) {
        float f = (float) (c >> 24 & 255) / 255.0F;
        float f1 = (float) (c >> 16 & 255) / 255.0F;
        float f2 = (float) (c >> 8 & 255) / 255.0F;
        float f3 = (float) (c & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(9);
        int i;
        double x2;
        double y2;

        if (id == 1) {
            GL11.glVertex2d(x, y);

            for (i = 0; i <= 90; ++i) {
                x2 = Math.sin((double) i * 3.141526D / 180.0D) * r;
                y2 = Math.cos((double) i * 3.141526D / 180.0D) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else if (id == 2) {
            GL11.glVertex2d(x, y);

            for (i = 90; i <= 180; ++i) {
                x2 = Math.sin((double) i * 3.141526D / 180.0D) * r;
                y2 = Math.cos((double) i * 3.141526D / 180.0D) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else if (id == 3) {
            GL11.glVertex2d(x, y);

            for (i = 270; i <= 360; ++i) {
                x2 = Math.sin((double) i * 3.141526D / 180.0D) * r;
                y2 = Math.cos((double) i * 3.141526D / 180.0D) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else if (id == 4) {
            GL11.glVertex2d(x, y);

            for (i = 180; i <= 270; ++i) {
                x2 = Math.sin((double) i * 3.141526D / 180.0D) * r;
                y2 = Math.cos((double) i * 3.141526D / 180.0D) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        } else {
            for (i = 0; i <= 360; ++i) {
                x2 = Math.sin((double) i * 3.141526D / 180.0D) * r;
                y2 = Math.cos((double) i * 3.141526D / 180.0D) * r;
                GL11.glVertex2f((float) (x - x2), (float) (y - y2));
            }
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }

    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawRoundRect(float d, float e, float g, float h, int color) {
        drawRect(d + 1.0F, e, g - 1.0F, h, color);
        drawRect(d, e + 1.0F, d + 1.0F, h - 1.0F, color);
        drawRect(d + 1.0F, e + 1.0F, d + 0.5F, e + 0.5F, color);
        drawRect(d + 1.0F, e + 1.0F, d + 0.5F, e + 0.5F, color);
        drawRect(g - 1.0F, e + 1.0F, g - 0.5F, e + 0.5F, color);
        drawRect(g - 1.0F, e + 1.0F, g, h - 1.0F, color);
        drawRect(d + 1.0F, h - 1.0F, d + 0.5F, h - 0.5F, color);
        drawRect(g - 1.0F, h - 1.0F, g - 0.5F, h - 0.5F, color);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawEntityKillAuraESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
        boolean Semicircle = true;
        float f = 5.0F;
        float f2 = (float) (color >> 24 & 255) / 255.0F;
        float f3 = (float) (color >> 16 & 255) / 255.0F;
        float f4 = (float) (color >> 8 & 255) / 255.0F;
        float f5 = (float) (color & 255) / 255.0F;

        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(5);
        GL11.glVertex2f(x0 + radius, y0);
        GL11.glVertex2f(x0 + radius, y1);
        GL11.glVertex2f(x1 - radius, y0);
        GL11.glVertex2f(x1 - radius, y1);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x0, y0 + radius);
        GL11.glVertex2f(x0 + radius, y0 + radius);
        GL11.glVertex2f(x0, y1 - radius);
        GL11.glVertex2f(x0 + radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x1, y0 + radius);
        GL11.glVertex2f(x1 - radius, y0 + radius);
        GL11.glVertex2f(x1, y1 - radius);
        GL11.glVertex2f(x1 - radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(6);
        float f6 = x1 - radius;
        float f7 = y0 + radius;

        GL11.glVertex2f(f6, f7);
        boolean j = false;

        float f11;
        int i;

        for (i = 0; i <= 18; ++i) {
            f11 = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) f6 + (double) radius * Math.cos(Math.toRadians((double) f11))), (float) ((double) f7 - (double) radius * Math.sin(Math.toRadians((double) f11))));
        }

        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);

        for (i = 0; i <= 18; ++i) {
            f11 = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) f6 - (double) radius * Math.cos(Math.toRadians((double) f11))), (float) ((double) f7 - (double) radius * Math.sin(Math.toRadians((double) f11))));
        }

        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);

        for (i = 0; i <= 18; ++i) {
            f11 = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) f6 - (double) radius * Math.cos(Math.toRadians((double) f11))), (float) ((double) f7 + (double) radius * Math.sin(Math.toRadians((double) f11))));
        }

        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);

        for (i = 0; i <= 18; ++i) {
            f11 = (float) i * 5.0F;
            GL11.glVertex2f((float) ((double) f6 + (double) radius * Math.cos(Math.toRadians((double) f11))), (float) ((double) f7 + (double) radius * Math.sin(Math.toRadians((double) f11))));
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
    }

    public static void entityESPBox(Entity e, Color color, Render3DEvent event) {
        double renderPosX = RenderUtils.mc.getRenderManager().renderPosX;
        double renderPosY = RenderUtils.mc.getRenderManager().renderPosY;
        double renderPosZ = RenderUtils.mc.getRenderManager().renderPosZ;
        double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) event.getPartialTicks() - renderPosX;
        double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) event.getPartialTicks() - renderPosY;
        double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) event.getPartialTicks() - renderPosZ;
        AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double) e.width, posY, posZ - (double) e.width, posX + (double) e.width, posY + (double) e.height + 0.2D, posZ + (double) e.width);

        if (e instanceof EntityLivingBase) {
            box = AxisAlignedBB.fromBounds(posX - (double) e.width + 0.2D, posY, posZ - (double) e.width + 0.2D, posX + (double) e.width - 0.2D, posY + (double) e.height + (e.isSneaking() ? 0.02D : 0.2D), posZ + (double) e.width - 0.2D);
        }

        EntityLivingBase e2 = (EntityLivingBase) e;

        if (e2.hurtTime != 0) {
            GL11.glColor4f(1.0F, 0.2F, 0.1F, 0.2F);
        } else {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
        }

        drawBoundingBox(box);
    }

    public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float) (a4 >> 24 & 255) / 255.0F;
        float a8 = (float) (a4 >> 16 & 255) / 255.0F;
        float a9 = (float) (a4 >> 8 & 255) / 255.0F;
        float a10 = (float) (a4 & 255) / 255.0F;
        float a11 = (float) (a5 >> 24 & 255) / 255.0F;
        float a12 = (float) (a5 >> 16 & 255) / 255.0F;
        float a13 = (float) (a5 >> 8 & 255) / 255.0F;
        float a14 = (float) (a5 & 255) / 255.0F;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(a8, a9, a10, a7);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0D, a2 + 1.0D, a3 + 1.0D));
        GL11.glLineWidth(a6);
        GL11.glColor4f(a12, a13, a14, a11);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0D, a2 + 1.0D, a3 + 1.0D));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void rectangle(double x, double y, double x2, double y2, int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawRectBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        rectangle(x, y, x + width, y1, borderColor);
        rectangle(x1 - width, y, x1, y1, borderColor);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);

        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);

        return (double[]) (result ? new double[] { (double) screenCoords.get(0), (double) ((float) Display.getHeight() - screenCoords.get(1)), (double) screenCoords.get(2)} : null);
    }

    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * (double) Minecraft.getMinecraft().timer.renderPartialTicks;
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();

        RenderUtils8.frustrum.setPosition(current.posX, current.posY, current.posZ);
        return RenderUtils8.frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void drawblock2(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float) (a4 >> 24 & 255) / 255.0F;
        float a8 = (float) (a4 >> 16 & 255) / 255.0F;
        float a9 = (float) (a4 >> 8 & 255) / 255.0F;
        float a10 = (float) (a4 & 255) / 255.0F;
        float a11 = (float) (a5 >> 24 & 255) / 255.0F;
        float a12 = (float) (a5 >> 16 & 255) / 255.0F;
        float a13 = (float) (a5 >> 8 & 255) / 255.0F;
        float a14 = (float) (a5 & 255) / 255.0F;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(a8, a9, a10, a7);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0D, a2 + 1.0D, a3 + 1.0D));
        GL11.glLineWidth(a6);
        GL11.glColor4f(a12, a13, a14, a11);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0D, a2 + 1.0D, a3 + 1.0D));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawVLine(float x, float y, float x1, float y1, float width, int color) {
        if (width > 0.0F) {
            GL11.glPushMatrix();
            pre3D();
            float f = (float) (color >> 24 & 255) / 255.0F;
            float f1 = (float) (color >> 16 & 255) / 255.0F;
            float f2 = (float) (color >> 8 & 255) / 255.0F;
            float f3 = (float) (color & 255) / 255.0F;

            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            int shade = GL11.glGetInteger(2900);

            GlStateManager.shadeModel(7425);
            GL11.glColor4f(f1, f2, f3, f);
            float line = GL11.glGetFloat(2849);

            GL11.glLineWidth(width);
            GL11.glBegin(3);
            GL11.glVertex3d((double) x, (double) y, 0.0D);
            GL11.glVertex3d((double) x1, (double) y1, 0.0D);
            GL11.glEnd();
            GlStateManager.shadeModel(shade);
            GL11.glLineWidth(line);
            post3D();
            GL11.glPopMatrix();
        }
    }

    public static void drawRoundedRect2(float x, float y, float x2, float y2, float round, int color) {
        x += (float) ((double) (round / 2.0F) + 0.5D);
        y += (float) ((double) (round / 2.0F) + 0.5D);
        x2 -= (float) ((double) (round / 2.0F) + 0.5D);
        y2 -= (float) ((double) (round / 2.0F) + 0.5D);
        drawRect((float) ((int) x), (float) ((int) y), (float) ((int) x2), (float) ((int) y2), color);
        circle(x2 - round / 2.0F, y + round / 2.0F, round, color);
        circle(x + round / 2.0F, y2 - round / 2.0F, round, color);
        circle(x + round / 2.0F, y + round / 2.0F, round, color);
        circle(x2 - round / 2.0F, y2 - round / 2.0F, round, color);
        drawRect((float) ((int) (x - round / 2.0F - 0.5F)), (float) ((int) (y + round / 2.0F)), (float) ((int) x2), (float) ((int) (y2 - round / 2.0F)), color);
        drawRect((float) ((int) x), (float) ((int) (y + round / 2.0F)), (float) ((int) (x2 + round / 2.0F + 0.5F)), (float) ((int) (y2 - round / 2.0F)), color);
        drawRect((float) ((int) (x + round / 2.0F)), (float) ((int) (y - round / 2.0F - 0.5F)), (float) ((int) (x2 - round / 2.0F)), (float) ((int) (y2 - round / 2.0F)), color);
        drawRect((float) ((int) (x + round / 2.0F)), (float) ((int) y), (float) ((int) (x2 - round / 2.0F)), (float) ((int) (y2 + round / 2.0F + 0.5F)), color);
    }

    public static void setupRender(boolean start) {
        if (start) {
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(!start);
    }

    public static void doGlScissor(int x, int y, int width, int height2) {
        int scaleFactor = 1;
        int k = RenderUtils.mc.gameSettings.guiScale;

        if (k == 0) {
            k = 1000;
        }

        while (scaleFactor < k && RenderUtils.mc.displayWidth / (scaleFactor + 1) >= 320 && RenderUtils.mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }

        GL11.glScissor(x * scaleFactor, RenderUtils.mc.displayHeight - (y + height2) * scaleFactor, width * scaleFactor, height2 * scaleFactor);
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(RenderUtils.mc);int factor = scale.getScaleFactor();

        GL11.glScissor((int) (x * (float) factor), (int) (((float) scale.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + width) * f), (double) ((v + height) * f1)).endVertex();
        worldrenderer.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + width) * f), (double) (v * f1)).endVertex();
        worldrenderer.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        tessellator.draw();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
        enableGL2D();
        glColor(borderColor);
        drawRect(x + width, y, x1 - width, y + width);
        drawRect(x, y, x + width, y1);
        drawRect(x1 - width, y, x1, y1);
        drawRect(x + width, y1 - width, x1 - width, y1);
        disableGL2D();
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }

    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawLimitedCircle(float lx, float ly, float x2, float y2, int xx, int yy, float radius, Color color) {
        byte sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2881);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));

            GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
            GL11.glVertex2f(Math.min(x2, Math.max((float) xx + x, lx)), Math.min(y2, Math.max((float) yy + y, ly)));
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static boolean isHovering(int n, int n2, float n3, float n4, float n5, float n6) {
        return (float) n > n3 && (float) n < n5 && (float) n2 > n4 && (float) n2 < n6;
    }

    public static void drawEntityBox(@NotNull EntityLivingBase it, @Nullable Color color, boolean b, boolean b1, float fl) {}

    public static void glColor(int hex, float alpha) {
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static void glColor(Color color, float alpha) {
        float red = (float) color.getRed() / 255.0F;
        float green = (float) color.getGreen() / 255.0F;
        float blue = (float) color.getBlue() / 255.0F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static int Astolfo(int i) {
        double v1 = Math.ceil((double) (System.currentTimeMillis() + (long) (i * 109))) / 5.0D;

        return Color.getHSBColor((double) ((float) ((v1 %= 360.0D) / 360.0D)) < 0.5D ? -((float) (v1 / 360.0D)) : (float) (v1 / 360.0D), 0.5F, 1.0F).getRGB();
    }

    static {
        for (int i = 0; i < RenderUtils8.DISPLAY_LISTS_2D.length; ++i) {
            RenderUtils8.DISPLAY_LISTS_2D[i] = GL11.glGenLists(1);
        }

        GL11.glNewList(RenderUtils8.DISPLAY_LISTS_2D[0], 4864);
        quickDrawRect(-7.0F, 2.0F, -4.0F, 3.0F);
        quickDrawRect(4.0F, 2.0F, 7.0F, 3.0F);
        quickDrawRect(-7.0F, 0.5F, -6.0F, 3.0F);
        quickDrawRect(6.0F, 0.5F, 7.0F, 3.0F);
        GL11.glEndList();
        GL11.glNewList(RenderUtils8.DISPLAY_LISTS_2D[1], 4864);
        quickDrawRect(-7.0F, 3.0F, -4.0F, 3.3F);
        quickDrawRect(4.0F, 3.0F, 7.0F, 3.3F);
        quickDrawRect(-7.3F, 0.5F, -7.0F, 3.3F);
        quickDrawRect(7.0F, 0.5F, 7.3F, 3.3F);
        GL11.glEndList();
        GL11.glNewList(RenderUtils8.DISPLAY_LISTS_2D[2], 4864);
        quickDrawRect(4.0F, -20.0F, 7.0F, -19.0F);
        quickDrawRect(-7.0F, -20.0F, -4.0F, -19.0F);
        quickDrawRect(6.0F, -20.0F, 7.0F, -17.5F);
        quickDrawRect(-7.0F, -20.0F, -6.0F, -17.5F);
        GL11.glEndList();
        GL11.glNewList(RenderUtils8.DISPLAY_LISTS_2D[3], 4864);
        quickDrawRect(7.0F, -20.0F, 7.3F, -17.5F);
        quickDrawRect(-7.3F, -20.0F, -7.0F, -17.5F);
        quickDrawRect(4.0F, -20.3F, 7.3F, -20.0F);
        quickDrawRect(-7.3F, -20.3F, -4.0F, -20.0F);
        GL11.glEndList();
        RenderUtils8.frustrum = new Frustum();
    }
}
