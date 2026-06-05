package org.valkyrienskies.addon.control.capability.lastRelay;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface ICapabilityLastRelay {
    @Nullable
    BlockPos getLastRelay();

    void setLastRelay(@Nullable BlockPos pos);

    boolean hasLastRelay();
}