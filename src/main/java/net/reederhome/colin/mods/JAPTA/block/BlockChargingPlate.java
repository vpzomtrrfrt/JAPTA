package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityChargingPlate;

import java.util.List;

public class BlockChargingPlate extends BlockBasePressurePlate implements ITileEntityProvider {

    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockChargingPlate() {
        super(Material.rock);
        setDefaultState(blockState.getBaseState().withProperty(POWERED, false));
        setHardness(1);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("chargingPlate");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChargingPlate();
    }

    public List<EntityPlayer> getPlayers(World world, BlockPos pos) {
        return world.getEntitiesWithinAABB(EntityPlayer.class, field_185511_c.func_186670_a(pos));
    }

    @Override
    protected void func_185507_b(World world, BlockPos pos) {
        world.func_184133_a(null, pos, SoundEvents.field_187847_fZ, SoundCategory.BLOCKS, .3f, .6f);
    }

    @Override
    protected void func_185508_c(World world, BlockPos pos) {
        world.func_184133_a(null, pos, SoundEvents.field_187847_fZ, SoundCategory.BLOCKS, .3f, .5f);
    }

    @Override
    protected int computeRedstoneStrength(World world, BlockPos pos) {
        return getPlayers(world, pos).isEmpty() ? 0 : 15;
    }

    @Override
    protected int getRedstoneStrength(IBlockState state) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(POWERED, strength > 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(POWERED, meta > 0);
    }
}
