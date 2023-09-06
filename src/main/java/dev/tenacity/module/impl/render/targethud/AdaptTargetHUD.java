package dev.tenacity.module.impl.render.targethud;

import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.render.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTranslated;

public class AdaptTargetHUD extends TargetHUD {

    private final ContinualAnimation animation = new ContinualAnimation();
    private final DecimalFormat DF_1 = new DecimalFormat("0.0");


    public AdaptTargetHUD() {
        super("Adapt");
    }

    @Override
    public void render(float x, float y, float alpha, EntityLivingBase target) {
        setWidth(Math.max(115, FontUtil.tenacityBoldFont22.getStringWidth(target.getName()) + 40));
        setHeight(37);

        Color c1 = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), alpha);
        Color c2 = ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), alpha);
        Color color = new Color(20, 18, 18, (int) (90 * alpha));

        int textColor = ColorUtil.applyOpacity(-1, alpha);

        RoundedUtil.drawRound(x, y, getWidth(), getHeight(), 5, color);

        if (target instanceof AbstractClientPlayer) {
            StencilUtil.initStencilToWrite();
            RenderUtil.renderRoundedRect(x + 3, y + 3, 31, 31, 6, -1);
            StencilUtil.readStencilBuffer(1);
            RenderUtil.color(-1, alpha);
            renderPlayer2D(x + 3, y + 3, 31, 31, (AbstractClientPlayer) target);
            StencilUtil.uninitStencilBuffer();
            GlStateManager.disableBlend();
        } else {
            FontUtil.tenacityBoldFont32.drawCenteredStringWithShadow("?", x + 20, y + 17 - FontUtil.tenacityBoldFont32.getHeight() / 2f, textColor);
        }

        float realHealthHeight = 5;

        float realHealthWidth = getWidth() - 44;

        float healthWidth = animation.getOutput();

        float healthPercent = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0, 1);


        RoundedUtil.drawGradientHorizontal(x + 37.5f, (y + getHeight() - 10.5f), healthWidth - 7.5f, realHealthHeight, 2.5f, c1, c2);

        animation.animate(realHealthWidth * healthPercent, 18);

        FontUtil.tenacityBoldFont18.drawStringWithShadow(target.getName(), x + 35.5f, y + 5, textColor);

        FontUtil.tenacityFont18.drawStringWithShadow("Distance: " + DF_1.format(mc.thePlayer.getDistanceToEntity(target)),
                x + 35.5f, y + 15, textColor);

        float targetHealth = target.getHealth();
        float targetAbsorptionAmount = target.getAbsorptionAmount();

        String healthText = String.valueOf((int) Math.ceil(targetHealth + targetAbsorptionAmount));
        FontUtil.tenacityFont16.drawStringWithShadow(healthText, x + healthWidth + 32.5f, (y + getHeight() - 11.8f), textColor);
    }


    @Override
    public void renderEffects(float x, float y, float alpha, boolean glow) {
        RoundedUtil.drawRound(x, y, getWidth(), getHeight(), 5, ColorUtil.applyOpacity(Color.BLACK, alpha));
    }
}