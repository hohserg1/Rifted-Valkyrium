package org.valkyrienskies.addon.world;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;

public final class PotionInit {
    private static final String LEVITATION_POTION_NAME = "valkyrium-levitation";
    private static final String LEVITATION_JUMP_POTION_NAME = "valkyrium-levitation-jump";

    public static final VSWorldPotion LEVITATION_POTION_EFFECT = new VSWorldPotion(false, 8360, LEVITATION_POTION_NAME);
    public static final VSWorldPotion LEVITATION_JUMP_POTION_EFFECT = new VSWorldPotion(false, 11600000, LEVITATION_JUMP_POTION_NAME);

    private static final PotionType LEVITATION_POTION_TYPE = (PotionType) new PotionType(
            LEVITATION_POTION_NAME,
            new PotionEffect(LEVITATION_POTION_EFFECT, 1200, 0)
    ).setRegistryName(LEVITATION_POTION_NAME);
    private static final PotionType LONG_LEVITATION_POTION_TYPE = (PotionType) new PotionType(
            LEVITATION_POTION_NAME,
            new PotionEffect(LEVITATION_POTION_EFFECT, 2400, 0)
    ).setRegistryName("long" + LEVITATION_POTION_NAME);
    private static final PotionType STRONG_LEVITATION_POTION_TYPE = (PotionType) new PotionType(
            LEVITATION_POTION_NAME,
            new PotionEffect(LEVITATION_POTION_EFFECT, 120, 1)
    ).setRegistryName("strong" + LEVITATION_POTION_NAME);
    private static final PotionType LEVITATION_JUMP_POTION_TYPE = (PotionType) new PotionType(
            LEVITATION_JUMP_POTION_NAME,
            new PotionEffect(LEVITATION_JUMP_POTION_EFFECT, 120, 0)
    ).setRegistryName(LEVITATION_JUMP_POTION_NAME);

    private PotionInit() {}

    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(LEVITATION_POTION_EFFECT);
        event.getRegistry().register(LEVITATION_JUMP_POTION_EFFECT);
    }

    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        event.getRegistry().register(LEVITATION_POTION_TYPE);
        event.getRegistry().register(LONG_LEVITATION_POTION_TYPE);
        event.getRegistry().register(STRONG_LEVITATION_POTION_TYPE);
        event.getRegistry().register(LEVITATION_JUMP_POTION_TYPE);
        registerPotionMixes();
    }

    private static void registerPotionMixes() {
        PotionHelper.addMix(PotionTypes.AWKWARD, ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal, LEVITATION_POTION_TYPE);
        PotionHelper.addMix(LEVITATION_POTION_TYPE, Items.REDSTONE, LONG_LEVITATION_POTION_TYPE);
        PotionHelper.addMix(LEVITATION_POTION_TYPE, ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal, STRONG_LEVITATION_POTION_TYPE);
        PotionHelper.addMix(LONG_LEVITATION_POTION_TYPE, ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal, STRONG_LEVITATION_POTION_TYPE);
        PotionHelper.addMix(STRONG_LEVITATION_POTION_TYPE, Items.COAL, LEVITATION_JUMP_POTION_TYPE);
    }
}
