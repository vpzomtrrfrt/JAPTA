package net.reederhome.colin.mods.JAPTA.block;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
        setRegistryName("rngQuarry");
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack p_onBlockPlacedBy_5_) {
        super.onBlockPlacedBy(world, pos, state, placer, p_onBlockPlacedBy_5_);
        if(world.isRemote) return;
        placer.sendMessage(new TextComponentTranslation("text.japta.rngQuarry.noTool", 0));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer p, EnumHand hand, EnumFacing p_onBlockActivated_6_, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
        if (world.isRemote) return true;
        TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
        ItemStack held = p.getHeldItem(hand);
        if (!ItemStackTools.isValid(held)) {
            if (ItemStackTools.isValid(te.item)) {
                p.sendMessage(new TextComponentTranslation("text.japta.rngQuarry.hasTool", te.item.getTextComponent(), te.stored));
            } else {
                p.sendMessage(new TextComponentTranslation("text.japta.rngQuarry.noTool", te.stored));
            }
        } else {
            ItemStack itm = te.item;
            te.item = p.inventory.removeStackFromSlot(p.inventory.currentItem);
            p.sendMessage(new TextComponentTranslation("text.japta.rngQuarry.gotTool", te.item.getTextComponent()));
            if (ItemStackTools.isValid(itm)) {
                p.inventory.setInventorySlotContents(p.inventory.currentItem, itm);
            }
        }
        return true;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        super.onBlockClicked(world, pos, player);
        if (player.isSneaking()) {
            TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
            if (ItemStackTools.isValid(te.item)) {
                if (player.inventory.addItemStackToInventory(te.item)) {
                    te.item = ItemStack.EMPTY;
                }
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityRNGQuarry te = (TileEntityRNGQuarry) world.getTileEntity(pos);
        if (ItemStackTools.isValid(te.item)) {
            InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, te.item);
            te.item = ItemStack.EMPTY;
        }
        super.breakBlock(world, pos, state);
    }
}
