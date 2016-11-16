package net.reederhome.colin.mods.JAPTA;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.block.BlockPowerCabinet;
import net.reederhome.colin.mods.JAPTA.item.ItemCapacitor;

import javax.annotation.Nullable;

public class RecipeCapacitorUpgrade implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return getCraftingResult(inventoryCrafting) != null;
    }

    @Nullable
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack capacitor = null;
        int addedCapacity = 0;
        int addedPower = 0;
        for(int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(i);
            if(stack != null) {
                Item item = stack.getItem();
                if(item == JAPTA.capacitor) {
                    if(capacitor == null) {
                        capacitor = stack;
                    }
                    else {
                        return null;
                    }
                }
                else if(item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockPowerCabinet) {
                    BlockPowerCabinet block = ((BlockPowerCabinet) ((ItemBlock) item).getBlock());
                    addedCapacity += block.getMetaValue()*15;
                    addedPower += block.getMetaValue()*item.getDamage(stack);
                }
                else {
                    return null;
                }
            }
        }
        if(capacitor == null || addedCapacity == 0) {
            return null;
        }
        else {
            ItemStack tr = new ItemStack(JAPTA.capacitor);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(ItemCapacitor.BONUS_CAPACITY_TAG, addedCapacity+JAPTA.capacitor.getBonusCapacity(capacitor));
            tr.setTagCompound(tag);
            JAPTA.capacitor.setEnergyStored(tr, JAPTA.capacitor.getEnergyStored(capacitor)+addedPower);
            return tr;
        }
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Nullable
    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting) {
        return NonNullList.func_191196_a();
    }

}
