package net.reederhome.colin.mods.JAPTA.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityFluidHopper;

import javax.annotation.Nullable;

public class BlockFluidHopper extends BlockModelContainer {

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, new Predicate<EnumFacing>() {
        @Override
        public boolean apply(EnumFacing input) {
            return input != EnumFacing.UP;
        }
    });

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFluidHopper();
    }

    public BlockFluidHopper() {
        super(Material.IRON);
        setUnlocalizedName("fluidHopper");
        setRegistryName("fluidHopper");
        setHardness(3);
        setCreativeTab(JAPTA.tab);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing dir = facing.getOpposite();
        if (dir == EnumFacing.UP) {
            dir = EnumFacing.DOWN;
        }
        return getDefaultState().withProperty(FACING, dir);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState p_onBlockActivated_3_, EntityPlayer player, EnumHand p_onBlockActivated_5_, EnumFacing p_onBlockActivated_6_, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
        if (world.isRemote) return true;
        TileEntityFluidHopper te = (TileEntityFluidHopper) world.getTileEntity(pos);
        IFluidTankProperties info = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN).getTankProperties()[0];
        if (info.getContents() != null) {
            player.addChatComponentMessage(new TextComponentTranslation("text.japta.fluidHopper.status", info.getContents().getFluid().getLocalizedName(info.getContents()), info.getContents().amount), false);
        } else {
            player.addChatComponentMessage(new TextComponentTranslation("text.japta.fluidHopper.empty"), false);
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_) {
        return false;
    }
}
