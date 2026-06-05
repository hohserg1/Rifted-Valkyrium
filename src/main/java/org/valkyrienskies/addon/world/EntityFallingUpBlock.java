package org.valkyrienskies.addon.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Like a regular falling block, but it falls up.
 *
 * @author thebest108
 */
public class EntityFallingUpBlock extends EntityFallingBlock {
    public EntityFallingUpBlock(World worldIn) {
        super(worldIn);
        fallTile = ValkyrienSkiesWorld.INSTANCE.valkyriumOre.getDefaultState();
    }

    public EntityFallingUpBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
        super(worldIn, x, y, z, fallingBlockState);
    }

    @Override
    public void onUpdate() {
        Block block = fallTile.getBlock();
        if (fallTile.getMaterial() == Material.AIR) {
            setDead();
        } else {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            if (fallTime++ == 0) {
                BlockPos blockpos = new BlockPos(this);
                if (world.getBlockState(blockpos).getBlock() == block) {
                    world.setBlockToAir(blockpos);
                } else if (!world.isRemote) {
                    setDead();
                    return;
                }
            }
            if (!hasNoGravity()) {
                motionY += 0.03999999910593033;
            }
            move(MoverType.SELF, motionX, motionY, motionZ);
            motionX *= 0.9800000190734863;
            motionY *= 0.9800000190734863;
            motionZ *= 0.9800000190734863;
            if (!world.isRemote) {
                BlockPos blockpos1 = new BlockPos(this);
                if (!onGround && collidedVertically) {
                    IBlockState iblockstate = world.getBlockState(blockpos1);
                    if (world.isAirBlock(new BlockPos(posX, posY + 1.009999999776482582, posZ))) {
                        if (BlockFalling.canFallThrough(world.getBlockState(new BlockPos(posX, posY + 1.009999999776482582, posZ)))) {
                            collidedVertically = false;
                            return;
                        }
                    }
                    motionX *= 0.699999988079071;
                    motionZ *= 0.699999988079071;
                    motionY *= -0.5;
                    if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION) {
                        setDead();
                        if (!dontSetBlock) {
                            if (world.mayPlace(block, blockpos1, true, EnumFacing.UP, null)
                                    && !BlockFalling.canFallThrough(world.getBlockState(blockpos1.up()))
                                    && world.setBlockState(blockpos1, fallTile, 3)) {
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling) block).onEndFalling(world, blockpos1, null, null);
                                }
                                if (tileEntityData != null && block instanceof ITileEntityProvider) {
                                    TileEntity tileentity = world.getTileEntity(blockpos1);
                                    if (tileentity != null) {
                                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                                        for (String s : tileEntityData.getKeySet()) {
                                            NBTBase nbtbase = tileEntityData.getTag(s);
                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                nbttagcompound.setTag(s, nbtbase.copy());
                                            }
                                        }
                                        tileentity.readFromNBT(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            } else if (shouldDropItem && world.getGameRules().getBoolean("doEntityDrops")) {
                                entityDropItem(new ItemStack(block, 1, block.damageDropped(fallTile)), 0.0F);
                            }
                        }
                    }
                } else if (fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || fallTime > 600) {
                    if (shouldDropItem && world.getGameRules().getBoolean("doEntityDrops")) {
                        entityDropItem(new ItemStack(block, 1, block.damageDropped(fallTile)), 0.0F);
                    }
                    setDead();
                }
            }
        }
    }
}
