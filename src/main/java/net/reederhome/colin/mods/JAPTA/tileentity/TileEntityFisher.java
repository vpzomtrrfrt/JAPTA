package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;

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
        if(worldObj.isRemote) return;
        if(stored >= USE) {
            LootTable table = worldObj.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING);
            LootContext.Builder builder = new LootContext.Builder(((WorldServer) worldObj));
            List<ItemStack> items = table.generateLootForPools(new Random(), builder.build());
            if(!items.isEmpty()) {
                stored -= USE;
                ItemStack stack = items.get(0);
                for(EnumFacing side : EnumFacing.values()) {
                    TileEntity te = worldObj.getTileEntity(getPos().offset(side));
                    if(te instanceof IInventory) {
                        stack = TileEntityHopper.putStackInInventoryAllSlots(((IInventory) te), stack, side.getOpposite());
                    }
                }
                if(stack != null && stack.stackSize > 0) {
                    InventoryHelper.spawnItemStack(worldObj, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, stack);
                }
            }
            else {
                System.out.println("No item??");
            }
        }
    }
}
