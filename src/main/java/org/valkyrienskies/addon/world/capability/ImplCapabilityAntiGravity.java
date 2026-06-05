package org.valkyrienskies.addon.world.capability;

public class ImplCapabilityAntiGravity implements ICapabilityAntiGravity {
    private double multiplier = 1D;

    @Override
    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
