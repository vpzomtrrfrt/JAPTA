package net.reederhome.colin.mods.JAPTA;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockMechanicalGenerator extends BlockContainer {
	
	public BlockMechanicalGenerator() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setHardness(4);
		setHarvestLevel("pickaxe", 2);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		TileEntityMechanicalGenerator te = (TileEntityMechanicalGenerator) world.getTileEntity(pos);
		if(te.amount+te.inc<te.maxAmount) {
			te.amount+=te.inc;
		}
		else {
			te.amount=te.maxAmount;
		}
		te.transmit();
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityMechanicalGenerator();
	}
}