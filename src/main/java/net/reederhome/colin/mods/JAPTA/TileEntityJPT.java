package net.reederhome.colin.mods.JAPTA;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

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
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive,
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
	public int extractEnergy(EnumFacing from, int maxExtract,
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
	public int getEnergyStored(EnumFacing from) {
		return amount;
	}
	
	public void transmit(EnumFacing side) {
		if(amount<=0) {
			return;
		}
		EnumFacing opp = side.getOpposite();
		BlockPos sp = getPos().offset(side);
		TileEntity te = worldObj.getTileEntity(sp);
		if(te!=null) {
			if(te instanceof IEnergyReceiver) {
				IEnergyReceiver h = (IEnergyReceiver) te;
				if(h.canConnectEnergy(side)) {
					int r = h.receiveEnergy(opp, amount, false);
					amount-=r;
				}
			}
		}
	}

	public void transmit(int s) {
		transmit(EnumFacing.VALUES[s]);
	}
	
	public void transmit() {
		for(int s = 0; s < 6; s++) {
			transmit(s);
		}
	}
}