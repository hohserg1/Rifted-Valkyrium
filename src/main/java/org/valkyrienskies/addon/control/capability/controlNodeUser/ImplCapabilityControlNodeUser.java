package org.valkyrienskies.addon.control.capability.controlNodeUser;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;
import org.valkyrienskies.addon.control.network.VSNodeControlMessage;
import org.valkyrienskies.addon.control.tileentity.TileEntityControlNodeImpl;
import org.valkyrienskies.mod.common.ships.ship_world.PhysicsObject;

public class ImplCapabilityControlNodeUser implements ICapabilityControlNodeUser {
    private PhysicsObject ship;
    private BlockPos usedControlNodePos;

    public PhysicsObject getShip() {
        return this.ship;
    }

    public void setShip(PhysicsObject physicsObject) {
        this.ship = physicsObject;
    }

    @Override
    public BlockPos getUsedControlNodePos() {
        return this.usedControlNodePos;
    }

    @Override
    public void setUsedControlNodePos(BlockPos pos) {
        this.usedControlNodePos = pos;
    }

    @Override
    public void stopUsingEverything() {
        this.setShip(null);
        this.setUsedControlNodePos(null);
    }

    @Override
    public void onClientTick() {
        if (this.usedControlNodePos != null) {
            ValkyrienSkiesControl.controlNodeNetwork.sendToServer(new VSNodeControlMessage(this.getUsedControlNodePos(), this.getControls()));
        }
    }

    private int getControls() {
        if (this.usedControlNodePos == null) return 0;
        TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(this.usedControlNodePos);
        if (!(tileEntity instanceof TileEntityControlNodeImpl teControlNode)) return 0;
        return teControlNode.getNodeControls().getControls();
    }
}
