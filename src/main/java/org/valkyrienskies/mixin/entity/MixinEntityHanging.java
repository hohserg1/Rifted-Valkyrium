package org.valkyrienskies.mixin.entity;

import net.minecraft.entity.EntityHanging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.mod.common.entity.EntityMountable;

@Mixin(EntityHanging.class)
public class MixinEntityHanging {

    @Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityHanging;onValidSurface()Z"))
    public boolean preventDropoutOnShip(EntityHanging self) {
        if (self.getRidingEntity() instanceof EntityMountable mountable) {
            if (mountable.getMountedShip().isPresent()) {
                Vec3d realPos = self.getPositionVector();
                Vec3d onShipPos = mountable.getMountPos();
                self.setPosition(onShipPos.x, onShipPos.y, onShipPos.z);
                boolean result = self.onValidSurface();
                self.setPosition(realPos.x, realPos.y, realPos.z);
                return result;
            }
        }
        return self.onValidSurface();
    }
}
