package org.valkyrienskies.addon.control.block.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.valkyrienskies.addon.control.MultiblockRegistry;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;

import java.util.ArrayList;
import java.util.List;

public class ValkyriumCompressorMultiblockSchematic implements IMultiblockSchematic {

    private final List<ImmutablePair<BlockPos, Block>> structureRelativeToCenter;
    private String schematicID;
    private EnumMultiblockRotation multiblockRotation;

    public ValkyriumCompressorMultiblockSchematic() {
        this.structureRelativeToCenter = new ArrayList<>();
        this.schematicID = MultiblockRegistry.EMPTY_SCHEMATIC_ID;
    }

    @Override
    public void initializeMultiblockSchematic(String schematicID) {
        Block enginePart = ValkyrienSkiesControl.INSTANCE.vsControlBlocks.valkyriumCompressorPart;
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = 0; z <= 1; z++) {
                    this.structureRelativeToCenter.add(new ImmutablePair<>(new BlockPos(x, y, z), enginePart));
                }
            }
        }
        this.schematicID = schematicID;
    }

    @Override
    public List<ImmutablePair<BlockPos, Block>> getStructureRelativeToCenter() {
        return this.structureRelativeToCenter;
    }

    @Override
    public String getSchematicID() {
        return this.schematicID;
    }

    @Override
    public void applyMultiblockCreation(World world, BlockPos tilePos, BlockPos relativePos) {
        if (!(world.getTileEntity(tilePos) instanceof TileEntityValkyriumCompressorPart tileEnginePart)) {
            throw new IllegalStateException();
        }
        tileEnginePart.assembleMultiblock(this, relativePos);
    }

    @Override
    public String getSchematicPrefix() {
        return "multiblock_valkyrium_compressor";
    }

    @Override
    public List<IMultiblockSchematic> generateAllVariants() {
        List<IMultiblockSchematic> varients = new ArrayList<IMultiblockSchematic>();

        for (EnumMultiblockRotation potentialRotation : EnumMultiblockRotation.values()) {
            ValkyriumCompressorMultiblockSchematic varient = new ValkyriumCompressorMultiblockSchematic();

            varient.initializeMultiblockSchematic(
                getSchematicPrefix() + ":rot:" + potentialRotation.toString()
            );

            List<ImmutablePair<BlockPos, Block>> rotatedPairs = new ArrayList<>();
            for (ImmutablePair<BlockPos, Block> unrotatedPairs : varient.structureRelativeToCenter) {
                BlockPos rotatedPos = potentialRotation.rotatePos(unrotatedPairs.getLeft());
                rotatedPairs.add(new ImmutablePair<>(rotatedPos, unrotatedPairs.getRight()));
            }
            varient.structureRelativeToCenter.clear();
            varient.structureRelativeToCenter.addAll(rotatedPairs);
            varient.multiblockRotation = potentialRotation;
            varients.add(varient);
        }
        return varients;
    }

    @Override
    public EnumMultiblockRotation getMultiblockRotation() {
        return this.multiblockRotation;
    }

}
