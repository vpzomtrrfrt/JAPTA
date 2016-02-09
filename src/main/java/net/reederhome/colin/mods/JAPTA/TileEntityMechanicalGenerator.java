package net.reederhome.colin.mods.JAPTA;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.util.ITickable;

public class TileEntityMechanicalGenerator extends TileEntityJPT implements IEnergyHandler, ITickable {

	static final int maxAmount = 20;
	static final int inc = 1;

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive,
			boolean simulate) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return maxAmount;
	}
	
	public void update() {
		if(amount>0) {
			transmit();
		}
	}

}