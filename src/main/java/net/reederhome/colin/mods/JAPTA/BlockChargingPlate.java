package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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

    protected List<EntityPlayer> getPlayers(World world, BlockPos pos) {
        return world.getEntitiesWithinAABB(EntityPlayer.class, getSensitiveAABB(pos));
    }

    @Override
    protected int computeRedstoneStrength(World world, BlockPos pos) {
        return getPlayers(world, pos).isEmpty()?0:15;
    }

    @Override
    protected int getRedstoneStrength(IBlockState state) {
        return state.getValue(POWERED)?15:0;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(POWERED, strength > 0);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED)?1:0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(POWERED, meta>0);
    }
}
