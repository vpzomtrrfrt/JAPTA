package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockRNGQuarry extends BlockContainer {

	IIcon topIcon;
	public BlockRNGQuarry() {
		super(Material.rock);
		setBlockName("rngQuarry");
		setBlockTextureName(JAPTA.modid+":rngQuarry");
		setCreativeTab(JAPTA.tab);
		setHardness(5);
		setHarvestLevel("pickaxe", 2);
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase b, ItemStack stack) {
		if(b instanceof EntityPlayer&&!world.isRemote) {
			((EntityPlayer) b).addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.noTool"));
		}
	}
	
	public void registerBlockIcons(IIconRegister ir) {
		super.registerBlockIcons(ir);
		topIcon = ir.registerIcon(getTextureName()+"Top");
	}
	
	public IIcon getIcon(int side, int meta) {
		if(side<2) {
			return topIcon;
		}
		else {
			return blockIcon;
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p, int s, float f1, float f2, float f3) {
		TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(x, y, z);
		ItemStack stack = p.getHeldItem();
		if(!world.isRemote) {
			if(stack==null) {
				if(te.itm==null) {
					p.addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.noTool", te.amount));
				}
				else {
					p.addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.hasTool", te.itm.func_151000_E(), te.amount));
				}
			}
			else {
				if(te.itm!=null) {
					world.spawnEntityInWorld(new EntityItem(world, x, y+1, z, te.itm));
				}
				te.itm=stack;
				p.inventory.setInventorySlotContents(p.inventory.currentItem, null);
				p.addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.gotTool", te.itm.func_151000_E()));
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityRNGQuarry();
	}
	
	public void breakBlock(World world, int x, int y, int z, Block b, int meta) {
		TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(x, y, z);
		if(te.itm!=null&&!world.isRemote) {
			world.spawnEntityInWorld(new EntityItem(world, x, y, z, te.itm));
		}
	}

}