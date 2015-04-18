package net.reederhome.colin.mods.JAPTA;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IInfoProvider {

	public void addInfo(ItemStack s, EntityPlayer p, List l, boolean b);
}