package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockLifeConverter extends BlockContainer {

	IIcon iconHeal;
	IIcon iconAbsorb;
	
	public BlockLifeConverter() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setBlockTextureName(JAPTA.modid+":lifeConverter");
		setBlockName("lifeConverter");
		setHardness(2);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityLifeConverter();
	}
	
	public IIcon getIcon(int side, int meta) {
		return meta==0?iconAbsorb:iconHeal;
	}
	
	public void registerBlockIcons(IIconRegister ir) {
		iconAbsorb = ir.registerIcon(getTextureName()+"Absorb");
		iconHeal = ir.registerIcon(getTextureName()+"Heal");
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p, int s, float f1, float f2, float f3) {
		if(!world.isRemote) {
			int meta = world.getBlockMetadata(x, y, z);
			if(meta==0) {
				meta=5;
			}
			else {
				meta=0;
			}
			IChatComponent chat = new ChatComponentTranslation("text.japta.lifeConverter.mode"+(meta==0?"Absorb":"Heal"));
			p.addChatMessage(chat);
			world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		}
		return true;
	}

}