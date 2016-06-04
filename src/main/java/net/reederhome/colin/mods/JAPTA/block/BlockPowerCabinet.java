package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityPowerCabinetBase;

import java.util.List;

public class BlockPowerCabinet extends Block implements IDiagnosable {
    public static final PropertyInteger VALUE = PropertyInteger.create("value", 0, 15);
    public static int MAX_META_VALUE;

    private int metaValue;

    public BlockPowerCabinet(int metaValue) {
        super(Material.ROCK);
        setDefaultState(blockState.getBaseState().withProperty(VALUE, 0));
        setCreativeTab(JAPTA.tab);
        this.metaValue = metaValue;
        if(metaValue > MAX_META_VALUE) {
            MAX_META_VALUE = metaValue;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VALUE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VALUE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VALUE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 15));
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        sender.addChatMessage(new TextComponentTranslation("tile.powerCabinet.diagnostic", getMetaValue()*state.getValue(VALUE)));
        BlockPos cp = pos.down();
        while(true) {
            IBlockState cs = world.getBlockState(cp);
            if(!(cs.getBlock() instanceof BlockPowerCabinet)) {
                break;
            }
            else {
                cp = cp.down();
            }
        }
        TileEntity te = world.getTileEntity(cp);
        if(te instanceof TileEntityPowerCabinetBase) {
            sender.addChatMessage(new TextComponentTranslation("tile.powerCabinet.diagnostic2", ((TileEntityPowerCabinetBase) te).getEnergyStored(null)));
        }
        else {
            sender.addChatMessage(new TextComponentTranslation("tile.powerCabinet.diagnostic3"));
        }
        return true;
    }

    public int getMetaValue() {
        return metaValue;
    }
}
