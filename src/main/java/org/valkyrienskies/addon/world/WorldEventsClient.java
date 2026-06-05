package org.valkyrienskies.addon.world;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.valkyrienskies.addon.world.util.LevitationUtil;

public class WorldEventsClient {
    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world != null && !minecraft.isGamePaused()) {
            LevitationUtil.addEntityLevitationEffects(minecraft.world);
        }
    }
}
