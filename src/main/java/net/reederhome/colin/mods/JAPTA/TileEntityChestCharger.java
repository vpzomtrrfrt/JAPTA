package net.reederhome.colin.mods.JAPTA;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;

public class TileEntityChestCharger extends TileEntity implements IEnergyHandler {
	
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		int tr = 0;
		for(int s = 0; s < 6; s++) {
			EnumFacing side = EnumFacing.VALUES[s];
			TileEntity te = worldObj.getTileEntity(getPos().offset(side));
			if(te instanceof IInventory) {
				IInventory inv = (IInventory) te;
				for(int i = 0; i < inv.getSizeInventory(); i++) {
					ItemStack st = inv.getStackInSlot(i);
					if(st!=null) {
						if(st.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) st.getItem();
							tr += ei.getMaxEnergyStored(st);
						}
					}
				}
			}
			if(te instanceof ISidedInventory) {
				ISidedInventory inv = (ISidedInventory) te;
				int[] slots = inv.getSlotsForFace(side.getOpposite());
				for(int si = 0; si < slots.length; si++) {
					int i = slots[si];
					ItemStack st = inv.getStackInSlot(i);
					if(st!=null) {
						if(st.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) st.getItem();
							tr += ei.getMaxEnergyStored(st);
						}
					}
				}
			}
		}
		return tr;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive,
			boolean simulate) {
		int tr = 0;
		for(int s = 0; s < 6; s++) {
			EnumFacing side = EnumFacing.VALUES[s];
			TileEntity te = worldObj.getTileEntity(getPos().offset(side));
			if(te instanceof IInventory) {
				IInventory inv = (IInventory) te;
				for(int i = 0; i < inv.getSizeInventory(); i++) {
					ItemStack st = inv.getStackInSlot(i);
					if(st!=null) {
						if(st.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) st.getItem();
							tr += ei.receiveEnergy(st, maxReceive-tr, simulate);
						}
					}
				}
			}
			if(te instanceof ISidedInventory) {
				ISidedInventory inv = (ISidedInventory) te;
				int[] slots = inv.getSlotsForFace(side.getOpposite());
				for(int si = 0; si < slots.length; si++) {
					int i = slots[si];
					ItemStack st = inv.getStackInSlot(i);
					if(st!=null) {
						if(st.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) st.getItem();
							tr += ei.receiveEnergy(st, maxReceive-tr, simulate);
						}
					}
				}
			}
		}
		return tr;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract,
			boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		int tr = 0;
		for(int s = 0; s < 6; s++) {
			EnumFacing side = EnumFacing.VALUES[s];
			TileEntity te = worldObj.getTileEntity(getPos().offset(side));
			if(te instanceof IInventory) {
				IInventory inv = (IInventory) te;
				for(int i = 0; i < inv.getSizeInventory(); i++) {
					ItemStack st = inv.getStackInSlot(i);
					if(st!=null) {
						if(st.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) st.getItem();
							tr += ei.getEnergyStored(st);
						}
					}
				}
			}
			if(te instanceof ISidedInventory) {
				ISidedInventory inv = (ISidedInventory) te;
				int[] slots = inv.getSlotsForFace(side.getOpposite());
				for(int si = 0; si < slots.length; si++) {
					int i = slots[si];
					ItemStack st = inv.getStackInSlot(i);
					if(st!=null) {
						if(st.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) st.getItem();
							tr += ei.getEnergyStored(st);
						}
					}
				}
			}
		}
		return tr;
	}

}