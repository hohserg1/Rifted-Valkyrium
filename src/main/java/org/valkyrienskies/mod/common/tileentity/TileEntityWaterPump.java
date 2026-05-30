package org.valkyrienskies.mod.common.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import org.valkyrienskies.mod.common.capability.VSCapabilityRegistry;
import org.valkyrienskies.mod.common.capability.entity_ship_draggable.IEntityShipDraggable;

import java.util.List;

public class TileEntityWaterPump extends TileEntity implements ITickable {

    private final double pumpRadius;

    public TileEntityWaterPump() {
        this.pumpRadius = 5;
    }

    @Override
    public void update() {
        final AxisAlignedBB pumpRangeBB = new AxisAlignedBB(pos, pos).grow(pumpRadius);
        final List<Entity> entitiesInPumpRadius = world.getEntitiesWithinAABBExcludingEntity(null, pumpRangeBB);

        for (final Entity entity : entitiesInPumpRadius) {
            IEntityShipDraggable draggable = entity.getCapability(VSCapabilityRegistry.VS_ENTITY_SHIP_DRAGGABLE, null);
            if (draggable == null) continue;

            draggable.setTicksAirPocket(2);
            entity.setAir(300);
        }
    }
}
