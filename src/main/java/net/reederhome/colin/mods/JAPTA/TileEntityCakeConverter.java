package net.reederhome.colin.mods.JAPTA;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityCakeConverter extends TileEntityJPT {

	int use = 18000;
	public static final int maxAmount = 100000;
	
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return maxAmount;
	}
	
	public void updateEntity() {
		super.updateEntity();
		int m = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int r = 4;
		if(worldObj.isRemote) return;
		if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) return;
		if(m==0) {
			for(int x = xCoord-r; x <= xCoord+r; x++) {
				for(int y = yCoord-r; y <= yCoord+r; y++) {
					for(int z = zCoord-r; z <= zCoord+r; z++) {
						if(amount+use<=maxAmount) {
							if(worldObj.getBlock(x, y, z).equals(Blocks.cake)) {
								int c = worldObj.getBlockMetadata(x, y, z);
								c++;
								amount+=use;
								if(c>=6) {
									worldObj.setBlockToAir(x, y, z);
								}
								else {
									worldObj.setBlockMetadataWithNotify(x, y, z, c, 2);
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
				int x = xCoord-r;
				int y = yCoord-1;
				int z = zCoord-r;
				while(c>4*r*r) {
					c-=4*r*r;
					y++;
				}
				while(c>2*r) {
					c-=2*r;
					x++;
				}
				z+=c;
				boolean isCake = worldObj.getBlock(x, y, z).equals(Blocks.cake);
				if((worldObj.isAirBlock(x, y, z) || isCake) && Blocks.cake.canBlockStay(worldObj, x, y, z)) {
					int cm = isCake?worldObj.getBlockMetadata(x, y, z):6;
					while(amount>=use && cm>0) {
						cm--;
						amount-=use;
					}
					worldObj.setBlock(x, y, z, Blocks.cake);
					worldObj.setBlockMetadataWithNotify(x, y, z, cm, 2);
				}
			}
		}
	}

}
