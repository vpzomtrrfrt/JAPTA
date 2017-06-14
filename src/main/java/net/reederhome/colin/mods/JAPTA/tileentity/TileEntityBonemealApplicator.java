package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;

import java.util.Random;

public class TileEntityBonemealApplicator extends TileEntityJPT implements IEnergyReceiver, ITickable, IDiagnosable {
    public static int USE = 100;
    public static int RANGE = 4;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 1000;
    }

    @Override
    public void update() {
        if (stored >= USE && !getWorld().isRemote && getWorld().isBlockIndirectlyGettingPowered(getPos()) == 0) {
            for (EnumFacing side : EnumFacing.VALUES) {
                TileEntity te = getWorld().getTileEntity(getPos().offset(side));
                if (te instanceof IInventory) {
                    IInventory inv = (IInventory) te;
                    for (int i = 0; i < inv.getSizeInventory(); i++) {
                        ItemStack stack = inv.getStackInSlot(i);
                        if (ItemStackTools.isValid(stack) && stack.getItem() == Items.DYE && stack.getItemDamage() == 15) {
                            for (int t = 0; t < 3; t++) { // try thrice for a valid spot
                                BlockPos cp = getPos().add(new Random().nextInt(RANGE * 2) - RANGE, RANGE, new Random().nextInt(RANGE * 2) - RANGE);
                                while (cp.getY() >= 0) {
                                    IBlockState state = getWorld().getBlockState(cp);
                                    if (state.getBlock() instanceof IGrowable) {
                                        IGrowable bl = (IGrowable) state.getBlock();
                                        if (bl != Blocks.GRASS && bl.canGrow(getWorld(), cp, state, false) && bl.canUseBonemeal(getWorld(), new Random(), cp, state)) {
                                            bl.grow(getWorld(), new Random(), cp, state);
                                            //stack.stackSize--;
                                            ItemStackTools.incStackSize(stack, -1);
                                            stored -= USE;
                                            return;
                                        }
                                    }
                                    cp = cp.down();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        boolean bonemeal = false;
        dancing:
        for (EnumFacing side : EnumFacing.VALUES) {
            TileEntity te = getWorld().getTileEntity(getPos().offset(side));
            if (te instanceof IInventory) {
                IInventory inv = (IInventory) te;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (ItemStackTools.isValid(stack) && stack.getItem() == Items.DYE && stack.getItemDamage() == 15) {
                        bonemeal = true;
                        break dancing;
                    }
                }
            }
        }
        int redstone = 0;
        if(getWorld() instanceof World) {
            redstone = ((World) getWorld()).isBlockIndirectlyGettingPowered(pos);
        }
        if (!bonemeal) {
            sender.sendMessage(new TextComponentTranslation("tile.bonemealApplicator.diagnostic.noBonemeal"));
            return true;
        } else {
            if (redstone > 0) {
                sender.sendMessage(new TextComponentTranslation("tile.bonemealApplicator.diagnostic.redstone"));
                return true;
            }
        }
        return false;
    }
}
