package net.reederhome.colin.mods.JAPTA.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

public class ItemPoweredMultiTool extends ItemJPT {

    private ToolMaterial getMaterial(ItemStack stack, String type) {
        NBTTagCompound materials = getMaterialsTag(stack);
        if (materials != null && materials.hasKey(type)) {
            return ToolMaterial.valueOf(materials.getString(type));
        }
        return ToolMaterial.WOOD;
    }

    private NBTTagCompound getMaterialsTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            NBTTagCompound materials = tag.getCompoundTag("Materials");
            if (materials != null) {
                return materials;
            }
        }
        return null;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String type) {
        if (!isDead(stack)) {
            return getMaterial(stack, type).getHarvestLevel();
        }
        return 0;
    }

    private boolean isDead(ItemStack stack) {
        return stack.getItemDamage() >= stack.getMaxDamage();
    }

    @Override
    public float getDigSpeed(ItemStack stack, IBlockState state) {
        if (!isDead(stack)) {
            ToolMaterial material = getMaterial(stack, state.getBlock().getHarvestTool(state));
            if (material.getHarvestLevel() >= state.getBlock().getHarvestLevel(state)) {
                return material.getEfficiencyOnProperMaterial();
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        NBTTagCompound materials = getMaterialsTag(stack);
        int tr = 0;
        for (String type : materials.getKeySet()) {
            tr += getMaterial(stack, type).getMaxUses();
        }
        return tr;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean p_addInformation_4_) {
        NBTTagCompound materials = getMaterialsTag(stack);
        if (materials != null) {
            for (String type : materials.getKeySet()) {
                list.add(materials.getString(type) + " " + type);
            }
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int layer) {
        String[] layerTools = {"no", "sword", "pickaxe", "axe", "hoe", "shovel"};
        if (layerTools[layer].equals("no")) {
            return 0xFFFFFF;
        } else {
            ToolMaterial material = getMaterial(stack, layerTools[layer]);
            if (material != null) {
                switch (material) {
                    case WOOD:
                        return 0x866526;
                    case STONE:
                        return 0x9a9a9a;
                    case IRON:
                        return 0xffffff;
                    case GOLD:
                        return 0xeaee57;
                    case EMERALD:
                        return 0x33ebcb;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player) {
        if (!isDead(stack)) {
            stack.damageItem(1, player);
        }
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!isDead(stack)) {
            stack.damageItem(1, attacker);
        }
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        int tr = 0;
        NBTTagCompound materials = getMaterialsTag(stack);
        if (materials != null) {
            for (String type : materials.getKeySet()) {
                tr += getMaterial(stack, type).getEnchantability();
            }
        }
        return tr;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        Multimap<String, AttributeModifier> tr = super.getAttributeModifiers(stack);
        tr.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", getMaterial(stack, "sword").getDamageVsEntity()+4, 0));
        return tr;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer p_onItemUse_2_, World p_onItemUse_3_, BlockPos p_onItemUse_4_, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_) {
        if(!isDead(stack)) {
            return Items.diamond_hoe.onItemUse(stack, p_onItemUse_2_, p_onItemUse_3_, p_onItemUse_4_, p_onItemUse_5_, p_onItemUse_6_, p_onItemUse_7_, p_onItemUse_8_);
        }
        return false;
    }
}