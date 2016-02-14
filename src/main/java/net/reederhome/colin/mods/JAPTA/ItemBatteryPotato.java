package net.reederhome.colin.mods.JAPTA;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemBatteryPotato extends Item implements IEnergyContainerItem {

    public static final int USE = 500;

    public ItemBatteryPotato() {
        super();
        setMaxStackSize(1);
        setMaxDamage(16000);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("batteryPotato");
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item));
        list.add(new ItemStack(item, 1, getMaxDamage()));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer p) {
        stack.damageItem(USE, p);
        p.getFoodStats().addStats(3, 1);
        return stack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer p) {
        if(p.canEat(false) && stack.getItemDamage()+USE <= stack.getMaxDamage()) {
            p.setItemInUse(stack, getMaxItemUseDuration(stack));
        }
        return stack;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int remaining = getMaxEnergyStored(container) - getEnergyStored(container);
        int tr;
        if(maxReceive < remaining) {
            tr = maxReceive;
        }
        else {
            tr = remaining;
        }
        if(!simulate) {
            container.setItemDamage(container.getItemDamage()-tr);
        }
        return tr;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int tr;
        int stored = getEnergyStored(container);
        if(maxExtract < stored) {
            tr = maxExtract;
        }
        else {
            tr = stored;
        }
        if(!simulate) {
            container.setItemDamage(container.getItemDamage()+tr);
        }
        return tr;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return getMaxDamage()-container.getItemDamage();
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return getMaxDamage();
    }
}
