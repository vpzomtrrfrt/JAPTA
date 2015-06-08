package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;

public class TileEntityChargingPlate extends TileEntityJPT {
	int maxAmount = 1000;

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return maxAmount;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)>0) {
			AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1);
			List<EntityPlayer> l = worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
			Iterator<EntityPlayer> it = l.iterator();
			while(it.hasNext()) {
				EntityPlayer p = it.next();
				for(int i = 0; i < p.inventory.getSizeInventory(); i++) {
					if(amount>0) {
						ItemStack s = p.inventory.getStackInSlot(i);
						if(s!=null && s.getItem() instanceof IEnergyContainerItem) {
							IEnergyContainerItem ei = (IEnergyContainerItem) s.getItem();
							amount-=ei.receiveEnergy(s, amount, false);
						}
					}
					else {
						break;
					}
				}
			}
			if(worldObj.getBlock(xCoord, yCoord, zCoord).equals(JAPTA.chargingPlateWooden)) {
				List<EntityLivingBase> ll = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
				Iterator<EntityLivingBase> lit = ll.iterator();
				while(lit.hasNext()) {
					EntityLivingBase lb = lit.next();
					if(!(lb instanceof EntityPlayer)) {
						for(int i = 0; i < 5; i++) {
							ItemStack s = lb.getEquipmentInSlot(i);
							if(amount>0 && s != null && s.getItem() instanceof IEnergyContainerItem) {
								IEnergyContainerItem ei = ((IEnergyContainerItem) s.getItem());
								amount-=ei.receiveEnergy(s, amount, false);
							}
						}
					}
				}
				List<EntityItem> eat = worldObj.getEntitiesWithinAABB(EntityItem.class, bb);
				Iterator<EntityItem> itit = eat.iterator();
				while(itit.hasNext()) {
					EntityItem te = itit.next();
					ItemStack s = te.getEntityItem();
					if(amount>0) {
						if(s != null && s.getItem() instanceof IEnergyContainerItem) {
							amount-=((IEnergyContainerItem)s.getItem()).receiveEnergy(s, amount, false);
						}
						te.age=0;
					}
				}
			}
		}
	}

}