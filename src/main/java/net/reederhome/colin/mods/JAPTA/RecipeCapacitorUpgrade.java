package net.reederhome.colin.mods.JAPTA;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.reederhome.colin.mods.JAPTA.block.BlockPowerCabinet;
import net.reederhome.colin.mods.JAPTA.item.ItemCapacitor;

public class RecipeCapacitorUpgrade extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return ItemStackTools.isValid(getCraftingResult(inventoryCrafting));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack capacitor = null;
        int addedCapacity = 0;
        int addedPower = 0;
        for(int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(i);
            Item item = stack.getItem();
            if(item == JAPTA.capacitor) {
                if(capacitor == null) {
                    capacitor = stack;
                }
                else {
                    return ItemStack.EMPTY;
                }
            }
            else if(item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockPowerCabinet) {
                BlockPowerCabinet block = ((BlockPowerCabinet) ((ItemBlock) item).getBlock());
                addedCapacity += block.getMetaValue()*15;
                addedPower += block.getMetaValue()*item.getDamage(stack);
            }
            else {
                return ItemStack.EMPTY;
            }
        }
        if(capacitor == null || addedCapacity == 0) {
            return ItemStack.EMPTY;
        }
        else {
            ItemStack tr = getRecipeOutput();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(ItemCapacitor.BONUS_CAPACITY_TAG, addedCapacity+JAPTA.capacitor.getBonusCapacity(capacitor));
            tr.setTagCompound(tag);
            JAPTA.capacitor.setEnergyStored(tr, JAPTA.capacitor.getEnergyStored(capacitor)+addedPower);
            return tr;
        }
    }

    public RecipeCapacitorUpgrade() {
        setRegistryName(new ResourceLocation(JAPTA.MODID, "crafting_capacitorUpgrade"));
    }

    @Override
    public boolean func_194133_a(int i, int i1) {
        return i*i1 > 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(JAPTA.capacitor);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting) {
        return NonNullList.create();
    }

}
