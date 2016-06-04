package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityItemBlaster;

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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack p_onBlockActivated_6_, EnumFacing p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_, float p_onBlockActivated_10_) {
        player.displayGUIChest((IInventory) world.getTileEntity(pos));
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        InventoryHelper.dropInventoryItems(world, pos, ((IInventory) world.getTileEntity(pos)));
        super.breakBlock(world, pos, state);
    }
}
