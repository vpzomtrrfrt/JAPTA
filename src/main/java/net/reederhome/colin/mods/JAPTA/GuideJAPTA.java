package net.reederhome.colin.mods.JAPTA;

import amerifrance.guideapi.api.GuideAPIItems;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.base.EntryBase;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuideJAPTA {
    public static Book book;

    public static void build() {
        List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
        List<EntryAbstract> blockEntries = new ArrayList<EntryAbstract>();
        blockEntries.add(buildEntry(JAPTA.rngQuarry));
        categories.add(new CategoryItemStack(blockEntries, "text.japta.guide.category.blocks", new ItemStack(JAPTA.rngQuarry)));
        book = new Book();
        book.setAuthor("vpzom");
        book.setCategoryList(categories);
        book.setColor(Color.black);
        book.setTitle(I18n.format("text.japta.guide.title"));
        book.setWelcomeMessage(I18n.format("text.japta.guide.title"));
        GuideRegistry.registerBook(book, null);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            ModelLoader.setCustomModelResourceLocation(GuideAPIItems.guideBook, GuideRegistry.getIndexOf(book), new ModelResourceLocation(new ResourceLocation("guideapi", "ItemGuideBook"), "type=book"));
    }

    private static EntryAbstract buildEntry(Item item) {
        return buildEntry(item.getUnlocalizedName() + ".name",
                PageHelper.pagesForLongText(I18n.format(item.getUnlocalizedName() + ".entry"), item));
    }

    private static EntryAbstract buildEntry(Block block) {
        return buildEntry(Item.getItemFromBlock(block));
    }

    private static EntryAbstract buildEntry(String unlocName, Object... pages) {
        List<IPage> realPages = new ArrayList<IPage>();
        for (Object o : pages) {
            if (o instanceof Collection) {
                realPages.addAll((Collection<? extends IPage>) o);
            } else if (o instanceof IPage) {
                realPages.add((IPage) o);
            } else if (o instanceof IRecipe) {
                realPages.add(new PageIRecipe((IRecipe) o));
            }
        }
        return new EntryBase(realPages, unlocName);
    }
}