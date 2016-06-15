package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockBlaster;

public class TileEntityItemBlaster extends TileEntity implements IInventory, ITickable {

    private ItemStack[] inv;

    public TileEntityItemBlaster() {
        super();
        clear();
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inv[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack sis = inv[index];
        if (sis.stackSize <= count) {
            inv[index] = null;
            return sis;
        } else {
            return sis.splitStack(count);
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack sis = inv[index];
        inv[index] = null;
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
    }

    @Override
    public String getName() {
        return "tile.itemBlaster.name";
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
            if (sis != null) {
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
            inv[nbt.getByte("Slot")] = ItemStack.loadItemStackFromNBT(nbt);
        }
    }

    private boolean attemptInhale(IInventory inv, int j) {
        ItemStack stack = inv.getStackInSlot(j);
        ItemStack copy = stack.copy();
        ItemStack stack1 = stack.splitStack(1);
        ItemStack remainder = TileEntityHopper.putStackInInventoryAllSlots(this, stack1, null);
        if(remainder != null && remainder.stackSize > 0) {
            inv.setInventorySlotContents(j, copy);
        }
        else {
            if(stack.stackSize == 0) {
                inv.setInventorySlotContents(j, null);
            }
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        IBlockState state = worldObj.getBlockState(getPos());
        EnumFacing facing = JAPTA.safeGetValue(state, BlockBlaster.FACING);
        boolean placedItem = false;
        for (int i = 1; i <= BlockBlaster.RANGE; i++) {
            BlockPos cp = getPos().offset(facing, i);
            while (worldObj.getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                cp = cp.up();
            }
            TileEntity te = worldObj.getTileEntity(cp);
            if (te instanceof IInventory) {
                IInventory ci = (IInventory) te;
                if(((BlockBlaster) state.getBlock()).isInhaler()) {
                    if(!placedItem) {
                        for (EnumFacing side : EnumFacing.values()) {
                            if (side != facing) {
                                ItemStack fs = getFirstStack(false);
                                if (fs != null) {
                                    TileEntity cte = worldObj.getTileEntity(getPos().offset(side));
                                    if (cte instanceof IInventory) {
                                        ItemStack ret = TileEntityHopper.putStackInInventoryAllSlots(((IInventory) cte), fs.splitStack(1), side.getOpposite());
                                        if (ret != null) {
                                            fs.stackSize++;
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
                    if(ci instanceof ISidedInventory) {
                        int[] slots = ((ISidedInventory) ci).getSlotsForFace(facing.getOpposite());
                        for(int slot : slots) {
                            ItemStack stack = ci.getStackInSlot(slot);
                            if(stack != null && ((ISidedInventory) ci).canExtractItem(slot, stack, facing.getOpposite()) && attemptInhale(ci, slot)) {
                                break;
                            }
                        }
                    }
                    else {
                        for (int j = 0; j < ci.getSizeInventory(); j++) {
                            if(attemptInhale(ci, j)) {
                                break;
                            }
                        }
                    }
                }
                else {
                    ItemStack fs = getFirstStack(false);
                    if (fs != null) {
                        ItemStack ret = TileEntityHopper.putStackInInventoryAllSlots(ci, fs.splitStack(1), facing);
                        if (ret != null) {
                            fs.stackSize++;
                        } else {
                            getFirstStack(false); // clear out the zero if it's there
                            return;
                        }
                    }
                }
            }
        }
    }

    private ItemStack getFirstStack(boolean remove) {
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack sis = getStackInSlot(i);
            if (sis != null) {
                if (sis.stackSize < 1) {
                    removeStackFromSlot(i);
                    continue;
                }
                if (remove) {
                    removeStackFromSlot(i);
                }
                return sis;
            }
        }
        return null;
    }
}
