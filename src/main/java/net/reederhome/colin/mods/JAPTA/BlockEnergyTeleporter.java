package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class BlockEnergyTeleporter extends BlockContainer {

	protected BlockEnergyTeleporter() {
		super(Material.iron);
		setBlockName("energyTeleporter");
		setBlockTextureName(JAPTA.modid+":energyTeleporter");
		setCreativeTab(JAPTA.tab);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityEnergyTeleporter();
	}

	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase lb, ItemStack is) {
		TileEntityEnergyTeleporter te = (TileEntityEnergyTeleporter) w.getTileEntity(x, y, z);
		if(lb instanceof EntityPlayer) {
			te.player=lb.getCommandSenderName();
		}
		te.item=lb.getCommandSenderName();
	}
	
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int par1, float par2, float par3, float par4) {
		TileEntityEnergyTeleporter te = (TileEntityEnergyTeleporter) w.getTileEntity(x, y, z);
		if(te.player==null||p.getCommandSenderName()==te.player) {
			ItemStack hi = p.getHeldItem();
			if(hi!=null) {
				String nf = hi.getUnlocalizedName();
				te.item=nf;
			}
			p.addChatMessage(new ChatComponentTranslation("text.japta.energyTeleporter.status", te.item, te.get(te.item)));
		}
		else {
			p.addChatMessage(new ChatComponentTranslation("text.japta.energyTeleporter.no"));
			w.playSoundAtEntity(p, "mob.villager.no", 1, 1);
		}
		return true;
	}
}