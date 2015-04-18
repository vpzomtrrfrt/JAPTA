package net.reederhome.colin.mods.JAPTA;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOne extends ItemBlock {

	public ItemBlockOne(Block b) {
		super(b);
		setMaxStackSize(1);
	}
	
	public void addInformation(ItemStack s, EntityPlayer p, List l, boolean b) {
		if(field_150939_a instanceof IInfoProvider) {
			((IInfoProvider) field_150939_a).addInfo(s, p, l, b);
		}
	}
}
