package org.valkyrienskies.addon.world;

import net.minecraft.potion.Potion;

public class VSWorldPotion extends Potion {
    public VSWorldPotion(boolean isBadEffectIn, int liquidColorIn, String name) {
        super(isBadEffectIn, liquidColorIn);
        setPotionName("effect." + name);
        setRegistryName(ValkyrienSkiesWorld.MOD_ID + ":" + name);
        setIconIndex(0, 1);
    }
}
