package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class RenderUtils6 extends MinecraftInstance {

    private static final Map glCapMap = new HashMap();
    public static int deltaTime;
    public static float yPosOffset = 0.0F;
    private static final int[] DISPLAY_LISTS_2D = new int[4];
    private static final Frustum frustrum;
    protected static float zLevel;

    public static float[] getRotations2(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double) (ent.getEyeHeight() / 2.0F);

        return RenderUtils6.getRotationFromPosition2(x, z, y);
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static int astolfoRainbow(int delay, int offset, int index) {
        double rainbowDelay = Math.ceil((double) (System.currentTimeMillis() + (long) delay * (long) index)) / (double) offset;

        return Color.getHSBColor((double) ((float) ((rainbowDelay %= 360.0D) / 360.0D)) < 0.5D ? -((float) (rainbowDelay / 360.0D)) : (float) (rainbowDelay / 360.0D), 0.5F, 1.0F).getRGB();
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

    public static float[] getRotationFromPosition2(double x, double z, double y) {
        double xDiff = x - RenderUtils.mc.thePlayer.posX;
        double zDiff = z - RenderUtils.mc.thePlayer.posZ;
        double yDiff = y - RenderUtils.mc.thePlayer.posY - 1.2D;
        double dist = (double) MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);

        return new float[] { yaw, pitch};
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

    public static void circle(float x, float y, float radius, int fill) {
        arc(x, y, 0.0F, 360.0F, radius, fill);
    }

    public static void circle(float x, float y, float radius, Color fill) {
        arc(x, y, 0.0F, 360.0F, radius, fill);
    }

    public static void arc(float x, float y, float start, float end, float radius, Color color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
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

    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = RenderUtils.mc.getRenderViewEntity();

        RenderUtils6.frustrum.setPosition(current.posX, current.posY, current.posZ);
        return RenderUtils6.frustrum.isBoundingBoxInFrustum(bb);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static int SkyRainbow(int i, float st, float bright) {
        double v1 = Math.ceil((double) (System.currentTimeMillis() + (long) (i * 109))) / 5.0D;

        return Color.getHSBColor((double) ((float) ((v1 %= 360.0D) / 360.0D)) < 0.5D ? -((float) (v1 / 360.0D)) : (float) (v1 / 360.0D), st, bright).getRGB();
    }

    public static Color skyRainbow(int i, float st, float bright) {
        double v1 = Math.ceil((double) (System.currentTimeMillis() + (long) (i * 109))) / 5.0D;

        return Color.getHSBColor((double) ((float) ((v1 %= 360.0D) / 360.0D)) < 0.5D ? -((float) (v1 / 360.0D)) : (float) (v1 / 360.0D), st, bright);
    }

    public static int getRainbowOpaque(int seconds, float saturation, float brightness, int index) {
        float hue = (float) ((System.currentTimeMillis() + (long) index) % (long) (seconds * 1000)) / (float) (seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, brightness);

        return color;
    }

    public static Color getGradientOffset(Color color1, Color color2, double gident) {
        double f3;
        int f4;

        if (gident > 1.0D) {
            f3 = gident % 1.0D;
            f4 = (int) gident;
            gident = f4 % 2 == 0 ? f3 : 1.0D - f3;
        }

        f3 = 1.0D - gident;
        f4 = (int) ((double) color1.getRed() * f3 + (double) color2.getRed() * gident);
        int f5 = (int) ((double) color1.getGreen() * f3 + (double) color2.getGreen() * gident);
        int f6 = (int) ((double) color1.getBlue() * f3 + (double) color2.getBlue() * gident);

        return new Color(f4, f5, f6);
    }

    public static int getNormalRainbow(int delay, float sat, float brg) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0D);

        rainbowState %= 360.0D;
        return Color.getHSBColor((float) (rainbowState / 360.0D), sat, brg).getRGB();
    }

    public static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
    }

    public static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
    }

    public static void drawExhiRect(float x, float y, float x2, float y2) {
        drawRect(x - 3.5F, y - 3.5F, x2 + 3.5F, y2 + 3.5F, Color.black.getRGB());
        drawRect(x - 3.0F, y - 3.0F, x2 + 3.0F, y2 + 3.0F, (new Color(50, 50, 50)).getRGB());
        drawRect(x - 2.5F, y - 2.5F, x2 + 2.5F, y2 + 2.5F, (new Color(26, 26, 26)).getRGB());
        drawRect(x - 0.5F, y - 0.5F, x2 + 0.5F, y2 + 0.5F, (new Color(50, 50, 50)).getRGB());
        drawRect(x, y, x2, y2, (new Color(18, 18, 18)).getRGB());
    }

    public static void drawRoundedRect(float nameXStart, float nameYStart, float nameXEnd, float nameYEnd, float radius, int color) {
        drawRoundedRect(nameXStart, nameYStart, nameXEnd, nameYEnd, radius, color, true);
    }

    public static void originalRoundedRect(float nameXStart, float nameYStart, float nameXEnd, float nameYEnd, float radius, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float z = 0.0F;

        if (nameXStart > nameXEnd) {
            z = nameXStart;
            nameXStart = nameXEnd;
            nameXEnd = z;
        }

        if (nameYStart > nameYEnd) {
            z = nameYStart;
            nameYStart = nameYEnd;
            nameYEnd = z;
        }

        double x1 = (double) (nameXStart + radius);
        double y1 = (double) (nameYStart + radius);
        double x2 = (double) (nameXEnd - radius);
        double y2 = (double) (nameYEnd - radius);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        worldrenderer.begin(9, DefaultVertexFormats.POSITION);
        double degree = 0.017453292519943295D;

        double i;

        for (i = 0.0D; i <= 90.0D; i += 0.5D) {
            worldrenderer.pos(x2 + Math.sin(i * degree) * (double) radius, y2 + Math.cos(i * degree) * (double) radius, 0.0D).endVertex();
        }

        for (i = 90.0D; i <= 180.0D; i += 0.5D) {
            worldrenderer.pos(x2 + Math.sin(i * degree) * (double) radius, y1 + Math.cos(i * degree) * (double) radius, 0.0D).endVertex();
        }

        for (i = 180.0D; i <= 270.0D; i += 0.5D) {
            worldrenderer.pos(x1 + Math.sin(i * degree) * (double) radius, y1 + Math.cos(i * degree) * (double) radius, 0.0D).endVertex();
        }

        for (i = 270.0D; i <= 360.0D; i += 0.5D) {
            worldrenderer.pos(x1 + Math.sin(i * degree) * (double) radius, y2 + Math.cos(i * degree) * (double) radius, 0.0D).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void newDrawRect(float left, float top, float right, float bottom, int color) {
        float f3;

        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }

        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }

        f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
        worldrenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
        worldrenderer.pos((double) right, (double) top, 0.0D).endVertex();
        worldrenderer.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect(float nameXStart, float nameYStart, float nameXEnd, float nameYEnd, float radius, int color, boolean popPush) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float z = 0.0F;

        if (nameXStart > nameXEnd) {
            z = nameXStart;
            nameXStart = nameXEnd;
            nameXEnd = z;
        }

        if (nameYStart > nameYEnd) {
            z = nameYStart;
            nameYStart = nameYEnd;
            nameYEnd = z;
        }

        double x1 = (double) (nameXStart + radius);
        double y1 = (double) (nameYStart + radius);
        double x2 = (double) (nameXEnd - radius);
        double y2 = (double) (nameYEnd - radius);

        if (popPush) {
            GL11.glPushMatrix();
        }

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.0F);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        double degree = 0.017453292519943295D;

        double i;

        for (i = 0.0D; i <= 90.0D; i += 0.5D) {
            GL11.glVertex2d(x2 + Math.sin(i * degree) * (double) radius, y2 + Math.cos(i * degree) * (double) radius);
        }

        for (i = 90.0D; i <= 180.0D; i += 0.5D) {
            GL11.glVertex2d(x2 + Math.sin(i * degree) * (double) radius, y1 + Math.cos(i * degree) * (double) radius);
        }

        for (i = 180.0D; i <= 270.0D; i += 0.5D) {
            GL11.glVertex2d(x1 + Math.sin(i * degree) * (double) radius, y1 + Math.cos(i * degree) * (double) radius);
        }

        for (i = 270.0D; i <= 360.0D; i += 0.5D) {
            GL11.glVertex2d(x1 + Math.sin(i * degree) * (double) radius, y2 + Math.cos(i * degree) * (double) radius);
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        if (popPush) {
            GL11.glPopMatrix();
        }

    }

    public static void customRounded(float nameXStart, float nameYStart, float nameXEnd, float nameYEnd, float rTL, float rTR, float rBR, float rBL, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float z = 0.0F;

        if (nameXStart > nameXEnd) {
            z = nameXStart;
            nameXStart = nameXEnd;
            nameXEnd = z;
        }

        if (nameYStart > nameYEnd) {
            z = nameYStart;
            nameYStart = nameYEnd;
            nameYEnd = z;
        }

        double xTL = (double) (nameXStart + rTL);
        double yTL = (double) (nameYStart + rTL);
        double xTR = (double) (nameXEnd - rTR);
        double yTR = (double) (nameYStart + rTR);
        double xBR = (double) (nameXEnd - rBR);
        double yBR = (double) (nameYEnd - rBR);
        double xBL = (double) (nameXStart + rBL);
        double yBL = (double) (nameYEnd - rBL);

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.0F);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        double degree = 0.017453292519943295D;

        double i;

        for (i = 0.0D; i <= 90.0D; i += 0.25D) {
            GL11.glVertex2d(xBR + Math.sin(i * degree) * (double) rBR, yBR + Math.cos(i * degree) * (double) rBR);
        }

        for (i = 90.0D; i <= 180.0D; i += 0.25D) {
            GL11.glVertex2d(xTR + Math.sin(i * degree) * (double) rTR, yTR + Math.cos(i * degree) * (double) rTR);
        }

        for (i = 180.0D; i <= 270.0D; i += 0.25D) {
            GL11.glVertex2d(xTL + Math.sin(i * degree) * (double) rTL, yTL + Math.cos(i * degree) * (double) rTL);
        }

        for (i = 270.0D; i <= 360.0D; i += 0.25D) {
            GL11.glVertex2d(xBL + Math.sin(i * degree) * (double) rBL, yBL + Math.cos(i * degree) * (double) rBL);
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void fastRoundedRect(float nameXStart, float nameYStart, float nameXEnd, float nameYEnd, float radius) {
        float z = 0.0F;

        if (nameXStart > nameXEnd) {
            z = nameXStart;
            nameXStart = nameXEnd;
            nameXEnd = z;
        }

        if (nameYStart > nameYEnd) {
            z = nameYStart;
            nameYStart = nameYEnd;
            nameYEnd = z;
        }

        double x1 = (double) (nameXStart + radius);
        double y1 = (double) (nameYStart + radius);
        double x2 = (double) (nameXEnd - radius);
        double y2 = (double) (nameYEnd - radius);

        GL11.glEnable(2848);
        GL11.glLineWidth(1.0F);
        GL11.glBegin(9);
        double degree = 0.017453292519943295D;

        double i;

        for (i = 0.0D; i <= 90.0D; i += 0.25D) {
            GL11.glVertex2d(x2 + Math.sin(i * degree) * (double) radius, y2 + Math.cos(i * degree) * (double) radius);
        }

        for (i = 90.0D; i <= 180.0D; i += 0.25D) {
            GL11.glVertex2d(x2 + Math.sin(i * degree) * (double) radius, y1 + Math.cos(i * degree) * (double) radius);
        }

        for (i = 180.0D; i <= 270.0D; i += 0.25D) {
            GL11.glVertex2d(x1 + Math.sin(i * degree) * (double) radius, y1 + Math.cos(i * degree) * (double) radius);
        }

        for (i = 270.0D; i <= 360.0D; i += 0.25D) {
            GL11.glVertex2d(x1 + Math.sin(i * degree) * (double) radius, y2 + Math.cos(i * degree) * (double) radius);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
    }

    public static void drawTriAngle(float cx, float cy, float r, float n, Color color, boolean polygon) {
        cx = (float) ((double) cx * 2.0D);
        cy = (float) ((double) cy * 2.0D);
        double b = 6.2831852D / (double) n;
        double p = Math.cos(b);
        double s = Math.sin(b);

        r = (float) ((double) r * 2.0D);
        double x = (double) r;
        double y = 0.0D;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GL11.glLineWidth(1.0F);
        enableGlCap(2848);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.resetColor();
        glColor(color);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        worldrenderer.begin(polygon ? 9 : 2, DefaultVertexFormats.POSITION);

        for (int ii = 0; (float) ii < n; ++ii) {
            worldrenderer.pos(x + (double) cx, y + (double) cy, 0.0D).endVertex();
            double t = x;

            x = p * x - s * y;
            y = s * t + p * y;
        }

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f2 = (float) (col1 >> 16 & 255) / 255.0F;
        float f3 = (float) (col1 >> 8 & 255) / 255.0F;
        float f4 = (float) (col1 & 255) / 255.0F;
        float f5 = (float) (col2 >> 24 & 255) / 255.0F;
        float f6 = (float) (col2 >> 16 & 255) / 255.0F;
        float f7 = (float) (col2 >> 8 & 255) / 255.0F;
        float f8 = (float) (col2 & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawGradientSideways(float left, float top, float right, float bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f2 = (float) (col1 >> 16 & 255) / 255.0F;
        float f3 = (float) (col1 >> 8 & 255) / 255.0F;
        float f4 = (float) (col1 & 255) / 255.0F;
        float f5 = (float) (col2 >> 24 & 255) / 255.0F;
        float f6 = (float) (col2 >> 16 & 255) / 255.0F;
        float f7 = (float) (col2 >> 8 & 255) / 255.0F;
        float f8 = (float) (col2 & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2f(left, top);
        GL11.glVertex2f(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2f(right, bottom);
        GL11.glVertex2f(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
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
        resetCaps();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
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



    public static void drawFilledBox(AxisAlignedBB axisAlignedBB) {
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
    }

    public static void drawEntityOnScreen(double posX, double posY, float scale, EntityLivingBase entity) {
        GlStateManager.pushMatrix();
        GlStateManager.enableColorMaterial();
        GlStateManager.translate(posX, posY, 50.0D);
        GlStateManager.scale(-scale, scale, scale);
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

    public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase entity) {
        drawEntityOnScreen((double) posX, (double) posY, (float) scale, entity);
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
        GL11.glPushMatrix();
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
        GL11.glPopMatrix();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        double f3;

        if (left < right) {
            f3 = left;
            left = right;
            right = f3;
        }

        if (top < bottom) {
            f3 = top;
            top = bottom;
            bottom = f3;
        }

        float f31 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f31);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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

    public static void drawCircle(float x, float y, float radius, float lineWidth, int start, int end, Color color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(color);
        GL11.glEnable(2848);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);

        for (float i = (float) end; i >= (float) start; i -= 4.0F) {
            GL11.glVertex2f((float) ((double) x + Math.cos((double) i * 3.141592653589793D / 180.0D) * (double) (radius * 1.001F)), (float) ((double) y + Math.sin((double) i * 3.141592653589793D / 180.0D) * (double) (radius * 1.001F)));
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(float x, float y, float radius, float lineWidth, int start, int end) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(Color.WHITE);
        GL11.glEnable(2848);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);

        for (float i = (float) end; i >= (float) start; i -= 4.0F) {
            GL11.glVertex2f((float) ((double) x + Math.cos((double) i * 3.141592653589793D / 180.0D) * (double) (radius * 1.001F)), (float) ((double) y + Math.sin((double) i * 3.141592653589793D / 180.0D) * (double) (radius * 1.001F)));
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
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
    }

    public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
        byte sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushAttrib(8192);
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
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void drawFilledCircle(float xx, float yy, float radius, Color color) {
        byte sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));

            GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
            GL11.glVertex2f(xx + x, yy + y);
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
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

    public static void drawImage2(ResourceLocation image, float x, float y, int width, int height) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(x, y, x);
        RenderUtils.mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glTranslatef(-x, -y, -x);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawImage3(ResourceLocation image, float x, float y, int width, int height, float r, float g, float b, float al) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(r, g, b, al);
        GL11.glTranslatef(x, y, x);
        RenderUtils.mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glTranslatef(-x, -y, -x);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
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
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[0]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[1]);
        GlStateManager.translate(0.0D, 21.0D + -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0D, 0.0D);
        glColor(color);
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[2]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[3]);
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
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[0]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[1]);
        GlStateManager.translate(0.0F, 9.0F, 0.0F);
        glColor(color);
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[2]);
        glColor(backgroundColor);
        GL11.glCallList(RenderUtils6.DISPLAY_LISTS_2D[3]);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
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

    public static void makeScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scaledResolution = new ScaledResolution(RenderUtils.mc);
        int factor = scaledResolution.getScaleFactor();

        GL11.glScissor((int) (x * (float) factor), (int) (((float) scaledResolution.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void resetCaps() {
        RenderUtils6.glCapMap.forEach(RenderUtils6::setGlState);
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
        RenderUtils6.glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
        setGlState(cap, state);
    }

    public static void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }

    }

    public static void drawShadow(float x, float y, float width, float height) {
        drawTexturedRect(x - 9.0F, y - 9.0F, 9.0F, 9.0F, "paneltopleft");
        drawTexturedRect(x - 9.0F, y + height, 9.0F, 9.0F, "panelbottomleft");
        drawTexturedRect(x + width, y + height, 9.0F, 9.0F, "panelbottomright");
        drawTexturedRect(x + width, y - 9.0F, 9.0F, 9.0F, "paneltopright");
        drawTexturedRect(x - 9.0F, y, 9.0F, height, "panelleft");
        drawTexturedRect(x + width, y, 9.0F, height, "panelright");
        drawTexturedRect(x, y - 9.0F, width, 9.0F, "paneltop");
        drawTexturedRect(x, y + height, width, 9.0F, "panelbottom");
    }

    public static void drawTexturedRect(float x, float y, float width, float height, String image) {
        GL11.glPushMatrix();
        boolean enableBlend = GL11.glIsEnabled(3042);
        boolean disableAlpha = !GL11.glIsEnabled(3008);

        if (!enableBlend) {
            GL11.glEnable(3042);
        }

        if (!disableAlpha) {
            GL11.glDisable(3008);
        }

        RenderUtils.mc.getTextureManager().bindTexture(new ResourceLocation("effect/" + image + ".png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
        if (!enableBlend) {
            GL11.glDisable(3042);
        }

        if (!disableAlpha) {
            GL11.glEnable(3008);
        }

        GL11.glPopMatrix();
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

    static {
        for (int i = 0; i < RenderUtils6.DISPLAY_LISTS_2D.length; ++i) {
            RenderUtils6.DISPLAY_LISTS_2D[i] = GL11.glGenLists(1);
        }

        GL11.glNewList(RenderUtils6.DISPLAY_LISTS_2D[0], 4864);
        quickDrawRect(-7.0F, 2.0F, -4.0F, 3.0F);
        quickDrawRect(4.0F, 2.0F, 7.0F, 3.0F);
        quickDrawRect(-7.0F, 0.5F, -6.0F, 3.0F);
        quickDrawRect(6.0F, 0.5F, 7.0F, 3.0F);
        GL11.glEndList();
        GL11.glNewList(RenderUtils6.DISPLAY_LISTS_2D[1], 4864);
        quickDrawRect(-7.0F, 3.0F, -4.0F, 3.3F);
        quickDrawRect(4.0F, 3.0F, 7.0F, 3.3F);
        quickDrawRect(-7.3F, 0.5F, -7.0F, 3.3F);
        quickDrawRect(7.0F, 0.5F, 7.3F, 3.3F);
        GL11.glEndList();
        GL11.glNewList(RenderUtils6.DISPLAY_LISTS_2D[2], 4864);
        quickDrawRect(4.0F, -20.0F, 7.0F, -19.0F);
        quickDrawRect(-7.0F, -20.0F, -4.0F, -19.0F);
        quickDrawRect(6.0F, -20.0F, 7.0F, -17.5F);
        quickDrawRect(-7.0F, -20.0F, -6.0F, -17.5F);
        GL11.glEndList();
        GL11.glNewList(RenderUtils6.DISPLAY_LISTS_2D[3], 4864);
        quickDrawRect(7.0F, -20.0F, 7.3F, -17.5F);
        quickDrawRect(-7.3F, -20.0F, -7.0F, -17.5F);
        quickDrawRect(4.0F, -20.3F, 7.3F, -20.0F);
        quickDrawRect(-7.3F, -20.3F, -4.0F, -20.0F);
        GL11.glEndList();
        frustrum = new Frustum();
       // RenderUtils.zLevel = 0.0F;
    }
}
