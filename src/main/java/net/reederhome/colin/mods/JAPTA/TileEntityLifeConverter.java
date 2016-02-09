package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import cofh.api.energy.IEnergyHandler;

public class TileEntityLifeConverter extends TileEntityJPT implements ITickable {
	static final int maxAmount = 100;
	static final int inc = 4;
	static final float th = 1;
	
	public void update() {
		BlockPos me = getPos();
		if(!worldObj.getBlockState(me).getValue(BlockLifeConverter.MODE) && worldObj.getWorldTime()%9==0) {
			List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.fromBounds(me.getX()-0.5, me.getY()-1.5, me.getZ()-0.5, me.getX()+1.5, me.getY()+2.5, me.getZ()+1.5));
			Iterator<EntityLivingBase> it = l.iterator();
			while(it.hasNext()&&amount>th*inc/2) {
				EntityLivingBase e = it.next();
				if(e.getHealth()<e.getMaxHealth()) {
					e.heal(th);
					amount-=th*inc/2;
				}
			}
		}
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return maxAmount;
	}

}