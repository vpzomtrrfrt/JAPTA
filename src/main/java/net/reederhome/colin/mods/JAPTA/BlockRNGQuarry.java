package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockRNGQuarry extends BlockContainer {
	public BlockRNGQuarry() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setHardness(5);
		setHarvestLevel("pickaxe", 2);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(placer instanceof EntityPlayer&&!world.isRemote) {
			((EntityPlayer) placer).addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.noTool"));
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer p, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
		ItemStack stack = p.getHeldItem();
		if(!world.isRemote) {
			if(stack==null) {
				if(te.itm==null) {
					p.addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.noTool", te.amount));
				}
				else {
					p.addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.hasTool", te.itm.getChatComponent(), te.amount));
				}
			}
			else {
				if(te.itm!=null) {
					world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY()+1, pos.getZ(), te.itm));
				}
				te.itm=stack;
				p.inventory.setInventorySlotContents(p.inventory.currentItem, null);
				p.addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.gotTool", te.itm.getChatComponent()));
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityRNGQuarry();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
		if(te.itm!=null&&!world.isRemote) {
			world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), te.itm));
		}
	}

}