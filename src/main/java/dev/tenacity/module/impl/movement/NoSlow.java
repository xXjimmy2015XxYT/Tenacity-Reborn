package dev.tenacity.module.impl.movement;

import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.SlowDownEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Watchdog", "Vanilla", "NCP", "Watchdog");
    private final NumberSetting forward = new NumberSetting("Forward Multiplier", 1f, 1f, 0.2f, 0.1f);
    private final NumberSetting strafe = new NumberSetting("Strafe Multiplier", 1f, 1f, 0.2f, 0.1f);

    private boolean synced;

    public NoSlow() {
        super("NoSlow", Category.MOVEMENT, "Allows you to move at normal speed while using items.");
        this.addSettings(mode, forward, strafe);
    }

    @Override
    public void onSlowDownEvent(SlowDownEvent event) {
        event.setForwardMult(forward.getValue().floatValue());
        event.setStrafeMult(strafe.getValue().floatValue());
        event.cancel();
    }

    @Override
    public void onMotionEvent(MotionEvent e) {
        this.setSuffix(mode.getMode());
        switch (mode.getMode()) {
            case "Watchdog":
                if (mc.thePlayer.onGround && mc.thePlayer.isUsingItem() && MovementUtils.isMoving()) {
                    if (e.isPre()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        synced = true;
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem < 8 ? mc.thePlayer.inventory.currentItem + 1 : mc.thePlayer.inventory.currentItem - 1));
                        synced = false;
                    }
                }
                if (!synced) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    synced = true;
                }
                break;
            case "NCP":
                if (MovementUtils.isMoving() && mc.thePlayer.isUsingItem() && mc.thePlayer.isOnGround()) {
                    if (e.isPre()) {
                        PacketUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    } else {
                        PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                    }

                }
                break;
        }
    }

}