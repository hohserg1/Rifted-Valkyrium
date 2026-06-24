package org.valkyrienskies.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.entity.EntityMountable;

@Mixin(EntityLeashKnot.class)
public class MixinEntityLeashKnot {

    @Inject(method = "getKnotForPosition", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void getKnotForPosition(World worldIn, BlockPos pos, CallbackInfoReturnable<EntityLeashKnot> cir) {
        for (EntityMountable mountable : worldIn.getEntitiesWithinAABB(EntityMountable.class, new AxisAlignedBB(pos).grow(1))) {
            for (Entity passenger : mountable.getPassengers()) {
                if (passenger instanceof EntityLeashKnot knot) {
                    cir.setReturnValue(knot);
                }
            }
        }
    }
}
