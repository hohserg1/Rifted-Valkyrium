package org.valkyrienskies.addon.control.capability.lastRelay;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ImplCapabilityLastRelay implements ICapabilityLastRelay {
    @Nullable
    private BlockPos lastRelay;

    @Override
    @Nullable
    public BlockPos getLastRelay() {
        return this.lastRelay;
    }

    @Override
    public void setLastRelay(@Nullable BlockPos pos) {
        this.lastRelay = pos;
    }

    @Override
    public boolean hasLastRelay() {
        return this.lastRelay != null;
    }
}
