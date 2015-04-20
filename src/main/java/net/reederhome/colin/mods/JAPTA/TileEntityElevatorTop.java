package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;


public class TileEntityElevatorTop extends TileEntityJPT {

	int useBase = 1000;
	int useExtra = 100;
	
	public int getEnergyCost(int d) {
		return useBase+d*useExtra;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if(amount<useBase) return;
		List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord+1, yCoord+2, zCoord+1));
		if(l.size()>0) {
			int ty = yCoord-1;
			boolean found = false;
			while(ty>0 && !found) {
				if(worldObj.getBlock(xCoord, ty, zCoord).equals(JAPTA.elevatorShaft)) {
					ty--;
				}
				else if(worldObj.isAirBlock(xCoord, ty, zCoord)) {
					found = true;
				}
				else {
					break;
				}
			}
			int use = getEnergyCost(yCoord-ty-1);
			if(found && !worldObj.isRemote) {
				Iterator<EntityLivingBase> it = l.iterator();		
				while(it.hasNext() && amount >= use) {
					EntityLivingBase e = it.next();
					if(e.isSneaking() && e.onGround) {
						e.setPositionAndUpdate(e.posX, ty-1, e.posZ);
						worldObj.playSoundAtEntity(e, "mob.chicken.plop", 1, 0.5f);
						amount-=use;
					}
				}
			}
		}
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 4000;
	}
}