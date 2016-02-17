package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockItemBlaster extends BlockBlaster {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityItemBlaster();
    }

    @Override
    protected String getBlasterType() {
        return "item";
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        player.displayGUIChest((IInventory) world.getTileEntity(pos));
        return true;
    }
}
