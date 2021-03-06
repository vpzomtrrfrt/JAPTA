package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public abstract class BlockBlaster extends BlockModelContainer implements IDiagnosable {

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public static int RANGE = 8;

    private boolean inhaler;

    @Override
    public abstract TileEntity createNewTileEntity(World worldIn, int meta);

    public BlockBlaster(boolean inhaler, boolean setNames) {
        super(Material.ROCK);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.inhaler = inhaler;
        if (setNames) {
            setNames();
        }
        setHardness(1);
        setCreativeTab(JAPTA.tab);
    }

    public BlockBlaster(boolean inhaler) {
        this(inhaler, true);
    }

    public void setNames() {
        String name = getName();
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public String getName() {
        return getBlasterType() + (inhaler ? "Inhaler" : "Blaster");
    }

    protected abstract String getBlasterType();

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack p_onBlockPlacedBy_5_) {
        super.onBlockPlacedBy(world, pos, state, placer, p_onBlockPlacedBy_5_);
        EnumFacing ts = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        if (!placer.isSneaking()) {
            ts = ts.getOpposite();
        }
        world.setBlockState(pos, state.withProperty(FACING, ts), 2);
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing side = state.getValue(FACING);
        sender.sendMessage(new TextComponentTranslation("tile.blaster.diagnostic.top", side.getName()));
        for (int i = 1; i <= RANGE; i++) {
            BlockPos cp = pos.offset(side, i);
            IBlockState cs = world.getBlockState(cp);
            sender.sendMessage(new TextComponentString(cs.getBlock().getUnlocalizedName()));
        }
        return true;
    }

    public boolean isInhaler() {
        return inhaler;
    }
}
