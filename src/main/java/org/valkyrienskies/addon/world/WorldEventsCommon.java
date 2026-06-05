package org.valkyrienskies.addon.world;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import org.valkyrienskies.addon.world.block.BlockValkyriumOre;
import org.valkyrienskies.addon.world.capability.AntiGravityCapabilityProvider;
import org.valkyrienskies.addon.world.util.LevitationUtil;

public class WorldEventsCommon {
    @SubscribeEvent
    public void onAttachCapabilityEventItem(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();
        if (item instanceof ItemValkyriumCrystal) {
            event.addCapability(
                    new ResourceLocation(ValkyrienSkiesWorld.MOD_ID, "levitation_strength_capability"),
                    new AntiGravityCapabilityProvider(1.0)
            );
        }
        if (stack.getItem() instanceof ItemBlock blockItem) {
            if (blockItem.getBlock() instanceof BlockValkyriumOre) {
                event.addCapability(
                        new ResourceLocation(ValkyrienSkiesWorld.MOD_ID, "levitation_strength_capability"),
                        new AntiGravityCapabilityProvider(1.0)
                );
            }
        }
    }

    @SubscribeEvent
    public void worldTick(WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            LevitationUtil.addEntityLevitationEffects(event.world);
        }
    }
}
