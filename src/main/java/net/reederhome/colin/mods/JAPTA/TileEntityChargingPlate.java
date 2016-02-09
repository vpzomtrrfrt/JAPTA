package net.reederhome.colin.mods.JAPTA;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.util.ITickable;

public class TileEntityChargingPlate extends TileEntityJPT implements ITickable {
	int maxAmount = 1000;

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return maxAmount;
	}
	
	public void update() {
		BlockPos me = getPos();
		if(worldObj.getBlockState(me).getValue(BlockChargingPlate.POWERED)) {
			AxisAlignedBB bb = AxisAlignedBB.fromBounds(me.getX(), me.getY(), me.getZ(), me.getX()+1, me.getY()+1, me.getZ()+1);
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
			if(worldObj.getBlockState(me).getBlock().equals(JAPTA.chargingPlateWooden)) {
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
						te.setAgeToCreativeDespawnTime();
					}
				}
			}
		}
	}

}