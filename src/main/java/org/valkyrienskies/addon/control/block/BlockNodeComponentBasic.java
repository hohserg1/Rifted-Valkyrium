package org.valkyrienskies.addon.control.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.valkyrienskies.addon.control.nodenetwork.EnumWireType;
import org.valkyrienskies.addon.control.nodenetwork.IVSNode;
import org.valkyrienskies.addon.control.nodenetwork.IVSNodeProvider;
import org.valkyrienskies.mod.common.util.BaseBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BlockNodeComponentBasic extends BaseBlock implements ITileEntityProvider {
    public BlockNodeComponentBasic(String name, Material mat, float light, boolean creativeTab) {
        super(name, mat, light, creativeTab);
    }

    public void handleWireRemoval(World worldIn, BlockPos pos) {
        //check relay first
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof IVSNodeProvider vsNodeProvider)) return;
        IVSNode node = vsNodeProvider.getNode();
        if (node == null || !node.isValid()) return;

        //check if theres available connections. otherwise, no shearing
        if (node.getLinkedNodesAndWireTypes().isEmpty()) return;

        //now manage wire removal
        List<ImmutablePair<IVSNode, ItemStack>> nodeItemPairList = new ArrayList<>();
        for (Map.Entry<BlockPos, EnumWireType> nodeWireEntry : node.getLinkedNodesAndWireTypes().entrySet()) {
            //---remove connection---
            //check block first
            TileEntity otherTileEntity = worldIn.getTileEntity(nodeWireEntry.getKey());
            if (!(otherTileEntity instanceof IVSNodeProvider otherVsNodeProvider)) continue;
            IVSNode otherNode = otherVsNodeProvider.getNode();
            if (otherNode == null || !otherNode.isValid()) continue;

            //create itemstack
            ItemStack stackToAdd = new ItemStack(nodeWireEntry.getValue().toItem());

            //create pair and add to list
            ImmutablePair<IVSNode, ItemStack> pairToAdd = new ImmutablePair<>(otherNode, stackToAdd);
            nodeItemPairList.add(pairToAdd);
        }

        //break connections and drop item in midpoint
        for (ImmutablePair<IVSNode, ItemStack> nodeItemPair : nodeItemPairList) {
            //break node connection
            node.breakConnection(nodeItemPair.getLeft());

            //drop item at midpoint of pair, but above by one block
            BlockPos otherNodePos = nodeItemPair.getLeft().getNodePos();
            BlockPos midpoint = new BlockPos(
                    (otherNodePos.getX() + node.getNodePos().getX()) / 2D,
                    (otherNodePos.getY() + node.getNodePos().getY()) / 2D,
                    (otherNodePos.getZ() + node.getNodePos().getZ()) / 2D
            ).up();

            //drop item at midpoint
            EntityItem entityItem = new EntityItem(worldIn);
            entityItem.setItem(nodeItemPair.getRight());
            entityItem.setPosition(midpoint.getX() + 0.5D, midpoint.getY() + 0.5D, midpoint.getZ() + 0.5D);
            worldIn.spawnEntity(entityItem);
        }
    }
}
