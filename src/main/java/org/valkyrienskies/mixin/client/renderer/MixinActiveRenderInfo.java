package org.valkyrienskies.mixin.client.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.capability.VSCapabilityRegistry;
import org.valkyrienskies.mod.common.capability.entity_ship_draggable.IEntityShipDraggable;

@Mixin(ActiveRenderInfo.class)
public class MixinActiveRenderInfo {

    @Inject(method = "getBlockStateAtEntityViewpoint", at = @At("HEAD"), cancellable = true)
    private static void onGetBlockStateAtEntityViewpoint(World worldIn, Entity entityIn, float p_186703_2_, CallbackInfoReturnable<IBlockState> cir) {
        IEntityShipDraggable entityShipDraggable = entityIn.getCapability(VSCapabilityRegistry.VS_ENTITY_SHIP_DRAGGABLE, null);
        if (entityShipDraggable == null || !entityShipDraggable.getInAirPocket()) return;

        cir.setReturnValue(Blocks.AIR.getDefaultState());
    }
}
