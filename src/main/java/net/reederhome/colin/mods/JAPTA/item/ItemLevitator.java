package net.reederhome.colin.mods.JAPTA.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public class ItemLevitator extends ItemJPT implements ITickableItem {
    public static int USE = 20;
    public static int MAX_HELD = 50000;
    public ItemLevitator() {
        setCreativeTab(JAPTA.tab);
        setRegistryName("levitator");
        setUnlocalizedName("levitator");
        setMaxDamage(MAX_HELD);
    }

    @Override
    public void update(ItemStack stack, EntityPlayer player) {
        if(getEnergyStored(stack) > USE) {
            int height = 0;
            NBTTagCompound tag = stack.getTagCompound();
            if(tag != null && tag.hasKey("Height")) {
                height = tag.getInteger("Height");
            }
            BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ).down();
            int i;
            for(i = 0; i < height; i++) {
                BlockPos cp = pos.down(i);
                IBlockState state = player.getEntityWorld().getBlockState(cp);
                if(!state.getBlock().isAir(state, player.getEntityWorld(), cp)) {
                    break;
                }
            }
            if(i < height && player.motionY < 0.21) {
                player.addVelocity(0, player.motionY>-0.2?(player.motionY<.15?0.1:0.05):0.2, 0);
                if(player.motionY > 0) {
                    player.fallDistance = 0;
                }
                setEnergyStored(stack, getEnergyStored(stack)-USE);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        int height = 0;
        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null && tag.hasKey("Height")) {
            height = tag.getInteger("Height");
        }
        int diff = 0;
        if(player.isSneaking()) {
            if(height > 0) {
                diff--;
            }
        }
        else {
            diff++;
        }
        if(diff != 0) {
            height += diff;
            player.addChatComponentMessage(new TextComponentTranslation("text.japta.levitator.heightChange", height), false);
            if(tag == null) {
                tag = new NBTTagCompound();
                stack.setTagCompound(tag);
            }
            tag.setInteger("Height", height);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
