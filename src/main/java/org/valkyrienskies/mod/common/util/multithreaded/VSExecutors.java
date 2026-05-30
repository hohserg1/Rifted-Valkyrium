package org.valkyrienskies.mod.common.util.multithreaded;

import java.util.concurrent.Executor;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.server.FMLServerHandler;
import org.valkyrienskies.mod.common.capability.VSCapabilityRegistry;
import org.valkyrienskies.mod.common.capability.ship_world.IShipWorld;
import org.valkyrienskies.mod.common.ships.ship_world.WorldServerShipManager;

public class VSExecutors {

    public static final Executor SERVER =
        runnable -> FMLServerHandler.instance().getServer().addScheduledTask(runnable);
    public static final Executor CLIENT = FMLClientHandler.instance().getClient()::addScheduledTask;

    /**
     * Returns an executor to execute tasks on the physics thread of the selected world
     */
    public static Executor physics(WorldServer world) {
        IShipWorld shipWorld = world.getCapability(VSCapabilityRegistry.VS_SHIP_WORLD, null);
        if (shipWorld == null) {
            throw new RuntimeException("IShipWorld doesn't appear to exist!");
        }
        if (!(shipWorld.getManager() instanceof WorldServerShipManager serverShipManager)) {
            throw new RuntimeException("Given ship manager is not instance of WorldServerShipManager!");
        }
        return serverShipManager.getPhysicsLoop()::addScheduledTask;
    }

    /**
     * Returns an executor to execute tasks on the thread of the selected world
     */
    public static Executor forWorld(WorldServer world) {
        return world::addScheduledTask;
    }

}
