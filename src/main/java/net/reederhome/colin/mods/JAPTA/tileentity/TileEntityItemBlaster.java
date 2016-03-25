package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
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
    private int cooldown = 0;

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
        if(sis.stackSize <= count) {
            inv[index] = null;
            return sis;
        }
        else {
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

    public TileEntityItemBlaster() {
        super();
        clear();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList l = new NBTTagList();
        for(int i = 0; i < getSizeInventory(); i++) {
            ItemStack sis = getStackInSlot(i);
            if(sis != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                sis.writeToNBT(nbt);
                nbt.setByte("Slot", (byte)i);
                l.appendTag(nbt);
            }
        }
        tag.setTag("Items", l);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList l = (NBTTagList) tag.getTag("Items");
        for(int i = 0; i < l.tagCount(); i++) {
            NBTTagCompound nbt = l.getCompoundTagAt(i);
            inv[nbt.getByte("Slot")] = ItemStack.loadItemStackFromNBT(nbt);
        }
    }

    @Override
    public void update() {
        if(cooldown <= 0) {
            EnumFacing facing = JAPTA.safeGetValue(worldObj.getBlockState(getPos()), BlockBlaster.FACING);
            for (int i = 1; i <= BlockBlaster.RANGE; i++) {
                BlockPos cp = getPos().offset(facing, i);
                while (worldObj.getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                    cp = cp.up();
                }
                TileEntity te = worldObj.getTileEntity(cp);
                if (te instanceof IInventory) {
                    IInventory ci = (IInventory) te;
                    ItemStack fs = getFirstStack(false);
                    if(fs != null) {
                        ItemStack ret = TileEntityHopper.putStackInInventoryAllSlots(ci, fs.splitStack(1), facing);
                        if (ret != null) {
                            fs.stackSize++;
                        }
                        else {
                            getFirstStack(false); // clear out the zero if it's there
                            cooldown = 8;
                            return;
                        }
                    }
                }
            }
        }
        else {
            cooldown--;
        }
    }

    private ItemStack getFirstStack(boolean remove) {
        for(int i = 0; i < getSizeInventory(); i++) {
            ItemStack sis = getStackInSlot(i);
            if(sis != null) {
                if(sis.stackSize < 1) {
                    removeStackFromSlot(i);
                    continue;
                }
                if(remove) {
                    removeStackFromSlot(i);
                }
                return sis;
            }
        }
        return null;
    }
}
