package net.reederhome.colin.mods.JAPTA;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cofh.api.energy.IEnergyContainerItem;

public class ItemBatteryPotato extends Item implements IEnergyContainerItem {

	public static final int maxAmount = 16000;
	
	public ItemBatteryPotato() {
		super();
		setMaxDamage(maxAmount);
		setMaxStackSize(1);
		setCreativeTab(JAPTA.tab);
		setUnlocalizedName("batteryPotato");
		setTextureName(JAPTA.modid+":batteryPotato");
	}
	
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.eat;
	}
	
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}
	
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		if(stack.getItemDamage()+200<=stack.getMaxDamage()) {
			player.getFoodStats().addStats(3, 1);
			stack.setItemDamage(stack.getItemDamage()+200);
		}
		return stack;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(player.canEat(false)) {
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}
		return stack;
	}
	
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive,
			boolean simulate) {
		int tr;
		int amount = maxAmount-container.getItemDamage();
		if(amount+maxReceive<=maxAmount) {
			tr=maxReceive;
		}
		else {
			tr=maxAmount-amount;
		}
		if(!simulate) {
			container.setItemDamage(maxAmount-(amount+tr));
		}
		return tr;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract,
			boolean simulate) {
		int tr;
		int amount = maxAmount-container.getItemDamage();
		if(amount>maxExtract) {
			tr=maxExtract;
		}
		else {
			tr=amount;
		}
		if(!simulate) {
			container.setItemDamage(maxAmount-(amount-tr));
		}
		return tr;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		return maxAmount-container.getItemDamage();
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return maxAmount;
	}
	
}