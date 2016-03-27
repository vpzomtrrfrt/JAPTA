package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityRNGQuarry;

public class BlockRNGQuarry extends BlockContainer {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRNGQuarry();
    }

    public BlockRNGQuarry() {
        super(Material.iron);
        setHardness(5);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("rngQuarry");
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if(!worldIn.isRemote) {
            placer.addChatMessage(new ChatComponentTranslation("text.japta.rngQuarry.noTool", 0));
        }
        return getDefaultState();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer p, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) return true;
        TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
        ItemStack held = p.getHeldItem();
        if(held == null) {
            if(te.item != null) {
                p.addChatComponentMessage(new ChatComponentTranslation("text.japta.rngQuarry.hasTool", te.item.getChatComponent(), te.stored));
            }
            else {
                p.addChatComponentMessage(new ChatComponentTranslation("text.japta.rngQuarry.noTool", te.stored));
            }
        }
        else {
            ItemStack itm = te.item;
            te.item = p.inventory.removeStackFromSlot(p.inventory.currentItem);
            p.addChatComponentMessage(new ChatComponentTranslation("text.japta.rngQuarry.gotTool", te.item.getChatComponent()));
            if(itm != null) {
                p.inventory.setInventorySlotContents(p.inventory.currentItem, itm);
            }
        }
        return true;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        super.onBlockClicked(world, pos, player);
        if(player.isSneaking()) {
            TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
            if(te.item != null) {
                if(player.inventory.addItemStackToInventory(te.item)) {
                    te.item = null;
                }
            }
        }
    }

    @Override
    public int getRenderType() {
        return 3;
    }
}
