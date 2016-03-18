package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public class ItemRFMeter extends Item {
    public ItemRFMeter() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName("rfMeter");
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            int value = -2;
            int max = -1;
            if (te instanceof IEnergyReceiver) {
                value = ((IEnergyReceiver) te).getEnergyStored(side);
                max = ((IEnergyReceiver) te).getMaxEnergyStored(side);
            } else if (te instanceof IEnergyProvider) {
                value = ((IEnergyProvider) te).getEnergyStored(side);
                max = ((IEnergyProvider) te).getMaxEnergyStored(side);
            }
            if (value != -2) {
                player.addChatComponentMessage(new TextComponentTranslation("text.japta.rfmeter.rf", value, max));
            } else {
                player.addChatComponentMessage(new TextComponentTranslation("text.japta.rfmeter.no"));
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
