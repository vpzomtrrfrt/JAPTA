package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class TileEntityLifeConverter extends TileEntityJPT {
	
	int amount = 0;
	static final int maxAmount = 100;
	static final int inc = 4;
	static final float th = 1;
	
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)>0) {
			List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord-0.5, yCoord, zCoord-0.5, xCoord+1.5, yCoord+2.5, zCoord+1.5));
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
	public int getMaxEnergyStored(ForgeDirection from) {
		return maxAmount;
	}

}