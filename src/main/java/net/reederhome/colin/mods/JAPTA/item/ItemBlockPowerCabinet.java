package net.reederhome.colin.mods.JAPTA.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPowerCabinet extends ItemBlock {
    public ItemBlockPowerCabinet(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + stack.getMetadata();
    }
}
