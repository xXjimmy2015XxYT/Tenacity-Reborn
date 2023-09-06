package dev.tenacity.module.impl.render.targethud;

import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.font.CustomFont;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.text.DecimalFormat;

import static dev.tenacity.utils.render.ColorUtil.applyOpacity;
import static dev.tenacity.utils.render.RenderUtil.*;

public class AdaptOtherTargetHUD extends TargetHUD {

    private final ContinualAnimation animation = new ContinualAnimation();
    private final DecimalFormat DF_1 = new DecimalFormat("0.0");

    public AdaptOtherTargetHUD() {
        super("Adapt 2");
    }

    @Override
    public void render(float x, float y, float alpha, EntityLivingBase target) {
        CustomFont rubikBold = rubikFont.boldSize(18);
        CustomFont rubikSmall = rubikFont.size(13);
        setWidth(Math.max(100, rubikBold.getStringWidth(target.getName()) + 45));
        setHeight(39.5F);

        double healthPercentage = MathHelper.clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0, 1);
        int bg = new Color(0, 0, 0, 0.4F * alpha).getRGB();

        // Draw background
        Gui.drawRect(x, y, x + getWidth(), y + 39.5F, bg);

        // Draw health bar

        // damage anim
        float endWidth = (float) Math.max(0, (getWidth() - 3.5) * healthPercentage);
        animation.animate(endWidth, 18);

        if (animation.getOutput() > 0) {
            drawGradientRectBordered(x + 2.5, y + 36.5, x + 1.5 + animation.getOutput(), y + 39, 0.74,
                    applyOpacity(0xFF009C41, alpha),
                    applyOpacity(0xFF8EFFC1, alpha), bg, bg);
        }
        double armorValue = target.getTotalArmorValue() / 20.0;
        if (armorValue > 0) {
            drawGradientRectBordered(x + 2.5, y + 32, x + 1.5 + ((getWidth() - 3.5) * armorValue), y + 34.5, 0.74,
                    applyOpacity(0xFF0067B0, alpha),
                    applyOpacity(0xFF39D5FF, alpha), bg, bg);
        }

        // Draw head
        GlStateManager.pushMatrix();
        setAlphaLimit(0);
        int textColor = applyOpacity(-1, alpha);
        if (target instanceof AbstractClientPlayer) {
            color(textColor);
            GlStateManager.scale(1, 1, 1);
            renderPlayer2D(x + 3, y + 3, 32, 32, (AbstractClientPlayer) target);
        } else {
            Gui.drawRect2(x + 3, y + 3, 25, 25, bg);
            GlStateManager.scale(2, 2, 2);
            rubikBold.drawStringWithShadow("?", (x + 11) / 2.0F, (y + 11) / 2.0F, textColor);
        }
        GlStateManager.popMatrix();

        // Draw name
        rubikBold.drawString(target.getName(), x + 31, y + 5, textColor);
        rubikSmall.drawString("Distance: " + DF_1.format(mc.thePlayer.getDistanceToEntity(target)) + "m", x + 31, y + 15, textColor);
    }


    @Override
    public void renderEffects(float x, float y, float alpha, boolean glow) {
        Gui.drawRect(x, y, x + getWidth(), y + 39.5F, applyOpacity(Color.BLACK.getRGB(), alpha));
    }

}