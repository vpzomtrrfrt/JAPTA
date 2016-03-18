package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;

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
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase ent) {
        stack.damageItem(USE, ent);
        if(ent instanceof EntityPlayer) {
            ((EntityPlayer)ent).getFoodStats().addStats(3, 1);
        }
        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer p, EnumHand hand) {
        if (p.canEat(false) && stack.getItemDamage() + USE <= stack.getMaxDamage()) {
            p.func_184598_c(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int remaining = getMaxEnergyStored(container) - getEnergyStored(container);
        int tr;
        if (maxReceive < remaining) {
            tr = maxReceive;
        } else {
            tr = remaining;
        }
        if (!simulate) {
            container.setItemDamage(container.getItemDamage() - tr);
        }
        return tr;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int tr;
        int stored = getEnergyStored(container);
        if (maxExtract < stored) {
            tr = maxExtract;
        } else {
            tr = stored;
        }
        if (!simulate) {
            container.setItemDamage(container.getItemDamage() + tr);
        }
        return tr;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return getMaxDamage() - container.getItemDamage();
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return getMaxDamage();
    }
}
