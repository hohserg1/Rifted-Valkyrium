package org.valkyrienskies.addon.world.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import org.valkyrienskies.addon.world.WorldEventsCommon;

public class CommonProxyWorld {
    public void preInit(FMLStateEvent e) {
        MinecraftForge.EVENT_BUS.register(new WorldEventsCommon());
    }

    public void init(FMLStateEvent e) {}

    public void postInit(FMLStateEvent e) {}
}
