package org.valkyrienskies.mixin.entity;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.capability.VSCapabilityRegistry;
import org.valkyrienskies.mod.common.capability.entity_ship_draggable.IEntityShipDraggable;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {

    /**
     * This mixin allows players to breathe underwater when they're in an air pocket.
     */
    @Inject(method = "canBreatheUnderwater", at = @At("HEAD"), cancellable = true)
    private void onPreCanBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        EntityLivingBase thisEntity = (EntityLivingBase) ((Object) this);
        IEntityShipDraggable entityShipDraggable = thisEntity.getCapability(VSCapabilityRegistry.VS_ENTITY_SHIP_DRAGGABLE, null);
        if (entityShipDraggable == null || !entityShipDraggable.getInAirPocket()) return;

        cir.setReturnValue(true);
    }
}
