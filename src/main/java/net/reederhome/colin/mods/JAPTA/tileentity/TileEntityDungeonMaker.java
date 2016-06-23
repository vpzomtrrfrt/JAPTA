package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.storage.loot.LootTableList;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;

import java.util.Random;

public class TileEntityDungeonMaker extends TileEntityJPT implements IEnergyReceiver, ITickable, IDiagnosable {

    public static int USE = 1000000;
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return USE;
    }

    @Override
    public void update() {
        if(worldObj.isRemote) return;
        if(stored >= USE) {
            TileEntityChest chest = findEmptyChest();
            if(chest != null) {
                chest.setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, new Random().nextLong());
                stored -= USE;
            }
        }
    }

    private TileEntityChest findEmptyChest() {
        for(EnumFacing side : EnumFacing.values()) {
            BlockPos tr = getPos().offset(side);
            TileEntity te = worldObj.getTileEntity(tr);
            if(te instanceof TileEntityChest) {
                TileEntityChest chest = ((TileEntityChest) te);
                if(isEmpty(chest)) return chest;
            }
        }
        return null;
    }

    private boolean isEmpty(IInventory inv) {
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            if(inv.getStackInSlot(i) != null) return false;
        }
        return true;
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        sender.addChatMessage(new TextComponentTranslation("tile.dungeonMaker.diagnostic."+(findEmptyChest()==null?"noChest":"yesChest")));
        return true;
    }
}