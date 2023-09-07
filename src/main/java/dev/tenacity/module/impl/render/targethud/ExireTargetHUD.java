package dev.tenacity.module.impl.render.targethud;

import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.GLUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;

import java.awt.Color;

import static dev.tenacity.utils.render.RenderUtil.*;
import static net.minecraft.util.MathHelper.clamp_float;

public class ExireTargetHUD extends TargetHUD {

    private final ContinualAnimation animation = new ContinualAnimation();

    public ExireTargetHUD() {
        super("Exire");
    }

    @Override
    public void render(float x, float y, float alpha, EntityLivingBase target) {
        double healthPercentage = clamp_float((target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount()), 0, 1);

        Color c1 = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), alpha);
        Color c2 = ColorUtil.applyOpacity(HUDMod.getClientColors().getSecond(), alpha);

        setWidth(tenacityBoldFont28.getStringWidth(target.getName()));
        setHeight(30);

        int alphaInt = (int) (alpha * 255);

        Color black = new Color(0, 0, 0, 255);


        Gui.drawRect2(x + 1, y + 1, getWidth() + 29.5f, getHeight() - 2, new Color(28, 28, 28, alphaInt).getRGB());

        float f = (float) (83 * healthPercentage);
        animation.animate(f, 40);

        // Black Health Bar
        drawGradientRect(x + 28.5f, y + 21, x + getWidth(), y + 27.5f, black.getRGB(), black.getRGB());

        // Health Bar
        drawGradientRect(x + 28.5f, y + 21, x + getWidth() * healthPercentage, y + 27.5f, c1.getRGB(), c2.getRGB());
        //drawGradientRect(x + 28.5f, y + 20, x + getWidth() * healthPercentage + f / 2, y + 27.5f, c1.getRGB(), c2.getRGB());

        int textColor = ColorUtil.applyOpacity(-1, alpha);
        int mcTextColor = ColorUtil.applyOpacity(-1, (float) Math.max(.1, alpha));
        GLUtil.startBlend();
        if (target instanceof AbstractClientPlayer) {
            color(textColor);
            renderPlayer2D(x + 3.5f, y + 3f, 23.5f, 23.5f, (AbstractClientPlayer) target);
        } else {
            fr.drawStringWithShadow("?", x + 17 - fr.getStringWidth("?") / 2f, y + 17 - fr.FONT_HEIGHT / 2f, mcTextColor);
        }

        GLUtil.startBlend();
        // Name
        tenacityBoldFont28.drawStringWithShadow(target.getName(), x + 28.5f, y + 4, mcTextColor);
    }


    @Override
    public void renderEffects(float x, float y, float alpha, boolean glow) {
        Gui.drawRect2(x, y, getWidth(), getHeight(), ColorUtil.applyOpacity(Color.BLACK.getRGB(), alpha));
    }

}