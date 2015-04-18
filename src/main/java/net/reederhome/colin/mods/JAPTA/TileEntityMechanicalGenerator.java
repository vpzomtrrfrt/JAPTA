package net.reederhome.colin.mods.JAPTA;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class TileEntityMechanicalGenerator extends TileEntity implements IEnergyHandler {

	int amount = 0;
	static final int maxAmount = 100;
	static final int inc = 2;
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Energy", amount);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("Energy")) {
			amount = nbt.getInteger("Energy");
		}
	}
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		int tr;
		if(maxExtract<amount) {
			tr = maxExtract;
		}
		else {
			tr = amount;
		}
		if(!simulate) {
			amount-=tr;
		}
		return tr;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return amount;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return maxAmount;
	}

	public void transmit() {
		for(int s = 0; s < 6; s++) {
			if(amount<=0) {
				return;
			}
			ForgeDirection side = ForgeDirection.getOrientation(s);
			TileEntity te = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
			if(te!=null) {
				if(te instanceof IEnergyHandler) {
					IEnergyHandler h = (IEnergyHandler) te;
					if(h.canConnectEnergy(side)) {
						int r = h.receiveEnergy(side, amount, false);
						amount-=r;
					}
				}
			}
		}
	}
	
	public void updateEntity() {
		super.updateEntity();
		if(amount>0) {
			transmit();
		}
	}

}