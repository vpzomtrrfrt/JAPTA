package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class BlockCakeConverter extends BlockContainer {

	public static final PropertyBool MODE = PropertyBool.create("mode");
	
	public BlockCakeConverter() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setUnlocalizedName("cakeConverter");
		setHardness(2);
		setHarvestLevel("pickaxe", 2);
		setDefaultState(getBlockState().getBaseState().withProperty(MODE, true));
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityCakeConverter();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer p, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			boolean mode = state.getValue(MODE);
			mode = !mode;
			IChatComponent chat = new ChatComponentTranslation("text.japta.converter.mode"+(mode?"Absorb":"Deploy"), "Cake");
			p.addChatMessage(chat);
			world.setBlockState(pos, state.withProperty(MODE, mode));
		}
		return true;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if(state.getValue(MODE)) {
			return 0;
		}
		else {
			return 5;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getBlockState().getBaseState().withProperty(MODE, meta == 0);
	}

	@Override
	public BlockState getBlockState() {
		return new BlockState(this, MODE);
	}
}