package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityEater;

import javax.annotation.Nullable;

public class BlockEater extends BlockModelContainer {

    public BlockEater() {
        super(Material.ROCK);
        setCreativeTab(JAPTA.tab);
        setHardness(1);
        setUnlocalizedName("eater");
        setRegistryName("eater");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityEater();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack p_onBlockActivated_6_, EnumFacing p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_, float p_onBlockActivated_10_) {
        if(world.isRemote) return true;
        TileEntityEater te = ((TileEntityEater) world.getTileEntity(pos));
        ItemStack item = te.getStackInSlot(0);
        if(item != null) {
            player.addChatComponentMessage(new TextComponentTranslation("text.japta.eater.hasItem", item.getTextComponent(), te.getProgress()*100/TileEntityEater.TIME));
        }
        else {
            ItemStack held = player.getHeldItem(hand);
            if(te.isItemValidForSlot(0, held)) {
                te.setInventorySlotContents(0, held.splitStack(1));
            }
            else {
                player.addChatComponentMessage(new TextComponentTranslation("text.japta.eater.noItem"));
            }
        }
        return true;
    }
}
