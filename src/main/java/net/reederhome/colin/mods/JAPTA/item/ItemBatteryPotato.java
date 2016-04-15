package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;

import java.util.List;

public class ItemBatteryPotato extends ItemJPT {

    public static final int USE = 500;

    public ItemBatteryPotato() {
        super();
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
        if (p.canEat(false) && stack.getItemDamage() + USE <= stack.getMaxDamage()) {
            p.setItemInUse(stack, getMaxItemUseDuration(stack));
        }
        return stack;
    }
}
