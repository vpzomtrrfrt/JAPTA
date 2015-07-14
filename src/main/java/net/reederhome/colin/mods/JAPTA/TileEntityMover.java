package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMover extends TileEntityJPT {

	static final int use = 2;
	
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 20;
	}
	
	public void updateEntity() {
		super.updateEntity();
		transmit(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		List<Entity> l = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord+1, yCoord+2, zCoord+1));
		Iterator<Entity> it = l.iterator();
		while(it.hasNext() && amount>use) {
			Entity e = it.next();
			if(e.isSneaking()) continue;
			ForgeDirection d = ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
			double scale = 0.5;
			double nx = e.posX+d.offsetX*scale;
			double ny = e.posY+d.offsetY*scale;
			double nz = e.posZ+d.offsetZ*scale;
			if(e instanceof EntityLivingBase) {
				((EntityLivingBase)e).setPositionAndUpdate(nx, ny, nz);
			}
			else {
				e.setPosition(nx, ny, nz);
			}
			amount-=use;
		}
	}

}
