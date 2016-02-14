package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
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
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            int value = -2;
            if (te instanceof IEnergyReceiver) {
                value = ((IEnergyReceiver) te).getEnergyStored(side);
            } else if (te instanceof IEnergyProvider) {
                value = ((IEnergyProvider) te).getEnergyStored(side);
            }
            if (value != -2) {
                player.addChatComponentMessage(new ChatComponentTranslation("text.japta.rfmeter.rf", value));
            } else {
                player.addChatComponentMessage(new ChatComponentTranslation("text.japta.rfmeter.no"));
            }
        }
        return true;
    }
}
