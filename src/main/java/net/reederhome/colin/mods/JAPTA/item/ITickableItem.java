package net.reederhome.colin.mods.JAPTA.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ITickableItem {
    void update(ItemStack stack, EntityPlayer player);
}
