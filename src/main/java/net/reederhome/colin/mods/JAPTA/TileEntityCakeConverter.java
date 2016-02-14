package net.reederhome.colin.mods.JAPTA;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileEntityCakeConverter extends TileEntityJPT implements IEnergyProvider, IEnergyReceiver, ITickable {
    public static final int RANGE = 4;
    public static final int BITE_VALUE = 18000;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 100000;
    }

    @Override
    public void update() {
        if(worldObj.isRemote) return;
        IBlockState me = worldObj.getBlockState(getPos());
        try {
            EnumConverterType mode = me.getValue(BlockConverter.MODE);
            dancing:
            if (mode == EnumConverterType.ABSORB) {
                for (int x = -RANGE; x < RANGE; x++) {
                    for (int y = -RANGE; y < RANGE; y++) {
                        for (int z = -RANGE; z < RANGE; z++) {
                            if (stored + BITE_VALUE < getMaxEnergyStored(null)) {
                                BlockPos cp = getPos().add(x, y, z);
                                IBlockState state = worldObj.getBlockState(cp);
                                if (state.getBlock() == Blocks.cake) {
                                    int bites = state.getValue(BlockCake.BITES);
                                    bites++;
                                    stored += BITE_VALUE;
                                    if (bites >= 6) {
                                        worldObj.setBlockToAir(cp);
                                    } else {
                                        worldObj.setBlockState(cp, state.withProperty(BlockCake.BITES, bites));
                                    }
                                    break dancing;
                                }
                            }
                        }
                    }
                }
                if (stored > 0) {
                    transmit();
                }
            } else {
                if (stored >= BITE_VALUE) {
                    for (int x = -RANGE; x < RANGE; x++) {
                        for (int y = -1; y < 1; y++) {
                            for (int z = -RANGE; z < RANGE; z++) {
                                BlockPos cp = getPos().add(x, y, z);
                                IBlockState state = worldObj.getBlockState(cp);
                                if (state.getBlock() == Blocks.cake) {
                                    int bites = state.getValue(BlockCake.BITES);
                                    if (bites > 0) {
                                        stored -= BITE_VALUE;
                                        worldObj.setBlockState(cp, state.withProperty(BlockCake.BITES, bites - 1));
                                        break dancing;
                                    }
                                } else if (state.getBlock().isReplaceable(worldObj, cp)) {
                                    stored -= BITE_VALUE;
                                    worldObj.setBlockState(cp, Blocks.cake.getDefaultState().withProperty(BlockCake.BITES, 5));
                                    break dancing;
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }
}