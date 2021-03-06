package net.reederhome.colin.mods.JAPTA.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockPowerCabinet;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCapacitor extends ItemJPT {
    public static final String BONUS_CAPACITY_TAG = "bonusCapacity";

    @Override
    public int getMaxDamage(ItemStack stack) {
        return BlockPowerCabinet.MAX_META_VALUE+getBonusCapacity(stack);
    }

    public ItemCapacitor() {
        setCreativeTab(JAPTA.tab);
        setRegistryName("capacitor");
        setUnlocalizedName("capacitor");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag p_addInformation_4_) {
        super.addInformation(stack, world, list, p_addInformation_4_);
        list.add(this.getEnergyStored(stack)+" / "+this.getMaxEnergyStored(stack)+" RF");
    }

    public int getBonusCapacity(ItemStack stack) {
        if(stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if(tag.hasKey(BONUS_CAPACITY_TAG)) {
                return tag.getInteger(BONUS_CAPACITY_TAG);
            }
        }
        return 0;
    }
}