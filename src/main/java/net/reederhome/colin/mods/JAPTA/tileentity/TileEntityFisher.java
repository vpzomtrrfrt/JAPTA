package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.List;
import java.util.Random;

public class TileEntityFisher extends TileEntityJPT implements IEnergyReceiver, ITickable {
    public static int USE = 30000;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 100000;
    }

    @Override
    public void update() {
        if (getWorld().isRemote) return;
        if (stored >= USE) {
            LootTable table = getWorld().getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING);
            LootContext.Builder builder = new LootContext.Builder(((WorldServer) getWorld()));
            List<ItemStack> items = table.generateLootForPools(new Random(), builder.build());
            if (!items.isEmpty()) {
                stored -= USE;
                ItemStack stack = items.get(0);
                for (EnumFacing side : EnumFacing.values()) {
                    TileEntity te = getWorld().getTileEntity(getPos().offset(side));
                    if (te instanceof IInventory) {
                        stack = TileEntityHopper.putStackInInventoryAllSlots(null, ((IInventory) te), stack, side.getOpposite());
                    }
                }
                if (ItemStackTools.isValid(stack)) {
                    InventoryHelper.spawnItemStack(getWorld(), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
                }
            } else {
                System.out.println("No item??");
            }
        }
    }
}
