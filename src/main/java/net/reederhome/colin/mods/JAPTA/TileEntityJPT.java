package net.reederhome.colin.mods.JAPTA;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public abstract class TileEntityJPT extends TileEntity implements IEnergyHandler {

	int amount = 0;
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Energy", amount);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		amount = nbt.getInteger("Energy");
	}
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		int tr;
		if(amount+maxReceive<=getMaxEnergyStored(from)) {
			tr=maxReceive;
		}
		else {
			tr=getMaxEnergyStored(from)-amount;
		}
		if(!simulate) {
			amount+=tr;
		}
		return tr;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		int tr;
		if(maxExtract<amount) {
			tr=maxExtract;
		}
		else {
			tr=amount;
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
	
	public void transmit() {
		for(int s = 0; s < 6; s++) {
			if(amount<=0) {
				return;
			}
			ForgeDirection side = ForgeDirection.getOrientation(s);
			ForgeDirection opp = side.getOpposite();
			TileEntity te = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
			if(te!=null) {
				if(te instanceof IEnergyHandler) {
					IEnergyHandler h = (IEnergyHandler) te;
					if(h.canConnectEnergy(side)) {
						int r = h.receiveEnergy(opp, amount, false);
						amount-=r;
					}
				}
			}
		}
	}
}