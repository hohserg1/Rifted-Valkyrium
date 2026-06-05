package org.valkyrienskies.addon.world.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.valkyrienskies.addon.world.ValkyrienSkiesWorld;
import org.valkyrienskies.addon.world.config.VSWorldConfig;

import java.util.Random;

/**
 * Created by joeyr on 4/18/2017.
 */
public class ValkyrienSkiesWorldGen implements IWorldGenerator {
    private WorldGenMinable genValkyriumOre;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (VSWorldConfig.valkyriumOreGenEnabled) {
            if (genValkyriumOre == null) {
                genValkyriumOre = new WorldGenMinable(ValkyrienSkiesWorld.INSTANCE.valkyriumOre.getDefaultState(), 8);
            }
            //spawn valkyrium ore on world only
            if (world.provider.getDimension() == 0) {
                runValkyriumGenerator(genValkyriumOre, world, random, chunkX, chunkZ, 2, 0, 25);
            }
        }
    }

    private void runValkyriumGenerator(WorldGenerator generator, World world, Random rand, int chunkX, int chunkZ,
                                       int chancesToSpawn, int minHeight, int maxHeight) {
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight) {
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
        }

        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }
}
