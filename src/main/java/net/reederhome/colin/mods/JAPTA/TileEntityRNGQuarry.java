package net.reederhome.colin.mods.JAPTA;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;


public class TileEntityRNGQuarry extends TileEntity implements cofh.api.energy.IEnergyHandler {

	int amount = 0;
	static final int maxAmount = 2000;
	static final int consume = 500;
	int range = 8;
	ItemStack itm = null;
	
	DamageSource quarryDamage = new DamageSource("japta.quarry");
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Energy", amount);
		if(itm!=null) {
			NBTTagCompound it = new NBTTagCompound();
			itm.writeToNBT(it);
			nbt.setTag("Item", it);
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		amount = nbt.getInteger("Energy");
		if(nbt.hasKey("Item")) {
			itm = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item"));
		}
	}
	
	public void updateEntity() {
		int i = 0;
		while(amount>=consume&&i<1) {
			i++;
			BlockPos cp = getPos();
			cp = cp.add(new Random().nextInt(range*2)-range, -1, new Random().nextInt(range*2)-range);
			while((worldObj.isAirBlock(cp)||worldObj.getBlockState(cp).getBlock().getMaterial().isLiquid())&&cp.getY()>0) {
				cp = cp.down();
			}
			if(cp.getY()==0) continue;
			List<EntityLivingBase> ents = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.fromBounds(cp.getX(), cp.getY(), cp.getZ(), cp.getX()+1, getPos().getY(), cp.getZ()+1));
			if(ents.size()>0) {
				EntityLivingBase ent = ents.get(0);
				ent.attackEntityFrom(quarryDamage, 10);
				amount-=consume/5;
			}
			else {
				IBlockState bs = worldObj.getBlockState(cp);
				Block b = bs.getBlock();
				int hl = 0;
				if(itm!=null) {
					hl = itm.getItem().getHarvestLevel(itm, b.getHarvestTool(bs));
					if(hl==-1) hl=0;
				}
				//System.out.println("harvest level: "+hl);
				int bhl = b.getHarvestLevel(bs);
				if(bhl<=hl&&b.getBlockHardness(worldObj, cp)!=-1) {
					List<ItemStack> drops = b.getDrops(worldObj, cp, bs, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itm));
					Iterator<ItemStack> it = drops.iterator();
					while(it.hasNext()) {
						ItemStack s = it.next();
						for(int si = 0; si < 6; si++) {
							BlockPos sp = getPos().offset(EnumFacing.VALUES[si]);
							TileEntity te = worldObj.getTileEntity(sp);
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
							BlockPos me = getPos();
							EntityItem ei = new EntityItem(worldObj, me.getX(), me.getY()+1, me.getZ(), s);
							worldObj.spawnEntityInWorld(ei);
						}
					}
					worldObj.setBlockToAir(cp);
					amount-=consume;
					if(Math.random()<0.9&&itm!=null&&itm.isItemStackDamageable()&&bhl>0) {
						itm.setItemDamage(itm.getItemDamage()+1);
						if(itm.getItemDamage()>itm.getMaxDamage()) {
							itm=null;
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive,
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
	public int extractEnergy(EnumFacing from, int maxExtract,
			boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return amount;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return maxAmount;
	}

}