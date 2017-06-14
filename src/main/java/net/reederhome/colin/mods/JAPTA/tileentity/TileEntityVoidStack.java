package net.reederhome.colin.mods.JAPTA.tileentity;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.BlockChest;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TileEntityVoidStack extends TileEntity implements IInventory, ITickable, IDiagnosable {

    private static final int SIZE = 9;
    private List<ItemStack> items = new ArrayList<ItemStack>();
    private String customName;
    private ItemStack[] pending = new ItemStack[SIZE];

    @Override
    public int getSizeInventory() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        return items.size() < 1;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int i) {
        return pending[i];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int i, int i1) {
        ItemStack stack = getStackInSlot(i);
        ItemStack lvt_3_1_ = stack.splitStack(i1);

        markDirty();
        return lvt_3_1_;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int i) {
        markDirty();
        ItemStack stack = pending[i];
        pending[i] = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int i, @Nullable ItemStack itemStack) {
        pending[i] = itemStack;
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
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
        items.clear();
        Arrays.fill(pending, ItemStack.EMPTY);
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : "tile.voidStack.name";
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        if (hasCustomName()) {
            return new TextComponentString(customName);
        }
        return new TextComponentTranslation(getName());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        if (hasCustomName()) {
            tag.setString("CustomName", customName);
        }
        NBTTagList itemList = new NBTTagList();
        for (ItemStack stack : items) {
            itemList.appendTag(stack.writeToNBT(new NBTTagCompound()));
        }
        for(int i = 0; i < SIZE; i++) {
            ItemStack stack = pending[i];
            if(ItemStackTools.isValid(stack)) {
                itemList.appendTag(stack.writeToNBT(new NBTTagCompound()));
            }
        }
        tag.setTag("Items", itemList);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("CustomName")) {
            customName = tag.getString("CustomName");
        }
        NBTTagList itemList = (NBTTagList) tag.getTag("Items");
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound nbt = itemList.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(nbt);
            items.add(stack);
        }
        update();
    }

    @Override
    public void update() {
        for(int i = 0; i < SIZE; i++) {
            ItemStack stack = pending[i];
            if(ItemStackTools.isValid(stack)) {
                items.add(stack);
                pending[i] = ItemStack.EMPTY;
            }
        }
        if(items.size() > 0) {
            pending[0] = items.remove(items.size() - 1);
        }
    }

    public void dropItems() {
        InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
        pending = new ItemStack[SIZE];
        while(items.size() > 0) {
            ItemStack stack = items.remove(0);
            InventoryHelper.spawnItemStack(getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    public TileEntityVoidStack() {
        clear();
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        sender.sendMessage(new TextComponentTranslation("tile.voidStack.diagnostic", items.size()));
        return true;
    }
}
