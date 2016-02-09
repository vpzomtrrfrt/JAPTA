package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;


public class TileEntityElevatorTop extends TileEntityJPT implements ITickable {

	int useBase = 1000;
	int useExtra = 100;
	
	public int getEnergyCost(int d) {
		return useBase+d*useExtra;
	}
	
	public void update() {
		if(amount<useBase) return;
		BlockPos me = getPos();
		List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.fromBounds(me.getX(), me.getY(), me.getZ(), me.getX()+1, me.getY()+2, me.getZ()+1));
		if(l.size()>0) {
			BlockPos cp = me.down();
			boolean found = false;
			while(cp.getY()>0 && !found) {
				if(worldObj.getBlockState(cp).getBlock().equals(JAPTA.elevatorShaft)) {
					cp = cp.down();
				}
				else if(worldObj.isAirBlock(cp)) {
					found = true;
				}
				else {
					break;
				}
			}
			int use = getEnergyCost(me.getY()-cp.getY()-1);
			if(found && !worldObj.isRemote) {
				Iterator<EntityLivingBase> it = l.iterator();		
				while(it.hasNext() && amount >= use) {
					EntityLivingBase e = it.next();
					if(e.isSneaking() && e.onGround) {
						e.setPositionAndUpdate(e.posX, cp.getY()-1, e.posZ);
						worldObj.playSoundAtEntity(e, "mob.chicken.plop", 1, 0.5f);
						amount-=use;
					}
				}
			}
		}
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return 4000;
	}
}