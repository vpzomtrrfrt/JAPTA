package net.reederhome.colin.mods.JAPTA;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class TileEntityMechanicalGenerator extends TileEntityJPT implements IEnergyHandler {

	static final int maxAmount = 20;
	static final int inc = 1;

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return maxAmount;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if(amount>0) {
			transmit();
		}
	}

}