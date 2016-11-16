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
    private boolean splitting;

    public BlockItemBlaster(boolean inhaler, boolean splitting) {
        super(inhaler, false);
        this.splitting = splitting;
        setNames();
    }

    @Override
    public String getName() {
        return splitting ? "itemSplitter" : super.getName();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityItemBlaster();
    }

    @Override
    protected String getBlasterType() {
        return "item";
    }

    @Override
    public boolean onBlockActivated(World p_onBlockActivated_1_, BlockPos p_onBlockActivated_2_, IBlockState p_onBlockActivated_3_, EntityPlayer p_onBlockActivated_4_, EnumHand p_onBlockActivated_5_, EnumFacing p_onBlockActivated_6_, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
        p_onBlockActivated_4_.displayGUIChest(((IInventory) p_onBlockActivated_1_.getTileEntity(p_onBlockActivated_2_)));
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        InventoryHelper.dropInventoryItems(world, pos, ((IInventory) world.getTileEntity(pos)));
        super.breakBlock(world, pos, state);
    }

    public boolean isSplitting() {
        return splitting;
    }
}
