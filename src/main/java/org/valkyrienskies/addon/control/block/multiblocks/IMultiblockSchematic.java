package org.valkyrienskies.addon.control.block.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

public interface IMultiblockSchematic {
    /**
     * This should generate the getStructureRelativeToCenter() list.
     */
    void initializeMultiblockSchematic(String schematicID);

    /**
     * Should return a static immutable list that represents how this multiblock is created.
     */
    List<ImmutablePair<BlockPos, Block>> getStructureRelativeToCenter();

    /**
     * Returns the render bounding box tile entities should use while rendering this schematic.
     */
    default AxisAlignedBB getSchematicRenderBB(BlockPos masterPos) {
        double minX, minY, minZ, maxX, maxY, maxZ;
        minX = minY = minZ = Double.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Double.NEGATIVE_INFINITY;
        for (ImmutablePair<BlockPos, Block> pair : getStructureRelativeToCenter()) {
            double curX = pair.getLeft().getX() + masterPos.getX();
            double curY = pair.getLeft().getY() + masterPos.getY();
            double curZ = pair.getLeft().getZ() + masterPos.getZ();

            minX = Math.min(curX, minX);
            minY = Math.min(curY, minY);
            minZ = Math.min(curZ, minZ);
            maxX = Math.max(curX, maxX);
            maxY = Math.max(curY, maxY);
            maxZ = Math.max(curZ, maxZ);
        }
        return new AxisAlignedBB(minX - .5, minY - .5, minZ - .5, maxX + .5, maxY + .5, maxZ + .5);
    }

    /**
     * Returns a common schematic prefix for all multiblocks of this type.
     */
    String getSchematicPrefix();

    String getSchematicID();

    /**
     * Returns true if the multiblock was successfully created.
     */
    default boolean attemptToCreateMultiblock(World world, BlockPos pos) {
        if (this.getStructureRelativeToCenter().isEmpty()) {
            throw new IllegalStateException("No structure info found in the multiblock schematic!");
        }

        boolean buildSuccessful = true;
        for (ImmutablePair<BlockPos, Block> pair : this.getStructureRelativeToCenter()) {
            BlockPos realPos = pos.add(pair.getLeft());
            IBlockState state = world.getBlockState(realPos);
            // This rotation didn't work
            if (state.getBlock() != pair.getRight()) {
                buildSuccessful = false;
                break;
            }
            else {
                TileEntity tile = world.getTileEntity(realPos);
                if (tile instanceof ITileEntityMultiblockPart<?, ?> multiblockPart) {
                    // If its already a part of a multiblock then do not allow this to assemble.
                    if (multiblockPart.isPartOfAssembledMultiblock()) {
                        buildSuccessful = false;
                        break;
                    }
                }
                else {
                    buildSuccessful = false;
                    break;
                }
            }
        }

        if (buildSuccessful) {
            for (ImmutablePair<BlockPos, Block> pair : getStructureRelativeToCenter()) {
                BlockPos realPos = pos.add(pair.getLeft());
                this.applyMultiblockCreation(world, realPos, pair.getLeft());
            }
            return true;
        }

        return false;
    }

    void applyMultiblockCreation(World world, BlockPos tilePos, BlockPos relativePos);

    /**
     * Should only be called once by initialization code. Doesn't have any non static properties but
     * java doesn't allow static interface methods.
     * <p>
     * The order in which the schematics are in this list will be used as priority order for which
     * schematic variants are tested for first.
     */
    List<IMultiblockSchematic> generateAllVariants();

    default EnumMultiblockRotation getMultiblockRotation() {
        return EnumMultiblockRotation.NONE;
    }
}
