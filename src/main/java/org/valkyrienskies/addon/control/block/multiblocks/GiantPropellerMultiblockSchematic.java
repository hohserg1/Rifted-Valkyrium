package org.valkyrienskies.addon.control.block.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.valkyrienskies.addon.control.MultiblockRegistry;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;

import java.util.ArrayList;
import java.util.List;

public class GiantPropellerMultiblockSchematic implements IMultiblockSchematic {
    private final List<ImmutablePair<BlockPos, Block>> structureRelativeToCenter;
    private String schematicID;
    private int propellerRadius;
    private EnumFacing propellerFacing;

    public GiantPropellerMultiblockSchematic() {
        this.structureRelativeToCenter = new ArrayList<ImmutablePair<BlockPos, Block>>();
        this.schematicID = MultiblockRegistry.EMPTY_SCHEMATIC_ID;
        this.propellerFacing = EnumFacing.NORTH;
    }

    @Override
    public void initializeMultiblockSchematic(String schematicID) {
        Block enginePart = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.giantPropellerPart;

        Vec3i perpAxisOne = null;
        Vec3i perpAxisTwo = switch (propellerFacing.getAxis()) {
            case X -> {
                perpAxisOne = new Vec3i(0, 1, 0);
                yield new Vec3i(0, 0, 1);
            }
            case Y -> {
                perpAxisOne = new Vec3i(1, 0, 0);
                yield new Vec3i(0, 0, 1);
            }
            case Z -> {
                perpAxisOne = new Vec3i(1, 0, 0);
                yield new Vec3i(0, 1, 0);
            }
        };

        for (int x = -this.propellerRadius; x <= this.propellerRadius; x++) {
            for (int y = -this.propellerRadius; y <= this.propellerRadius; y++) {
                int relativeX = (perpAxisOne.getX() * x) + (perpAxisTwo.getX() * y);
                int relativeY = (perpAxisOne.getY() * x) + (perpAxisTwo.getY() * y);
                int relativeZ = (perpAxisOne.getZ() * x) + (perpAxisTwo.getZ() * y);
                this.structureRelativeToCenter.add(new ImmutablePair<BlockPos, Block>(
                        new BlockPos(relativeX, relativeY, relativeZ),
                        enginePart
                ));
            }
        }
        this.schematicID = schematicID;
    }

    @Override
    public List<ImmutablePair<BlockPos, Block>> getStructureRelativeToCenter() {
        return structureRelativeToCenter;
    }

    @Override
    public String getSchematicPrefix() {
        return "multiblock_giant_propeller";
    }

    @Override
    public String getSchematicID() {
        return this.schematicID;
    }

    @Override
    public void applyMultiblockCreation(World world, BlockPos tilePos, BlockPos relativePos) {
        if (!(world.getTileEntity(tilePos) instanceof TileEntityGiantPropellerPart enginePart)) {
            throw new IllegalStateException();
        }
        enginePart.assembleMultiblock(this, relativePos);
    }

    @Override
    public List<IMultiblockSchematic> generateAllVariants() {
        List<IMultiblockSchematic> variants = new ArrayList<IMultiblockSchematic>();

        for (EnumFacing variantPropellerFacing : EnumFacing.values()) {
            for (int radius = 3; radius >= 1; radius--) {
                GiantPropellerMultiblockSchematic variant = new GiantPropellerMultiblockSchematic();

                variant.propellerRadius = radius;
                variant.propellerFacing = variantPropellerFacing;
                variant.initializeMultiblockSchematic(
                    getSchematicPrefix() + ":facing:" + variantPropellerFacing.toString() + ":radius:" + radius
                );

                variants.add(variant);
            }
        }
        return variants;
    }

    public EnumFacing getPropellerFacing() {
        return propellerFacing;
    }

    public int getPropellerRadius() {
        return propellerRadius;
    }
}
