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
		if(is.hasTagCompound()) {
			te.readFromNBT(is.stackTagCompound);
		}
		if(lb instanceof EntityPlayer) {
			te.player=lb.getCommandSenderName();
		}
	}
	
	public int quantityDropped(Random r) {
		return 0;
	}
	
	public void breakBlock(World w, int x, int y, int z, Block b, int i1) {
		if (!w.isRemote && w.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            float var6 = 0.7F;
            double var7 = (double)(w.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            double var9 = (double)(w.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            double var11 = (double)(w.rand.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            ItemStack stack = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            TileEntity te = w.getTileEntity(x, y, z);
            if(te!=null) {
            	te.writeToNBT(tag);
            	System.out.println(tag);
            	w.removeTileEntity(x, y, z);
            }
            else {
            	System.out.println("aww?");
            }
            stack.setTagCompound(tag);
            EntityItem var13 = new EntityItem(w, (double)x + var7, (double)y + var9, (double)z + var11, stack);
            var13.delayBeforeCanPickup = 10;
            w.spawnEntityInWorld(var13);
        }
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