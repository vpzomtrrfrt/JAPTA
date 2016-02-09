package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityMover extends TileEntityJPT implements ITickable {

	static final int use = 20;
	
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return 200;
	}
	
	public void update() {
		BlockPos me = getPos();
		EnumFacing d = worldObj.getBlockState(me).getValue(BlockMover.FACING);
		transmit(d);
		List<Entity> l = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.fromBounds(me.getX(), me.getY(), me.getZ(), me.getX()+1, me.getY()+2, me.getZ()+1));
		Iterator<Entity> it = l.iterator();
		while(it.hasNext() && amount>use) {
			Entity e = it.next();
			if(e.isSneaking()) continue;
			double scale = 0.5;
			double nx = e.posX+d.getFrontOffsetX()*scale;
			double ny = e.posY+d.getFrontOffsetY()*scale;
			double nz = e.posZ+d.getFrontOffsetZ()*scale;
			e.setPositionAndUpdate(nx, ny, nz);
			amount-=use;
		}
	}

}
