package org.valkyrienskies.addon.world.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.valkyrienskies.addon.world.ValkyrienSkiesWorld;

public class AntiGravityCapabilityProvider implements ICapabilitySerializable<NBTTagDouble> {
    private final ICapabilityAntiGravity inst = ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY.getDefaultInstance();

    public AntiGravityCapabilityProvider(double multiplier) {
        inst.setMultiplier(multiplier);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY) {
            return ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY.cast(inst);
        }
        return null;
    }

    @Override
    public NBTTagDouble serializeNBT() {
        NBTBase nbt = ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY.getStorage()
                .writeNBT(ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY, inst, null);
        return (NBTTagDouble) nbt;
    }

    @Override
    public void deserializeNBT(NBTTagDouble nbt) {
        ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY.getStorage()
                .readNBT(ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY, inst, null, nbt);
    }
}
