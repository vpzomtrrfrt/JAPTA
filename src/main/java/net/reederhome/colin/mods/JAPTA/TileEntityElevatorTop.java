package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;


public class TileEntityElevatorTop extends TileEntityJPT {

	int use = 1000;
	
	public void updateEntity() {
		super.updateEntity();
		if(amount<use) return;
		List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord+1, yCoord+2, zCoord+1));
		Iterator<EntityLivingBase> it = l.iterator();
		while(it.hasNext() && amount >= use) {
			EntityLivingBase e = it.next();
			if(e.isSneaking() && e.onGround) {
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
				if(found && !worldObj.isRemote) {
					e.setPositionAndUpdate(e.posX, ty-1, e.posZ);
					worldObj.playSoundAtEntity(e, "mob.chicken.plop", 1, 0.5f);
					amount-=use;
				}
			}
		}
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 4000;
	}
}