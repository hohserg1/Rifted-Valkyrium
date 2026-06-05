package org.valkyrienskies.addon.world.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.valkyrienskies.addon.world.ItemValkyriumCrystal;
import org.valkyrienskies.addon.world.PotionInit;
import org.valkyrienskies.addon.world.ValkyrienSkiesWorld;
import org.valkyrienskies.addon.world.block.BlockValkyriumOre;
import org.valkyrienskies.addon.world.capability.ICapabilityAntiGravity;
import org.valkyrienskies.addon.world.config.VSWorldConfig;

import java.util.Arrays;
import java.util.List;

public final class LevitationUtil {
    private LevitationUtil() {}

    /**
     * Adds levitation effects to all entities in world.
     */
    public static void addEntityLevitationEffects(World world) {
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityItem entityItem) {
                ItemStack itemStack = entityItem.getItem();
                ICapabilityAntiGravity capability = itemStack.getCapability(ValkyrienSkiesWorld.ANTI_GRAVITY_CAPABILITY, null);
                if (capability != null) {
                    entity.addVelocity(0.0D, 0.08D * capability.getMultiplier(), 0.0D);
                }
            }
            else if (entity instanceof EntityLivingBase living) {
                if (living instanceof EntityPlayer player) {
                    if (VSWorldConfig.valkyriumItemsLiftPlayers && !player.isCreative()) {
                        double addedUpVelocity = 0.0D;
                        List<List<ItemStack>> inventories = Arrays.asList(
                                player.inventory.mainInventory,
                                player.inventory.armorInventory,
                                player.inventory.offHandInventory
                        );
                        for (List<ItemStack> stackArray : inventories) {
                            for (ItemStack stack : stackArray) {
                                if (stack != null) {
                                    if (stack.getItem() instanceof ItemBlock) {
                                        ItemBlock blockItem = (ItemBlock) stack.getItem();
                                        if (blockItem.getBlock() instanceof BlockValkyriumOre) {
                                            addedUpVelocity += 0.00025D * stack.getCount() * VSWorldConfig.valkyriumOreForce;
                                        }
                                    } else if (stack.getItem() instanceof ItemValkyriumCrystal) {
                                        addedUpVelocity += 0.00025D * stack.getCount() * VSWorldConfig.valkyriumCrystalForce;
                                    }
                                }
                            }
                        }
                        if (addedUpVelocity > 0.0D) {
                            entity.addVelocity(0.0D, addedUpVelocity, 0.0D);
                            if (addedUpVelocity > 0.05D) {
                                entity.fallDistance = 0.0F;
                            }
                        }
                    }
                }

                if (living.isPotionActive(PotionInit.LEVITATION_POTION_EFFECT)) {
                    PotionEffect levitationEffect = living.getActivePotionEffect(PotionInit.LEVITATION_POTION_EFFECT);
                    if (living instanceof EntityPlayer && (((EntityPlayer) living).isCreative() || ((EntityPlayer) living).isSpectator())) {
                        break;
                    }
                    if (levitationEffect != null) {
                        switch (levitationEffect.getAmplifier()) {
                            case 0:
                                entity.addVelocity(0.0D, 0.07D, 0.0D);
                                break;
                            case 1:
                                entity.addVelocity(0.0D, 0.1D, 0.0D);
                                break;
                            default:
                                break;
                        }
                    }
                    entity.fallDistance = 0.0F;
                }

                if (living.isPotionActive(PotionInit.LEVITATION_JUMP_POTION_EFFECT)) {
                    PotionEffect levitationEffect = living.getActivePotionEffect(PotionInit.LEVITATION_JUMP_POTION_EFFECT);
                    if (living instanceof EntityPlayer && (((EntityPlayer) living).isCreative() || ((EntityPlayer) living).isSpectator())) {
                        break;
                    }
                    if (levitationEffect != null && levitationEffect.getDuration() > 120 * 0.9D) {
                        entity.addVelocity(0.0D, 0.2D, 0.0D);
                    }
                    else {
                        entity.addVelocity(0.0D, 0.05D, 0.0D);
                    }
                    entity.fallDistance = 0.0F;
                }
            }
        }
    }
}
