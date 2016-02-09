package net.reederhome.colin.mods.JAPTA;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityBonemealApplicator extends TileEntityJPT implements ITickable {

	static final int use = 100;
	
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return 1000;
	}

	@Override
	public void update() {
		if(amount>=use && !worldObj.isRemote && worldObj.isBlockIndirectlyGettingPowered(getPos()) > 0) {
			mealing:
			for(int s = 0; s < 6; s++) {
				EnumFacing side = EnumFacing.VALUES[s];
				BlockPos sp = getPos().offset(side);
				TileEntity te = worldObj.getTileEntity(sp);
				if(te instanceof IInventory) {
					IInventory inv = (IInventory)te;
					for(int i = 0; i < inv.getSizeInventory(); i++) {
						ItemStack stack = inv.getStackInSlot(i);
						if(stack!=null) {
							if(stack.getItem().equals(Items.dye) && stack.getItemDamage()==15 && stack.stackSize>0) {
								Random r = new Random();
								BlockPos cp = getPos().add(r.nextInt(8)-4, 4, r.nextInt(8)-4);
								while(cp.getY()>getPos().getY()-4) {
									IBlockState bs = worldObj.getBlockState(cp);
									Block b = bs.getBlock();
									if(b instanceof IGrowable && b != Blocks.grass) {
										IGrowable g = (IGrowable) b;
										if (g.canGrow(worldObj, cp, bs, worldObj.isRemote))
							            {
											if (g.canUseBonemeal(worldObj, worldObj.rand, cp, bs))
											{
							                    g.grow(worldObj, worldObj.rand, cp, bs);
							                    stack.stackSize--;
							                    amount-=use;
							                    break mealing;
							                }
							            }
									}
									cp = cp.down();
								}
							}
						}
					}
				}
			}
		}
	}
}