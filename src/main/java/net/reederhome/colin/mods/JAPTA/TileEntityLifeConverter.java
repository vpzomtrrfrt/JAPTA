package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class TileEntityLifeConverter extends TileEntity implements IEnergyHandler {
	
	int amount = 0;
	static final int maxAmount = 100;
	static final int inc = 4;
	static final float th = 1;
	
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)>0) {
			List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord-0.5, yCoord, zCoord-0.5, xCoord+1.5, yCoord+2.5, zCoord+1.5));
			Iterator<EntityLivingBase> it = l.iterator();
			while(it.hasNext()&&amount>th*inc/2) {
				EntityLivingBase e = it.next();
				if(e.getHealth()<e.getMaxHealth()) {
					e.heal(th);
					amount-=th*inc/2;
				}
			}
		}
	}
	
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
		if(amount+maxReceive<=maxAmount) {
			tr=maxReceive;
		}
		else {
			tr=maxAmount-amount;
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

}