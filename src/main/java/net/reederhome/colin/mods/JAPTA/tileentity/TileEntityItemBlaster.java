package net.reederhome.colin.mods.JAPTA.tileentity;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockBlaster;
import net.reederhome.colin.mods.JAPTA.block.BlockItemBlaster;

import java.util.Arrays;

public class TileEntityItemBlaster extends TileEntity implements IInventory, ITickable {

    private ItemStack[] inv;
    private int pos = 1;

    public TileEntityItemBlaster() {
        super();
        clear();
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public boolean func_191420_l() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inv[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack sis = inv[index];
        if (ItemStackTools.getStackSize(sis) <= count) {
            inv[index] = ItemStack.field_190927_a;
            return sis;
        } else {
            return sis.splitStack(count);
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack sis = inv[index];
        inv[index] = ItemStack.field_190927_a;
        return sis;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inv[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        inv = new ItemStack[getSizeInventory()];
        Arrays.fill(inv, ItemStack.field_190927_a);
    }

    @Override
    public String getName() {
        return getWorld().getBlockState(getPos()).getBlock().getUnlocalizedName() + ".name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        NBTTagList l = new NBTTagList();
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack sis = getStackInSlot(i);
            if (ItemStackTools.isValid(sis)) {
                NBTTagCompound nbt = new NBTTagCompound();
                sis.writeToNBT(nbt);
                nbt.setByte("Slot", (byte) i);
                l.appendTag(nbt);
            }
        }
        tag.setTag("Items", l);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList l = (NBTTagList) tag.getTag("Items");
        for (int i = 0; i < l.tagCount(); i++) {
            NBTTagCompound nbt = l.getCompoundTagAt(i);
            inv[nbt.getByte("Slot")] = new ItemStack(nbt);
        }
    }

    private boolean attemptInhale(IInventory inv, int j) {
        ItemStack stack = inv.getStackInSlot(j);
        ItemStack copy = stack.copy();
        ItemStack stack1 = stack.splitStack(1);
        ItemStack remainder = TileEntityHopper.putStackInInventoryAllSlots(inv, this, stack1, null);
        if (ItemStackTools.isValid(remainder)) {
            inv.setInventorySlotContents(j, copy);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        IBlockState state = getWorld().getBlockState(getPos());
        EnumFacing facing = JAPTA.safeGetValue(state, BlockBlaster.FACING);
        boolean placedItem = false;
        if (!((BlockItemBlaster) state.getBlock()).isSplitting()) {
            pos = 1;
        } else {
            pos++;
            if (pos > BlockBlaster.RANGE) pos = 1;
        }
        int initialPos = pos;
        boolean once = false;
        while (!once || pos != initialPos) {
            once = true;
            BlockPos cp = getPos().offset(facing, pos);
            while (getWorld().getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                cp = cp.up();
            }
            TileEntity te = getWorld().getTileEntity(cp);
            if (te instanceof IInventory) {
                IInventory ci = (IInventory) te;
                if (((BlockBlaster) state.getBlock()).isInhaler()) {
                    if (!placedItem) {
                        for (EnumFacing side : EnumFacing.values()) {
                            if (side != facing) {
                                ItemStack fs = getFirstStack(false);
                                if (ItemStackTools.isValid(fs)) {
                                    TileEntity cte = getWorld().getTileEntity(getPos().offset(side));
                                    if (cte instanceof IInventory) {
                                        ItemStack ret = TileEntityHopper.putStackInInventoryAllSlots(((IInventory) cte), this, fs.splitStack(1), side.getOpposite());
                                        if (ItemStackTools.isValid(ret)) {
//                                            fs.stackSize++;
                                            ItemStackTools.incStackSize(fs, 1);
                                        } else {
                                            getFirstStack(false);
                                            placedItem = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (ci instanceof ISidedInventory) {
                        int[] slots = ((ISidedInventory) ci).getSlotsForFace(facing.getOpposite());
                        for (int slot : slots) {
                            ItemStack stack = ci.getStackInSlot(slot);
                            if (ItemStackTools.isValid(stack) && ((ISidedInventory) ci).canExtractItem(slot, stack, facing.getOpposite()) && attemptInhale(ci, slot)) {
                                break;
                            }
                        }
                    } else {
                        for (int j = 0; j < ci.getSizeInventory(); j++) {
                            if (attemptInhale(ci, j)) {
                                break;
                            }
                        }
                    }
                } else {
                    ItemStack fs = getFirstStack(false);
                    if (ItemStackTools.isValid(fs)) {
                        ItemStack ret = TileEntityHopper.putStackInInventoryAllSlots(this, ci, fs.splitStack(1), facing);
                        if (ItemStackTools.isValid(ret)) {
                            //fs.stackSize++;
                            ItemStackTools.incStackSize(fs, 1);
                        } else {
                            return;
                        }
                    }
                }
            }
            pos++;
            if (pos > BlockBlaster.RANGE) {
                pos = 1;
            }
        }
    }

    private ItemStack getFirstStack(boolean remove) {
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack sis = getStackInSlot(i);
            if (ItemStackTools.isValid(sis)) {
                if (remove) {
                    removeStackFromSlot(i);
                }
                return sis;
            }
        }
        return null;
    }
}
