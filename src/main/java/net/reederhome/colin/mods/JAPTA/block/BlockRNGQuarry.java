package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityRNGQuarry;

public class BlockRNGQuarry extends BlockModelContainer {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRNGQuarry();
    }

    public BlockRNGQuarry() {
        super(Material.IRON);
        setHardness(5);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("rngQuarry");
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if(!worldIn.isRemote) {
            placer.addChatMessage(new TextComponentTranslation("text.japta.rngQuarry.noTool", 0));
        }
        return getDefaultState();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer p, EnumHand hand, ItemStack stack, EnumFacing p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_, float p_onBlockActivated_10_) {
        if(world.isRemote) return true;
        TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
        ItemStack held = p.inventory.getCurrentItem();
        if(held == null) {
            if(te.item != null) {
                p.addChatComponentMessage(new TextComponentTranslation("text.japta.rngQuarry.hasTool", te.item.getTextComponent(), te.stored));
            }
            else {
                p.addChatComponentMessage(new TextComponentTranslation("text.japta.rngQuarry.noTool", te.stored));
            }
        }
        else {
            ItemStack itm = te.item;
            te.item = p.inventory.removeStackFromSlot(p.inventory.currentItem);
            p.addChatComponentMessage(new TextComponentTranslation("text.japta.rngQuarry.gotTool", te.item.getTextComponent()));
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
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
        if(te.item != null) {
            InventoryHelper.spawnItemStack(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, te.item);
            te.item = null;
        }
        super.breakBlock(world, pos, state);
    }
}
