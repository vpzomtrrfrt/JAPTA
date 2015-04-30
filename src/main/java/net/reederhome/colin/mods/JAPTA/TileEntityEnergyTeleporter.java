package net.reederhome.colin.mods.JAPTA;

import java.util.HashMap;

import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityEnergyTeleporter extends TileEntity implements IEnergyHandler {

	static final int maxEnergy = 10;
	
	static HashMap<String,Integer> map = new HashMap<String,Integer>();
	
	String item = "generic";
	String player;
	
	public static int get(String item) {
		if(map.containsKey(item)) {
			return map.get(item);
		}
		else {
			return 0;
		}
	}

	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(player!=null) {
			tag.setString("Player", player);
		}
		if(item!="generic") {
			tag.setString("Item", item);
		}
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(tag.hasKey("Player")) {
			player = tag.getString("Player");
		}
		if(tag.hasKey("Item")) {
			item = tag.getString("Item");
		}
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		int canReceive = maxEnergy-get(item);
		int tr;
		if(maxReceive<canReceive) {
			tr = maxReceive;
		}
		else {
			tr = canReceive;
		}
		if(!simulate) {
			map.put(item, get(item)+tr);
		}
		return tr;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		int tr;
		int amount = get(item);
		if(maxExtract<amount) {
			tr = maxExtract;
		}
		else {
			tr = amount;
		}
		if(!simulate) {
			map.put(item, amount-tr);
		}
		System.out.println("received "+tr);
		return tr;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return get(item);
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return maxEnergy;
	}
	
	public void updateEntity() {
		super.updateEntity();
		int amt = get(item);
		if(amt>0) {
			for(int s = 0; s < 6; s++) {
				ForgeDirection side = ForgeDirection.getOrientation(s);
				ForgeDirection oppo = side.getOpposite();
				TileEntity te = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
				if(te!=null) {
					if(te instanceof IEnergyHandler) {
						IEnergyHandler h = (IEnergyHandler) te;
						if(h.canConnectEnergy(oppo)) {
							int r = h.receiveEnergy(oppo, amt, false);
							amt-=r;
						}
					}
				}
			}
		}
		map.put(item, amt);
	}
}