package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
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
import net.reederhome.colin.mods.JAPTA.IDiagnosable;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public class ItemRFMeter extends Item {
    private boolean advanced;
    public ItemRFMeter(boolean advanced) {
        super();
        this.advanced = advanced;
        setMaxStackSize(1);
        setUnlocalizedName(advanced?"diagnosticTool":"rfMeter");
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
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
            boolean someinfo = false;
            if (value != -2) {
                player.addChatComponentMessage(new TextComponentTranslation("text.japta.rfmeter.rf", value, max));
                someinfo = true;
            }
            if(advanced) {
                if(state.getBlock() instanceof IDiagnosable) {
                    if(((IDiagnosable) state.getBlock()).addInformation(player, world, pos)) {
                        someinfo = true;
                    }
                }
                if(te instanceof IDiagnosable) {
                    if(((IDiagnosable) te).addInformation(player, world, pos)) {
                        someinfo = true;
                    }
                }
            }
            if(!someinfo) {
                player.addChatComponentMessage(new TextComponentTranslation("text.japta.rfmeter."+(advanced?"advancedNo":"no")));
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
