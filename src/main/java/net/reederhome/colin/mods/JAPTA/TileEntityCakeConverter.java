package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityCakeConverter extends TileEntityJPT implements ITickable {

	int use = 18000;
	public static final int maxAmount = 100000;
	
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return maxAmount;
	}
	
	public void update() {
		BlockPos me = getPos();
		IBlockState bs = worldObj.getBlockState(me);
		int r = 4;
		if(worldObj.isRemote) return;
		if(worldObj.isBlockIndirectlyGettingPowered(me) > 0) return;
		if(bs.getValue(BlockCakeConverter.MODE)) {
			for(int x = me.getX()-r; x <= me.getX()+r; x++) {
				for(int y = me.getY()-r; y <= me.getY()+r; y++) {
					for(int z = me.getZ()-r; z <= me.getZ()+r; z++) {
						BlockPos cp = new BlockPos(x, y, z);
						if(amount+use<=maxAmount) {
							IBlockState cbs = worldObj.getBlockState(cp);
							if(cbs.getBlock().equals(Blocks.cake)) {
								int c = cbs.getValue(BlockCake.BITES);
								c++;
								amount+=use;
								if(c>=6) {
									worldObj.setBlockToAir(cp);
								}
								else {
									worldObj.setBlockState(cp, cbs.withProperty(BlockCake.BITES, c));
								}
							}
						}
					}
				}
			}
			if(amount>0) {
				transmit();
			}
		}
		else {
			for(int p = 0; p <= 3*4*r*r; p++) {
				if(amount<use) {
					break;
				}
				int c = p;
				BlockPos cp = me.add(-r, -1, -r);
				while(c>4*r*r) {
					c-=4*r*r;
					cp = cp.up();
				}
				while(c>2*r) {
					c-=2*r;
					cp = cp.offset(EnumFacing.NORTH);
				}
				cp = cp.offset(EnumFacing.EAST);
				IBlockState cbs = worldObj.getBlockState(cp);
				boolean isCake = cbs.getBlock().equals(Blocks.cake);
				if((worldObj.isAirBlock(cp) || isCake) && worldObj.getBlockState(cp.down()).getBlock().isSideSolid(worldObj, cp.down(), EnumFacing.UP)) {
					int cm = isCake?cbs.getValue(BlockCake.BITES):6;
					while(amount>=use && cm>0) {
						cm--;
						amount-=use;
					}
					worldObj.setBlockState(cp, Blocks.cake.getDefaultState().withProperty(BlockCake.BITES, cm));
				}
			}
		}
	}

}
