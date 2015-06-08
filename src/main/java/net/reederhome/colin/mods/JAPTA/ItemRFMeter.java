package net.reederhome.colin.mods.JAPTA;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemRFMeter extends Item {

	public ItemRFMeter() {
		super();
		setCreativeTab(JAPTA.tab);
		setMaxStackSize(1);
		setTextureName(JAPTA.modid+":rfMeter");
		setUnlocalizedName("rfMeter");
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer p, World w, int x, int y, int z, int s, float f1, float f2, float f3) {
		if(w.isRemote) return true;
		TileEntity te = w.getTileEntity(x, y, z);
		int rf = -5;
		if(te instanceof IEnergyReceiver) {
			rf = ((IEnergyReceiver) te).getEnergyStored(ForgeDirection.getOrientation(s));
		}
		else if(te instanceof IEnergyProvider) {
			rf = ((IEnergyProvider) te).getEnergyStored(ForgeDirection.getOrientation(s));
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