package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;

public class TileEntityChargingPlate extends TileEntity implements IEnergyHandler {

	int amount = 0;
	int maxAmount = 1000;
	
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
	
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)>0) {
			List<EntityPlayer> l = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1));
			Iterator<EntityPlayer> it = l.iterator();
			while(it.hasNext()) {
				EntityPlayer p = it.next();
				for(int i = 0; i < p.inventory.getSizeInventory(); i++) {
					if(amount>0) {
						ItemStack s = p.inventory.getStackInSlot(i);
						if(s!=null && s.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) s.getItem();
							amount-=ei.receiveEnergy(s, amount, false);
						}
					}
					else {
						break;
					}
				}
			}
		}
	}

}