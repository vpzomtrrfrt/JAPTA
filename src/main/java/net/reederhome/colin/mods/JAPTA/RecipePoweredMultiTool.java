package net.reederhome.colin.mods.JAPTA;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipePoweredMultiTool extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return ItemStackTools.isValid(getCraftingResult(inventoryCrafting));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int numCoils = 0;
        String pickaxe = null;
        String sword = null;
        String axe = null;
        String shovel = null;
        String hoe = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == JAPTA.coilReception) {
                numCoils++;
            } else if (stack.getItem() instanceof ItemPickaxe) {
                pickaxe = ((ItemPickaxe) stack.getItem()).getToolMaterialName();
            } else if (stack.getItem() instanceof ItemSword) {
                sword = ((ItemSword) stack.getItem()).getToolMaterialName();
            } else if (stack.getItem() instanceof ItemAxe) {
                axe = ((ItemAxe) stack.getItem()).getToolMaterialName();
            } else if (stack.getItem() instanceof ItemSpade) {
                shovel = ((ItemSpade) stack.getItem()).getToolMaterialName();
            } else if (stack.getItem() instanceof ItemHoe) {
                hoe = ((ItemHoe) stack.getItem()).getMaterialName();
            } else if(ItemStackTools.isValid(stack)) {
                return ItemStack.EMPTY;
            }
        }
        if (numCoils == 1 && pickaxe != null && sword != null && axe != null && shovel != null && hoe != null) {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound materials = new NBTTagCompound();
            materials.setString("pickaxe", pickaxe);
            materials.setString("sword", sword);
            materials.setString("axe", axe);
            materials.setString("shovel", shovel);
            materials.setString("hoe", hoe);
            tag.setTag("Materials", materials);
            ItemStack tr = getRecipeOutput();
            tr.setTagCompound(tag);
            tr.setItemDamage(tr.getMaxDamage());
            return tr;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean func_194133_a(int i, int i1) {
        return i*i1 >= 7;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(JAPTA.poweredMultiTool);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting) {
        return NonNullList.create();
    }
}
