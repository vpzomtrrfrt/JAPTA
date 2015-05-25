package net.reederhome.colin.mods.JAPTA;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;

public class TileEntityChestCharger extends TileEntity implements IEnergyHandler {
	
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		int tr = 0;
		for(int s = 0; s < 6; s++) {
			ForgeDirection side = ForgeDirection.getOrientation(s);
			TileEntity te = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
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
				int[] slots = inv.getAccessibleSlotsFromSide(side.getOpposite().ordinal());
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
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		int tr = 0;
		for(int s = 0; s < 6; s++) {
			ForgeDirection side = ForgeDirection.getOrientation(s);
			TileEntity te = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
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
				int[] slots = inv.getAccessibleSlotsFromSide(side.getOpposite().ordinal());
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
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		int tr = 0;
		for(int s = 0; s < 6; s++) {
			ForgeDirection side = ForgeDirection.getOrientation(s);
			TileEntity te = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
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
				int[] slots = inv.getAccessibleSlotsFromSide(side.getOpposite().ordinal());
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