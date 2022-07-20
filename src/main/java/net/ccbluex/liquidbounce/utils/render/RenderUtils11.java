package net.ccbluex.liquidbounce.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import java.awt.*;

public class RenderUtils11 {

    public static float delta;

    public static float smooth(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;

        if (movement > 0.0F) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0F) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }

        return current + movement;
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
    public static void drawRoundedRect2(float x, float y, float x2, float y2, float round, int color) {
        x += (float) ((double) (round / 2.0F) + 0.5D);
        y += (float) ((double) (round / 2.0F) + 0.5D);
        x2 -= (float) ((double) (round / 2.0F) + 0.5D);
        y2 -= (float) ((double) (round / 2.0F) + 0.5D);
        Gui.drawRect((int) x, (int) y, (int) x2, (int) y2, color);
        circle(x2 - round / 2.0F, y + round / 2.0F, round, color);
        circle(x + round / 2.0F, y2 - round / 2.0F, round, color);
        circle(x + round / 2.0F, y + round / 2.0F, round, color);
        circle(x2 - round / 2.0F, y2 - round / 2.0F, round, color);
        Gui.drawRect((int) (x - round / 2.0F - 0.5F), (int) (y + round / 2.0F), (int) x2, (int) (y2 - round / 2.0F), color);
        Gui.drawRect((int) x, (int) (y + round / 2.0F), (int) (x2 + round / 2.0F + 0.5F), (int) (y2 - round / 2.0F), color);
        Gui.drawRect((int) (x + round / 2.0F), (int) (y - round / 2.0F - 0.5F), (int) (x2 - round / 2.0F), (int) (y2 - round / 2.0F), color);
        Gui.drawRect((int) (x + round / 2.0F), (int) y, (int) (x2 - round / 2.0F), (int) (y2 + round / 2.0F + 0.5F), color);
    }

    public static void drawRectBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        rectangle(x, y, x + width, y1, borderColor);
        rectangle(x1 - width, y, x1, y1, borderColor);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
        new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor3f((float) color.getRed() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getRed() / 255.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void circle(float x, float y, float radius, int fill) {
        GL11.glEnable(3042);
        R2DUtils.arc(x, y, 0.0F, 360.0F, radius, fill);
        GL11.glDisable(3042);
    }

    public static int width() {
        return (new ScaledResolution(Minecraft.getMinecraft())).getScaledWidth();
    }

    public static int height() {
        return (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight();
    }

    public static void circle(float x, float y, float radius, Color fill) {
        R2DUtils.arc(x, y, 0.0F, 360.0F, radius, fill);
    }

    public static void drawFilledCircle(int xx, int yy, float radius, Color col) {
        byte sections = 100;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));

            GL11.glColor4f((float) col.getRed() / 255.0F, (float) col.getGreen() / 255.0F, (float) col.getBlue() / 255.0F, (float) col.getAlpha() / 255.0F);
            GL11.glVertex2f((float) xx + x, (float) yy + y);
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
        float f = (float) (col >> 24 & 255) / 255.0F;
        float f1 = (float) (col >> 16 & 255) / 255.0F;
        float f2 = (float) (col >> 8 & 255) / 255.0F;
        float f3 = (float) (col & 255) / 255.0F;
        byte sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));

            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f((float) xx + x, (float) yy + y);
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(float xx, float yy, float radius, int col) {
        float f = (float) (col >> 24 & 255) / 255.0F;
        float f1 = (float) (col >> 16 & 255) / 255.0F;
        float f2 = (float) (col >> 8 & 255) / 255.0F;
        float f3 = (float) (col & 255) / 255.0F;
        byte sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));

            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f(xx + x, yy + y);
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col, int xLeft, int yAbove, int xRight, int yUnder) {
        float f = (float) (col >> 24 & 255) / 255.0F;
        float f1 = (float) (col >> 16 & 255) / 255.0F;
        float f2 = (float) (col >> 8 & 255) / 255.0F;
        float f3 = (float) (col & 255) / 255.0F;
        byte sections = 50;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.sin((double) i * dAngle));
            float y = (float) ((double) radius * Math.cos((double) i * dAngle));
            float xEnd = (float) xx + x;
            float yEnd = (float) yy + y;

            if (xEnd < (float) xLeft) {
                xEnd = (float) xLeft;
            }

            if (xEnd > (float) xRight) {
                xEnd = (float) xRight;
            }

            if (yEnd < (float) yAbove) {
                yEnd = (float) yAbove;
            }

            if (yEnd > (float) yUnder) {
                yEnd = (float) yUnder;
            }

            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f(xEnd, yEnd);
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth) {
        r *= 2.0D;
        x1 *= 2.0F;
        y1 *= 2.0F;
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;

        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glLineWidth((float) linewidth);
        GL11.glEnable(2848);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(3);

        for (int i = startPoint; (double) i <= arc; ++i) {
            double x = Math.sin((double) i * 3.141592653589793D / 180.0D) * r;
            double y = Math.cos((double) i * 3.141592653589793D / 180.0D) * r;

            GL11.glVertex2d((double) x1 + x, (double) y1 + y);
        }

        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
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

    public static void drawRainbowRectVertical(int x, int y, int x1, int y1, int segments, float alpha) {
        if (segments < 1) {
            segments = 1;
        }

        if (segments > y1 - y) {
            segments = y1 - y;
        }

        int segmentLength = (y1 - y) / segments;
        long time = System.nanoTime();

        for (int i = 0; i < segments; ++i) {
            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y + segmentLength * i - 1, x1, y + (segmentLength + 1) * i, ColorManager.rainbow(time, (float) i * 0.1F, alpha).getRGB(), ColorManager.rainbow(time, ((float) i + 0.1F) * 0.1F, alpha).getRGB());
        }

    }

    public static void drawCircle(int xx, int yy, int radius, Color col) {
        byte sections = 70;
        double dAngle = 6.283185307179586D / (double) sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);

        for (int i = 0; i < sections; ++i) {
            float x = (float) ((double) radius * Math.cos((double) i * dAngle));
            float y = (float) ((double) radius * Math.sin((double) i * dAngle));

            GL11.glColor4f((float) col.getRed() / 255.0F, (float) col.getGreen() / 255.0F, (float) col.getBlue() / 255.0F, (float) col.getAlpha() / 255.0F);
            GL11.glVertex2f((float) xx + x, (float) yy + y);
        }

        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) ((double) RenderUtils11.delta * speed);

        return animation < finalState ? (animation + (double) add >= finalState ? finalState : animation + (double) add) : (animation - (double) add <= finalState ? finalState : animation - (double) add);
    }

    public static void drawOutline(float x, float y, float width, float height, float lineWidth, int color) {
        drawRect(x, y, x + width, y + lineWidth, color);
        drawRect(x, y, x + lineWidth, y + height, color);
        drawRect(x, y + height - lineWidth, x + width, y + height, color);
        drawRect(x + width - lineWidth, y, x + width, y + height, color);
    }

    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * (double) Minecraft.getMinecraft().timer.renderPartialTicks;
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static float[] getRGBAs(int rgb) {
        return new float[] { (float) (rgb >> 16 & 255) / 255.0F, (float) (rgb >> 8 & 255) / 255.0F, (float) (rgb & 255) / 255.0F, (float) (rgb >> 24 & 255) / 255.0F};
    }

    public static Color makeDarker(Color curColor, int distance) {
        int red = curColor.getRed();
        int green = curColor.getGreen();
        int blue = curColor.getBlue();

        red -= distance;
        green -= distance;
        blue -= distance;
        if (red < 0) {
            red = 0;
        }

        if (green < 0) {
            green = 0;
        }

        if (blue < 0) {
            blue = 0;
        }

        return new Color(red, green, blue);
    }

    public static void drawBoundingBox(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.color(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        float scale = 0.1F;

        GL11.glScalef(0.1F, 0.1F, 0.1F);
        drawCircle(x *= 10.0D, y *= 10.0D, (double) (radius *= 10.0F), insideC);
        GL11.glScalef(10.0F, 10.0F, 10.0F);
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }

    public static int getHexRGB(int hex) {
        return -16777216 | hex;
    }

    public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
        new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        int factor = sr.getScaleFactor();

        GL11.glScissor((int) (x * (float) factor), (int) (((float) sr.getScaledHeight() - y2) * (float) factor), (int) ((x2 - x) * (float) factor), (int) ((y2 - y) * (float) factor));
    }

    public static void drawBorderedRect(double x2, double d2, double x22, double e2, float l1, int col1, int col2) {
        drawRect(x2, d2, x22, e2, col2);
        float f2 = (float) (col1 >> 24 & 255) / 255.0F;
        float f22 = (float) (col1 >> 16 & 255) / 255.0F;
        float f3 = (float) (col1 >> 8 & 255) / 255.0F;
        float f4 = (float) (col1 & 255) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, d2);
        GL11.glVertex2d(x2, e2);
        GL11.glVertex2d(x22, e2);
        GL11.glVertex2d(x22, d2);
        GL11.glVertex2d(x2, d2);
        GL11.glVertex2d(x22, d2);
        GL11.glVertex2d(x2, e2);
        GL11.glVertex2d(x22, e2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
        R2DUtils.enableGL2D();
        glColor(borderColor);
        R2DUtils.drawRect(x + width, y, x1 - width, y + width);
        R2DUtils.drawRect(x, y, x + width, y1);
        R2DUtils.drawRect(x1 - width, y, x1, y1);
        R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
        R2DUtils.disableGL2D();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        R2DUtils.enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        R2DUtils.drawVLine(x *= 2.0F, y *= 2.0F, y1 *= 2.0F, borderC);
        R2DUtils.drawVLine((x1 *= 2.0F) - 1.0F, y, y1, borderC);
        R2DUtils.drawHLine(x, x1 - 1.0F, y, borderC);
        R2DUtils.drawHLine(x, x1 - 2.0F, y1 - 1.0F, borderC);
        R2DUtils.drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        R2DUtils.disableGL2D();
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

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = 1.0F - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];

        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);

        return color3;
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

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline, int background) {
        R2DUtils.drawRect(x1, y1, x2, y2, outline);
        R2DUtils.drawRect(x1 + 1.0D, y1 + 1.0D, x2 - 1.0D, y2 - 1.0D, inline);
        R2DUtils.drawRect(x1 + 2.0D, y1 + 2.0D, x2 - 2.0D, y2 - 2.0D, background);
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
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

    public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
        float red = 0.003921569F * (float) redRGB;
        float green = 0.003921569F * (float) greenRGB;
        float blue = 0.003921569F * (float) blueRGB;

        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0F;
        float red = (float) (hex >> 16 & 255) / 255.0F;
        float green = (float) (hex >> 8 & 255) / 255.0F;
        float blue = (float) (hex & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);
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

    public static void drawVerticalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        float f4 = (float) (col2 >> 24 & 255) / 255.0F;
        float f5 = (float) (col2 >> 16 & 255) / 255.0F;
        float f6 = (float) (col2 >> 8 & 255) / 255.0F;
        float f7 = (float) (col2 & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer world = tessellator.getWorldRenderer();

        world.begin(7, DefaultVertexFormats.POSITION_COLOR);
        world.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
        world.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
        world.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        world.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawHorizontalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float) (col1 >> 24 & 255) / 255.0F;
        float f1 = (float) (col1 >> 16 & 255) / 255.0F;
        float f2 = (float) (col1 >> 8 & 255) / 255.0F;
        float f3 = (float) (col1 & 255) / 255.0F;
        float f4 = (float) (col2 >> 24 & 255) / 255.0F;
        float f5 = (float) (col2 >> 16 & 255) / 255.0F;
        float f6 = (float) (col2 >> 8 & 255) / 255.0F;
        float f7 = (float) (col2 & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer world = tessellator.getWorldRenderer();

        world.begin(7, DefaultVertexFormats.POSITION_COLOR);
        world.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
        world.pos(left, bottom, 0.0D).color(f1, f2, f3, f).endVertex();
        world.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        world.pos(right, top, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation image, Color color) {
        new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        GL11.glEnable(2881);
        GL11.glDisable(2881);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawCircle(double x, double y, double radius, int c) {
        GL11.glEnable('?');
        GL11.glEnable(2881);
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

        for (int i = 0; i <= 360; ++i) {
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

        GL11.glDisable(2881);
        GL11.glClear(0);
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0F;
        cy *= 2.0F;
        float theta = (float) (6.2831852D / (double) num_segments);
        float p = (float) Math.cos((double) theta);
        float s = (float) Math.sin((double) theta);
        float x = r *= 2.0F;
        float y = 0.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        glColor(c);
        GL11.glBegin(2);

        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            float t = x;

            x = p * x - s * y;
            y = s * t + p * y;
        }

        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        glColor(c);
        GL11.glPopMatrix();
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

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double d0;

        if (left < right) {
            d0 = left;
            left = right;
            right = d0;
        }

        if (top < bottom) {
            d0 = top;
            top = bottom;
            bottom = d0;
        }

        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f1, f2, f3, f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).endVertex();
        worldRenderer.pos(right, top, 0.0D).endVertex();
        worldRenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
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

    public static void drawBorderedRect(float x2, float y2, float x22, float y22, float l1, int col1, int col2) {
        drawRect(x2, y2, x22, y22, col2);
        float f2 = (float) (col1 >> 24 & 255) / 255.0F;
        float f22 = (float) (col1 >> 16 & 255) / 255.0F;
        float f3 = (float) (col1 >> 8 & 255) / 255.0F;
        float f4 = (float) (col1 & 255) / 255.0F;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y22);
        GL11.glVertex2d((double) x22, (double) y22);
        GL11.glVertex2d((double) x22, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x22, (double) y2);
        GL11.glVertex2d((double) x2, (double) y22);
        GL11.glVertex2d((double) x22, (double) y22);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawBordRect(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
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

    public static void color(int color, float alpha) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);
    }

    public static Vec3 interpolateRender(EntityPlayer player) {
        float part = Minecraft.getMinecraft().timer.renderPartialTicks;
        double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) part;
        double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) part;
        double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) part;

        return new Vec3(interpX, interpY, interpZ);
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

    public static void drawCylinderESP(EntityLivingBase entity, double x, double y, double z) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-entity.width, 0.0F, 1.0F, 0.0F);
        glColor((new Color(1, 89, 1, 150)).getRGB());
        enableSmoothLine(0.1F);
        Cylinder c = new Cylinder();

        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        c.setDrawStyle(100011);
        c.draw(0.0F, 0.2F, 0.5F, 5, 300);
        disableSmoothLine();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 0.5D, z);
        GL11.glRotatef(-entity.width, 0.0F, 1.0F, 0.0F);
        glColor((new Color(2, 168, 2, 150)).getRGB());
        enableSmoothLine(0.1F);
        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        c.setDrawStyle(100011);
        c.draw(0.2F, 0.0F, 0.5F, 5, 300);
        disableSmoothLine();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (float) ((System.currentTimeMillis() + (long) offset) % (long) speed);

        hue /= (float) speed;
        return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
    }

    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }

    public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glEnable(2881);
        GL11.glDisable(2881);
        drawScaledRect(0, 0, 0.0F, 0.0F, sizex, sizey, sizex, sizey, (float) sizex, (float) sizey);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        int scaleFactor = 1;
        int k = Minecraft.getMinecraft().gameSettings.guiScale;

        if (k == 0) {
            k = 1000;
        }

        while (scaleFactor < k && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }

        GL11.glScissor(x * scaleFactor, Minecraft.getMinecraft().displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }

    public static class R2DUtils {

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

        public static void drawCircle(double x, double y, double radius, int c) {
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

        public static void drawRoundedRect2(float x, float y, float x2, float y2, float round, int color) {
            x += (float) ((double) (round / 2.0F) + 0.5D);
            y += (float) ((double) (round / 2.0F) + 0.5D);
            x2 -= (float) ((double) (round / 2.0F) + 0.5D);
            y2 -= (float) ((double) (round / 2.0F) + 0.5D);
            Gui.drawRect((int) x, (int) y, (int) x2, (int) y2, color);
            circle(x2 - round / 2.0F, y + round / 2.0F, round, color);
            circle(x + round / 2.0F, y2 - round / 2.0F, round, color);
            circle(x + round / 2.0F, y + round / 2.0F, round, color);
            circle(x2 - round / 2.0F, y2 - round / 2.0F, round, color);
            Gui.drawRect((int) (x - round / 2.0F - 0.5F), (int) (y + round / 2.0F), (int) x2, (int) (y2 - round / 2.0F), color);
            Gui.drawRect((int) x, (int) (y + round / 2.0F), (int) (x2 + round / 2.0F + 0.5F), (int) (y2 - round / 2.0F), color);
            Gui.drawRect((int) (x + round / 2.0F), (int) (y - round / 2.0F - 0.5F), (int) (x2 - round / 2.0F), (int) (y2 - round / 2.0F), color);
            Gui.drawRect((int) (x + round / 2.0F), (int) y, (int) (x2 - round / 2.0F), (int) (y2 + round / 2.0F + 0.5F), color);
        }

        public static void circle(float x, float y, float radius, int fill) {
            arc(x, y, 0.0F, 360.0F, radius, fill);
        }

        public static void circle(float x, float y, float radius, Color fill) {
            arc(x, y, 0.0F, 360.0F, radius, fill);
        }

        public static void arc(float x, float y, float start, float end, float radius, int color) {
            arcEllipse(x, y, start, end, radius, radius, color);
        }

        public static void arc(float x, float y, float start, float end, float radius, Color color) {
            arcEllipse(x, y, start, end, radius, radius, color);
        }

        public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
            GlStateManager.color(0.0F, 0.0F, 0.0F);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
            float temp = 0.0F;

            if (start > end) {
                temp = end;
                end = start;
                start = temp;
            }

            float f = (float) (color >> 24 & 255) / 255.0F;
            float f1 = (float) (color >> 16 & 255) / 255.0F;
            float f2 = (float) (color >> 8 & 255) / 255.0F;
            float f3 = (float) (color & 255) / 255.0F;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(f1, f2, f3, f);
            float i;
            float ldx;
            float ldy;

            if (f > 0.5F) {
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
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

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

        public static void drawRect(double x2, double y2, double x1, double y1, int color) {
            enableGL2D();
            glColor(color);
            drawRect(x2, y2, x1, y1);
            disableGL2D();
        }

        private static void drawRect(double x2, double y2, double x1, double y1) {
            GL11.glBegin(7);
            GL11.glVertex2d(x2, y1);
            GL11.glVertex2d(x1, y1);
            GL11.glVertex2d(x1, y2);
            GL11.glVertex2d(x2, y2);
            GL11.glEnd();
        }

        public static void glColor(int hex) {
            float alpha = (float) (hex >> 24 & 255) / 255.0F;
            float red = (float) (hex >> 16 & 255) / 255.0F;
            float green = (float) (hex >> 8 & 255) / 255.0F;
            float blue = (float) (hex & 255) / 255.0F;

            GL11.glColor4f(red, green, blue, alpha);
        }

        public static void drawRect(float x, float y, float x1, float y1, int color) {
            enableGL2D();
            glColor(color);
            drawRect(x, y, x1, y1);
            disableGL2D();
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

        public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
            enableGL2D();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            drawVLine(x *= 2.0F, y *= 2.0F, y1 *= 2.0F, borderC);
            drawVLine((x1 *= 2.0F) - 1.0F, y, y1, borderC);
            drawHLine(x, x1 - 1.0F, y, borderC);
            drawHLine(x, x1 - 2.0F, y1 - 1.0F, borderC);
            drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            disableGL2D();
        }

        public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
            enableGL2D();
            GL11.glShadeModel(7425);
            GL11.glBegin(7);
            glColor(topColor);
            GL11.glVertex2f(x, y1);
            GL11.glVertex2f(x1, y1);
            glColor(bottomColor);
            GL11.glVertex2f(x1, y);
            GL11.glVertex2f(x, y);
            GL11.glEnd();
            GL11.glShadeModel(7424);
            disableGL2D();
        }

        public static void drawHLine(float x, float y, float x1, int y1) {
            if (y < x) {
                float f = x;

                x = y;
                y = f;
            }

            drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
        }

        public static void drawVLine(float x, float y, float x1, int y1) {
            if (x1 < y) {
                float f = y;

                y = x1;
                x1 = f;
            }

            drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
        }

        public static void drawHLine(float x, float y, float x1, int y1, int y2) {
            if (y < x) {
                float f = x;

                x = y;
                y = f;
            }

            drawGradientRect(x, x1, y + 1.0F, x1 + 1.0F, y1, y2);
        }

        public static void drawRect(float x, float y, float x1, float y1) {
            GL11.glBegin(7);
            GL11.glVertex2f(x, y1);
            GL11.glVertex2f(x1, y1);
            GL11.glVertex2f(x1, y);
            GL11.glVertex2f(x, y);
            GL11.glEnd();
        }
    }

    public static class R3DUtils {

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

        public static void drawOutlinedBox(AxisAlignedBB box) {
            if (box != null) {
                Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
                GL11.glBegin(3);
                GL11.glVertex3d(box.minX, box.minY, box.minZ);
                GL11.glVertex3d(box.maxX, box.minY, box.minZ);
                GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
                GL11.glVertex3d(box.minX, box.minY, box.maxZ);
                GL11.glVertex3d(box.minX, box.minY, box.minZ);
                GL11.glEnd();
                GL11.glBegin(3);
                GL11.glVertex3d(box.minX, box.maxY, box.minZ);
                GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
                GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
                GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
                GL11.glVertex3d(box.minX, box.maxY, box.minZ);
                GL11.glEnd();
                GL11.glBegin(1);
                GL11.glVertex3d(box.minX, box.minY, box.minZ);
                GL11.glVertex3d(box.minX, box.maxY, box.minZ);
                GL11.glVertex3d(box.maxX, box.minY, box.minZ);
                GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
                GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
                GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
                GL11.glVertex3d(box.minX, box.minY, box.maxZ);
                GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
                GL11.glEnd();
            }
        }

        public static void drawFilledBox(AxisAlignedBB mask) {
            GL11.glBegin(4);
            GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
            GL11.glEnd();
            GL11.glBegin(4);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
            GL11.glEnd();
            GL11.glBegin(4);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
            GL11.glEnd();
            GL11.glBegin(4);
            GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
            GL11.glEnd();
            GL11.glBegin(4);
            GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
            GL11.glEnd();
            GL11.glBegin(4);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
            GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
            GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
            GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
            GL11.glEnd();
        }

        public static void drawOutlinedBoundingBox(AxisAlignedBB aabb) {
            GL11.glBegin(3);
            GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
            GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
            GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
            GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
            GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
            GL11.glEnd();
            GL11.glBegin(3);
            GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
            GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
            GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
            GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
            GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
            GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
            GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
            GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
            GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
            GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
            GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
            GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
            GL11.glEnd();
        }
    }
}
