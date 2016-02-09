package net.reederhome.colin.mods.JAPTA;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class ItemRFMeter extends Item {

	public ItemRFMeter() {
		super();
		setCreativeTab(JAPTA.tab);
		setMaxStackSize(1);
		setUnlocalizedName("rfMeter");
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer p, World w, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(w.isRemote) return true;
		TileEntity te = w.getTileEntity(pos);
		int rf = -5;
		if(te instanceof IEnergyReceiver) {
			rf = ((IEnergyReceiver) te).getEnergyStored(side);
		}
		else if(te instanceof IEnergyProvider) {
			rf = ((IEnergyProvider) te).getEnergyStored(side);
		}
		if(rf!=-5) {
			p.addChatMessage(new ChatComponentTranslation("text.japta.rfmeter.rf", rf));
		}
		else {
			p.addChatMessage(new ChatComponentTranslation("text.japta.rfmeter.no"));
		}
		return true;
	}
}