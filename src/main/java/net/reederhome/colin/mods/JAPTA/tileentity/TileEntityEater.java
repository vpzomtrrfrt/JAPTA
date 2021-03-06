package net.reederhome.colin.mods.JAPTA.tileentity;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityEater extends TileEntityJPT implements TileEntityJPT.EnergyProvider, ITickable, IInventory {

    private ItemStack item;
    private byte progress;
    public static int TIME = 64;
    public static int MULTIPLIER = 100;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return MULTIPLIER * 20;
    }

    @Override
    public void update() {
        if (getWorld().isRemote) return;
        if (progress >= TIME) {
            int value = getPowerValue();
            stored += value;
            if (ItemStackTools.isValid(item)) {
                //item.stackSize--;
                item = ItemStackTools.incStackSize(item, -1);
            }
            progress = 0;
        }
        if (progress == 0) {
            int value = getPowerValue();
            if (value > 0) {
                progress++;
            }
        } else if (progress > 0) {
            progress++;
            BlockPos pos = getPos();
            if (getWorld().getTotalWorldTime() % 2 == 0)
                getWorld().playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.5f + 0.5f * new Random().nextInt(2), (float) ((Math.random() - Math.random()) * 0.2 + 1));
        }
        if (stored > 0) {
            transmit();
        }
    }

    private int getPowerValue() {
        if (ItemStackTools.isValid(item)) {
            Item type = item.getItem();
            if (type instanceof ItemFood) {
                ItemFood food = ((ItemFood) type);
                return food.getHealAmount(item) * MULTIPLIER;
            }
        }
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return !ItemStackTools.isValid(item);
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int i) {
        return item;
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int i, int i1) {
        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int i, @Nullable ItemStack itemStack) {
        item = itemStack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemFood;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        item = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "tile.eater.name";
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
        if (ItemStackTools.isValid(item)) {
            NBTTagCompound nbt = new NBTTagCompound();
            item.writeToNBT(nbt);
            tag.setTag("Item", nbt);
        }
        tag.setByte("Progress", progress);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("Item")) {
            item = new ItemStack(tag.getCompoundTag("Item"));
        }
        progress = tag.getByte("Progress");
    }

    public int getProgress() {
        return progress;
    }
}
