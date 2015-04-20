package net.reederhome.colin.mods.JAPTA;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMechanicalGenerator extends Block implements ITileEntityProvider, IInfoProvider {

	IIcon topIcon;
	
	public BlockMechanicalGenerator() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setBlockName("mechanicalGenerator");
		setBlockTextureName(JAPTA.modid+":mechanicalGenerator");
		setHardness(4);
		setHarvestLevel("pickaxe", 2);
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		TileEntityMechanicalGenerator te = (TileEntityMechanicalGenerator) world.getTileEntity(x, y, z);
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
	
	public void registerBlockIcons(IIconRegister ir) {
		super.registerBlockIcons(ir);
		topIcon = ir.registerIcon(getTextureName()+"Top");
	}
	
	public IIcon getIcon(int side, int meta) {
		return side<2?topIcon:blockIcon;
	}
	
	public int quantityDropped(Random r) {
		return 0;
	}
	
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase lb, ItemStack is) {
		if(is.hasTagCompound()) {
			w.getTileEntity(x, y, z).readFromNBT(is.stackTagCompound);
		}
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
            	tag.removeTag("x");
            	tag.removeTag("y");
            	tag.removeTag("z");
            	tag.removeTag("id");
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

	@Override
	public void addInfo(ItemStack s, EntityPlayer p, List l, boolean b) {
		if(s.hasTagCompound()) {
			NBTTagCompound tag = s.getTagCompound();
			if(tag.hasKey("Energy")) {
				l.add(tag.getInteger("Energy")+" RF");
			}
		}
	}
}