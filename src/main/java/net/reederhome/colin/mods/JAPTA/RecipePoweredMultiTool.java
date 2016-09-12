package net.reederhome.colin.mods.JAPTA;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipePoweredMultiTool implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return getCraftingResult(inventoryCrafting) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int numCoils = 0;
        Item.ToolMaterial pickaxe = null;
        String sword = null;
        Item.ToolMaterial axe = null;
        Item.ToolMaterial shovel = null;
        String hoe = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) {
                continue;
            }
            if (stack.getItem() == JAPTA.coilReception) {
                numCoils++;
            } else if (stack.getItem() instanceof ItemPickaxe) {
                pickaxe = ((ItemPickaxe) stack.getItem()).getToolMaterial();
            } else if (stack.getItem() instanceof ItemSword) {
                sword = ((ItemSword) stack.getItem()).getToolMaterialName();
            } else if (stack.getItem() instanceof ItemAxe) {
                axe = ((ItemAxe) stack.getItem()).getToolMaterial();
            } else if (stack.getItem() instanceof ItemSpade) {
                shovel = ((ItemSpade) stack.getItem()).getToolMaterial();
            } else if (stack.getItem() instanceof ItemHoe) {
                hoe = ((ItemHoe) stack.getItem()).getMaterialName();
            } else {
                return null;
            }
        }
        if (numCoils == 1 && pickaxe != null && sword != null && axe != null && shovel != null && hoe != null) {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound materials = new NBTTagCompound();
            materials.setString("pickaxe", pickaxe.name());
            materials.setString("sword", sword);
            materials.setString("axe", axe.name());
            materials.setString("shovel", shovel.name());
            materials.setString("hoe", hoe);
            tag.setTag("Materials", materials);
            ItemStack tr = getRecipeOutput();
            tr.setTagCompound(tag);
            tr.setItemDamage(tr.getMaxDamage());
            return tr;
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(JAPTA.poweredMultiTool);
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        return new ItemStack[inventoryCrafting.getSizeInventory()];
    }
}
