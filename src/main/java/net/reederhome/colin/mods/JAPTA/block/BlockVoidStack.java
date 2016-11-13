package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityVoidStack;

import javax.annotation.Nullable;

public class BlockVoidStack extends BlockModelContainer {
    public BlockVoidStack() {
        super(Material.IRON);
        setCreativeTab(JAPTA.tab);
        setHardness(3);
        setUnlocalizedName("voidStack");
        setRegistryName("voidStack");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityVoidStack();
    }

    @Override
    public boolean onBlockActivated(World p_onBlockActivated_1_, BlockPos p_onBlockActivated_2_, IBlockState p_onBlockActivated_3_, EntityPlayer p_onBlockActivated_4_, EnumHand p_onBlockActivated_5_, @Nullable ItemStack p_onBlockActivated_6_, EnumFacing p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_, float p_onBlockActivated_10_) {
        if(super.onBlockActivated(p_onBlockActivated_1_, p_onBlockActivated_2_, p_onBlockActivated_3_, p_onBlockActivated_4_, p_onBlockActivated_5_, p_onBlockActivated_6_, p_onBlockActivated_7_, p_onBlockActivated_8_, p_onBlockActivated_9_, p_onBlockActivated_10_)) {
            return true;
        }
        p_onBlockActivated_4_.displayGUIChest((IInventory) p_onBlockActivated_1_.getTileEntity(p_onBlockActivated_2_));
        return true;
    }

    @Override
    public void breakBlock(World p_breakBlock_1_, BlockPos p_breakBlock_2_, IBlockState p_breakBlock_3_) {
        TileEntityVoidStack te = (TileEntityVoidStack) p_breakBlock_1_.getTileEntity(p_breakBlock_2_);
        te.dropItems();
        super.breakBlock(p_breakBlock_1_, p_breakBlock_2_, p_breakBlock_3_);
    }
}
