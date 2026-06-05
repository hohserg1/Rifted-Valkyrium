package org.valkyrienskies.addon.control.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.valkyrienskies.addon.control.item.ItemBaseWire;
import org.valkyrienskies.addon.control.tileentity.ITileEntityControlNode;

public abstract class BlockNodeControlBasic extends BlockNodeComponentBasic {
    public BlockNodeControlBasic(String name, Material mat, float hardness) {
        super(name, mat, 0.0F, true);
        this.setHardness(hardness);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        //no need to awkwardly sneak when attaching a node to a node controller
        if (playerIn.getHeldItem(hand).getItem() instanceof ItemBaseWire) return false;

        //manage connections when dealing with shears
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (heldItem.getItem() instanceof ItemShears) {
            this.handleWireRemoval(worldIn, pos);
            return true;
        }

        //set controller of control node
        TileEntity tileIn = worldIn.getTileEntity(pos);
        if (tileIn instanceof ITileEntityControlNode controlNode) {
            controlNode.setUserEntity(playerIn);
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.handleWireRemoval(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }
}
