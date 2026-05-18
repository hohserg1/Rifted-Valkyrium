package org.valkyrienskies.addon.control.block.multiblocks;

import net.minecraft.util.math.BlockPos;

public enum EnumMultiblockRotation {

    NONE(0),
    QUARTER(90),
    HALF(180),
    THREE_FOURTHS(270);

    private final int yaw;

    EnumMultiblockRotation(int yaw) {
        this.yaw = yaw;
    }

    public BlockPos rotatePos(BlockPos pos) {
        if (this.yaw == 0) {
            return pos;
        }
        else if (this.yaw == 90) {
            return new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
        }
        else if (this.yaw == 180) {
            return new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
        }
        else if (this.yaw == 270) {
            return new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
        }
        throw new IllegalStateException("How the hell did we get here?");
    }

    public int getYaw() {
        return this.yaw;
    }
}
