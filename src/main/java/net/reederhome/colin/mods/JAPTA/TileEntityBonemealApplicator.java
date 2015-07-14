package net.reederhome.colin.mods.JAPTA;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityBonemealApplicator extends TileEntityJPT {

	static final int use = 100;
	
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 1000;
	}

	public void updateEntity() {
		if(amount>=use && !worldObj.isRemote) {
			mealing:
			for(int s = 0; s < 6; s++) {
				ForgeDirection side = ForgeDirection.getOrientation(s);
				TileEntity te = worldObj.getTileEntity(xCoord+side.offsetX, yCoord+side.offsetY, zCoord+side.offsetZ);
				if(te instanceof IInventory) {
					IInventory inv = (IInventory)te;
					for(int i = 0; i < inv.getSizeInventory(); i++) {
						ItemStack stack = inv.getStackInSlot(i);
						if(stack!=null) {
							if(stack.getItem().equals(Items.dye) && stack.getItemDamage()==15 && stack.stackSize>0) {
								Random r = new Random();
								int tx = xCoord+r.nextInt(8)-4;
								int tz = zCoord+r.nextInt(8)-4;
								int cy = yCoord+4;
								while(cy>yCoord-4) {
									Block b = worldObj.getBlock(tx, cy, tz);
									if(b instanceof IGrowable && b != Blocks.grass) {
										IGrowable g = (IGrowable) b;
										if (g.func_149851_a(worldObj, tx, cy, tz, worldObj.isRemote))
							            {
											if (g.func_149852_a(worldObj, worldObj.rand, tx, cy, tz))
											{
							                    g.func_149853_b(worldObj, worldObj.rand, tx, cy, tz);
							                    stack.stackSize--;
							                    amount-=use;
							                    break mealing;
							                }
							            }
									}
									cy--;
								}
							}
						}
					}
				}
			}
		}
	}
}