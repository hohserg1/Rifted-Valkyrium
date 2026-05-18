package org.valkyrienskies.addon.control.block.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.valkyrienskies.addon.control.MultiblockRegistry;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;

import java.util.ArrayList;
import java.util.List;

public class RudderAxleMultiblockSchematic implements IMultiblockSchematic {
    public static final int MIN_AXLE_LENGTH = 2;
    public static final int MAX_AXLE_LENGTH = 6;
    private final List<ImmutablePair<BlockPos, Block>> structureRelativeToCenter;
    private String schematicID;
    private int axleLength;
    private EnumFacing axleAxis;
    private EnumFacing axleFacing;

    public RudderAxleMultiblockSchematic() {
        this.structureRelativeToCenter = new ArrayList<>();
        this.schematicID = MultiblockRegistry.EMPTY_SCHEMATIC_ID;
        this.axleLength = -1;
        this.axleAxis = EnumFacing.UP;
        this.axleFacing = EnumFacing.UP;
    }

    @Override
    public void initializeMultiblockSchematic(String schematicID) {
        this.schematicID = schematicID;
    }

    @Override
    public List<ImmutablePair<BlockPos, Block>> getStructureRelativeToCenter() {
        return this.structureRelativeToCenter;
    }

    @Override
    public String getSchematicPrefix() {
        return "multiblock_rudder_axle";
    }

    @Override
    public String getSchematicID() {
        return this.schematicID;
    }

    @Override
    public void applyMultiblockCreation(World world, BlockPos tilePos, BlockPos relativePos) {
        TileEntity tileEntity = world.getTileEntity(tilePos);
        if (!(tileEntity instanceof TileEntityRudderPart tileRudderPart)) {
            throw new IllegalStateException();
        }
        tileRudderPart.assembleMultiblock(this, relativePos);
    }

    @Override
    public List<IMultiblockSchematic> generateAllVariants() {
        Block rudderAxleBlock = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.rudderPart;
        // Order matters here
        List<IMultiblockSchematic> variants = new ArrayList<IMultiblockSchematic>();
        for (int length = MAX_AXLE_LENGTH; length >= MIN_AXLE_LENGTH; length--) {
            for (EnumFacing possibleAxleAxisDirection : EnumFacing.VALUES) {
                for (EnumFacing possibleAxleFacingDirection : EnumFacing.VALUES) {
                    if (possibleAxleAxisDirection.getAxis() != possibleAxleFacingDirection.getAxis()) {
                        RudderAxleMultiblockSchematic schematicVariant = new RudderAxleMultiblockSchematic();
                        schematicVariant.initializeMultiblockSchematic(
                            getSchematicPrefix() + "axle_axis_direction:"
                                + possibleAxleAxisDirection.toString() + ":axle_facing:"
                                + possibleAxleFacingDirection.toString() + ":length:" + length
                        );
                        schematicVariant.axleAxis = possibleAxleAxisDirection;
                        schematicVariant.axleFacing = possibleAxleFacingDirection;
                        schematicVariant.axleLength = length;
                        for (int i = 0; i < length; i++) {
                            schematicVariant.structureRelativeToCenter
                                .add(new ImmutablePair<BlockPos, Block>(
                                    BlockPos.ORIGIN.offset(possibleAxleAxisDirection, i),
                                    rudderAxleBlock
                                ));
                        }
                        variants.add(schematicVariant);
                    }
                }
            }
        }
        return variants;
    }

    public int getAxleLength() {
        return this.axleLength;
    }

    public EnumFacing getAxleAxisDirection() {
        return this.axleAxis;
    }

    public EnumFacing getAxleFacingDirection() {
        return this.axleFacing;
    }

}
