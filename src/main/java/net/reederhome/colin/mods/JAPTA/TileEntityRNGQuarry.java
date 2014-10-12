package net.reederhome.colin.mods.JAPTA;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.ForgeDirection;


public class TileEntityRNGQuarry extends TileEntity implements cofh.api.energy.IEnergyHandler {

	int amount = 0;
	static final int maxAmount = 2000;
	static final int consume = 200;
	int range = 8;
	ItemStack itm = new ItemStack(Items.diamond_pickaxe);
	
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("Energy", amount);
		NBTTagCompound it = new NBTTagCompound();
		itm.writeToNBT(it);
		nbt.setTag("Item", it);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		amount = nbt.getInteger("Energy");
		itm = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item"));
	}
	
	public void updateEntity() {
		int i = 0;
		while(amount>consume&&i<5) {
			i++;
			int x = xCoord+new Random().nextInt(range*2)-range;
			int y = yCoord-1;
			int z = zCoord+new Random().nextInt(range*2)-range;
			while(worldObj.isAirBlock(x, y, z)&&y>0) {
				y--;
			}
			Block b = worldObj.getBlock(x, y, z);
			int meta = worldObj.getBlockMetadata(x, y, z);
			int hl = itm.getItem().getHarvestLevel(itm, "pickaxe");
			//System.out.println("harvest level: "+hl);
			if(b.getHarvestLevel(meta)<hl) {
				ArrayList<ItemStack> drops = b.getDrops(worldObj, x, y, z, meta, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itm));
				Iterator<ItemStack> it = drops.iterator();
				while(it.hasNext()) {
					ItemStack s = it.next();
					for(int si = 0; si < 6; si++) {
						int cx = xCoord+Facing.offsetsXForSide[si];
						int cy = yCoord+Facing.offsetsYForSide[si];
						int cz = zCoord+Facing.offsetsZForSide[si];
						TileEntity te = worldObj.getTileEntity(cx, cy, cz);
						if(te!=null) {
							if(te instanceof IInventory) {
								IInventory inv = (IInventory)te;
								for(int p = 0; p < inv.getSizeInventory(); p++) {
									if(s==null) {
										break;
									}
									ItemStack stack = inv.getStackInSlot(p);
									if(stack==null||stack.stackSize==0||stack.isItemEqual(s)) {
										ItemStack tr;
										int ss;
										if(stack==null) {
											tr = s.copy();
											ss = 0;
										}
										else {
											tr = stack.copy();
											ss = stack.stackSize;
										}
										if(ss+s.stackSize<=s.getMaxStackSize()) {
											tr.stackSize=ss+s.stackSize;
											it.remove();
											s=null;
										}
										else if(ss<=s.getMaxStackSize()) {
											s.stackSize-=s.getMaxStackSize()-ss;
											tr.stackSize=s.getMaxStackSize();
										}
										else {
											tr=null;
										}
										if(tr!=null) {
											inv.setInventorySlotContents(p, tr);
										}
									}
								}
								if(s==null) break;
							}
						}
					}
					if(s!=null) {
						EntityItem ei = new EntityItem(worldObj, xCoord, yCoord+1, zCoord, s);
						worldObj.spawnEntityInWorld(ei);
					}
				}
				worldObj.setBlockToAir(x, y, z);
			}
			amount-=consume;
		}
	}
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		int tr = 0;
		if(amount+maxReceive>maxAmount) {
			tr = maxAmount-amount;
		}
		else {
			tr = maxReceive;
		}
		if(!simulate) {
			amount+=tr;
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
		return amount;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return maxAmount;
	}

}