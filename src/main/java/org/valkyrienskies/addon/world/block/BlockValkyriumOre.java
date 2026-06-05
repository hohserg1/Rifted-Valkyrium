package org.valkyrienskies.addon.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.valkyrienskies.addon.world.EntityFallingUpBlock;
import org.valkyrienskies.addon.world.ValkyrienSkiesWorld;
import org.valkyrienskies.mod.common.util.BaseBlock;

import java.util.List;
import java.util.Random;

public class BlockValkyriumOre extends BaseBlock {
    public BlockValkyriumOre() {
        super("valkyrium_ore", Material.ROCK, 3f / 15f, true);
        setHardness(3f);
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> itemInformation, ITooltipFlag advanced) {
        itemInformation.add(TextFormatting.ITALIC.toString() + TextFormatting.BLUE + TextFormatting.ITALIC
                + I18n.format("tooltip.vs_world.valkyrium_ore_1"));
        itemInformation.add(TextFormatting.ITALIC.toString() + TextFormatting.BLUE + TextFormatting.ITALIC
                + I18n.format("tooltip.vs_world.valkyrium_ore_2"));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRemote) return;
        tryFallingUp(worldIn, pos);
    }

    private void tryFallingUp(World worldIn, BlockPos pos) {
        BlockPos downPos = pos.up();
        if ((worldIn.isAirBlock(downPos) || canFallThrough(worldIn.getBlockState(downPos))) && pos.getY() >= 0) {
            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
                if (!worldIn.isRemote) {
                    EntityFallingUpBlock entityfallingblock = new EntityFallingUpBlock(
                            worldIn,
                            pos.getX() + 0.5D,
                            pos.getY(),
                            pos.getZ() + 0.5D,
                            worldIn.getBlockState(pos)
                    );
                    worldIn.spawnEntity(entityfallingblock);
                }
            }
            else {
                IBlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockToAir(pos);
                BlockPos blockpos = pos.up();
                while ((worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() < 255) {
                    blockpos = blockpos.up();
                }
                if (blockpos.getY() < 255) {
                    worldIn.setBlockState(blockpos.down(), state, 3);
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ValkyrienSkiesWorld.INSTANCE.valkyriumCrystal;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return quantityDropped(random) + random.nextInt(fortune + 1);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 4 + random.nextInt(4);
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        if (getItemDropped(state, RANDOM, fortune) != Item.getItemFromBlock(this)) {
            return 16 + RANDOM.nextInt(10);
        }
        return 0;
    }

    // Ripped from BlockFalling class for consistency with game mechanics
    public static boolean canFallThrough(IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }
}
