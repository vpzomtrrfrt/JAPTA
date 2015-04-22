package net.reederhome.colin.mods.JAPTA;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class BlockEnergyTeleporter extends Block implements ITileEntityProvider {

	protected BlockEnergyTeleporter() {
		super(Material.iron);
		setBlockName("energyTeleporter");
		setBlockTextureName(JAPTA.modid+":energyTeleporter");
		setCreativeTab(JAPTA.tab);
		setHardness(12);
		setHarvestLevel("pickaxe", 2);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityEnergyTeleporter();
	}

	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase lb, ItemStack is) {
		TileEntityEnergyTeleporter te = (TileEntityEnergyTeleporter) w.getTileEntity(x, y, z);
		te.item=lb.getCommandSenderName();
		te.player=lb.getCommandSenderName();
	}
	
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int par1, float par2, float par3, float par4) {
		if(w.isRemote) return true;
		TileEntityEnergyTeleporter te = (TileEntityEnergyTeleporter) w.getTileEntity(x, y, z);
		if(te.player==null||p.getCommandSenderName().equals(te.player)) {
			ItemStack hi = p.getHeldItem();
			if(hi!=null) {
				String nf = hi.getUnlocalizedName();
				te.item=nf;
			}
			p.addChatMessage(new ChatComponentTranslation("text.japta.energyTeleporter.status", te.item, te.get(te.item)));
		}
		else {
			p.addChatMessage(new ChatComponentTranslation("text.japta.energyTeleporter.no", te.player));
			w.playSoundAtEntity(p, "mob.villager.no", 1, 1);
		}
		return true;
	}
}