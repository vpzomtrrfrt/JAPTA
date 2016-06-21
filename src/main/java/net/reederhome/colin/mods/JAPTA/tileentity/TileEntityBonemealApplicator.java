package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
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
        if(stored >= USE && !worldObj.isRemote && worldObj.isBlockIndirectlyGettingPowered(getPos()) == 0) {
            for(EnumFacing side : EnumFacing.VALUES) {
                TileEntity te = worldObj.getTileEntity(getPos().offset(side));
                if(te instanceof IInventory) {
                    IInventory inv = (IInventory) te;
                    for(int i = 0; i < inv.getSizeInventory(); i++) {
                        ItemStack stack = inv.getStackInSlot(i);
                        if(stack != null && stack.getItem() == Items.DYE && stack.getItemDamage() == 15 && stack.stackSize > 0) {
                            for(int t = 0; t < 3; t++) { // try thrice for a valid spot
                                BlockPos cp = getPos().add(new Random().nextInt(RANGE * 2) - RANGE, RANGE, new Random().nextInt(RANGE * 2) - RANGE);
                                while (cp.getY() >= 0) {
                                    IBlockState state = worldObj.getBlockState(cp);
                                    if (state.getBlock() instanceof IGrowable) {
                                        IGrowable bl = (IGrowable) state.getBlock();
                                        if (bl != Blocks.GRASS && bl.canGrow(worldObj, cp, state, false) && bl.canUseBonemeal(worldObj, new Random(), cp, state)) {
                                            bl.grow(worldObj, new Random(), cp, state);
                                            stack.stackSize--;
                                            stored -= USE;
                                            if (stack.stackSize == 0) {
                                                inv.setInventorySlotContents(i, null);
                                            }
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
        for(EnumFacing side : EnumFacing.VALUES) {
            TileEntity te = worldObj.getTileEntity(getPos().offset(side));
            if (te instanceof IInventory) {
                IInventory inv = (IInventory) te;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (stack != null && stack.getItem() == Items.DYE && stack.getItemDamage() == 15 && stack.stackSize > 0) {
                        bonemeal = true;
                        break dancing;
                    }
                }
            }
        }
        int redstone = worldObj.isBlockIndirectlyGettingPowered(pos);
        if(!bonemeal) {
            sender.addChatMessage(new TextComponentTranslation("tile.bonemealApplicator.diagnostic.noBonemeal"));
            return true;
        }
        else {
            if(redstone > 0) {
                sender.addChatMessage(new TextComponentTranslation("tile.bonemealApplicator.diagnostic.redstone"));
                return true;
            }
        }
        return false;
    }
}
