package net.reederhome.colin.mods.JAPTA;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.base.EntryBase;
import amerifrance.guideapi.api.util.BookBuilder;
import amerifrance.guideapi.categories.CategoryItemStack;
import amerifrance.guideapi.pages.PageIRecipe;
import amerifrance.guideapi.pages.PageItemStack;
import amerifrance.guideapi.pages.PageText;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class GuideJAPTA {
	public static Book book;
	public static void build() {
		List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
		List<EntryAbstract> entries = new ArrayList<EntryAbstract>();
		entries.add(buildEntry("RNG Quarry",
				pagesForLongText("The RNG Quarry will mine blocks in a chunk-sized area below it using the supplied tool (provided it has RF to run).  Simply right-click it to give it a tool. If it has one already, that will be dropped.  The tool's durability will go down for every block not mineable by a wooden tool.  Any items mined will be placed in nearby inventories (like chests) or simply spewed.", JAPTA.rngQuarry),
				JAPTA.getRecipe(JAPTA.rngQuarry)));
		entries.add(buildEntry("Charging Plate",
				pagesForLongText("The Charging Plate is a pressure plate only activated by players, and charges their items if supplied with RF. It also comes in a Wooden variety, which is activated by anything and can charge items dropped on it.", JAPTA.chargingPlate),
				JAPTA.getRecipe(JAPTA.chargingPlate),
				JAPTA.getRecipe(JAPTA.chargingPlateWooden)));
		entries.add(buildEntry("Life Converter",
				pagesForLongText("The Life Converter has two purposes, but it can only do one at a time. Right-click it to switch between them.\n\nIn Absorb mode, RF is generated whenever a creature takes damage on top of it.\n\nIn Heal mode, RF is used to heal creatures and players above or below it.", JAPTA.lifeConverter),
				JAPTA.getRecipe(JAPTA.lifeConverter)));
		entries.add(buildEntry("Mechanical Generator",
				pagesForLongText("The Mechanical Generator generates RF from block updates. This is more useful when used with a redstone clock.", JAPTA.mechanicalGenerator),
				JAPTA.getRecipe(JAPTA.mechanicalGenerator)));
		entries.add(buildEntry("Elevator",
				pagesForLongText("The elevator is actually two blocks, the Elevator Shaft and the Elevator Top. Place the Elevator Top on top of the Elevator Shaft(s) and supply the top with RF, then jump from the bottom to go up or sneak on top to go down.", JAPTA.elevatorTop),
				JAPTA.getRecipe(JAPTA.elevatorShaft),
				JAPTA.getRecipe(JAPTA.elevatorTop)));
		entries.add(buildEntry("Time Machine",
				pagesForLongText("The Time Machine lets you use your RF to speed up time! It only applies to time of day, and doesn't affect plants, furnaces, etc. Apply a redstone signal to pause it, quite useful with a Daylight Sensor.", JAPTA.timeMachine),
				JAPTA.getRecipe(JAPTA.timeMachine)));
		entries.add(buildEntry("Chest Charger",
				pagesForLongText("The Chest Charger will charge items in inventories placed next to it with RF.", JAPTA.chestCharger),
				JAPTA.getRecipe(JAPTA.chestCharger)));
		entries.add(buildEntry("Cake Converter",
				pagesForLongText("The Cake Converter converts between Cake and RF. Right-clicking will change its mode between Deploy (RF->Cake) and Absorb (Cake->RF). It has a range of 4 blocks in each direction for cake absorption but only 1 up and down for cake placement.", JAPTA.cakeConverter),
				JAPTA.getRecipe(JAPTA.cakeConverter)));
		entries.add(buildEntry("Flux Blaster",
				pagesForLongText("The Flux Blaster provides power to all blocks up to 8 blocks in front of it. When placing it, it faces away from you. If it finds an Elevator Shaft, it will power the block above it (probably an Elevator Top).", new ItemStack(JAPTA.fluxBlaster, 1, 3)),
				JAPTA.getRecipe(JAPTA.fluxBlaster)));
		entries.add(buildEntry("Bonemeal Applicator",
				pagesForLongText("The Bonemeal Applicator, when given power and placed next to an inventory containing Bone Meal, will use the Bone Meal on plants in an 8x8x8 cuboid around it. Its decisions of where to apply it are similar to the RNG Quarry.", JAPTA.bonemealApplicator),
				JAPTA.getRecipe(JAPTA.bonemealApplicator)));
		entries.add(buildEntry("Mover",
				pagesForLongText("The Mover will teleport entities on top of it in the direction it is facing. It is most useful in long chains. It also provides power to the block in front of it, so you might only need to power the back of the chain.", JAPTA.mover),
				JAPTA.getRecipe(JAPTA.mover)));
		entries.add(buildEntry("Battery Potato",
				pagesForLongText("The Battery Potato is an item that lets you eat RF in the form of a delicious potato.  It can be charged by the Charging Plate or any other block that charges items.", JAPTA.batteryPotato),
				JAPTA.getRecipe(JAPTA.batteryPotato)));
		entries.add(buildEntry("RF Meter",
				pagesForLongText("The RF Meter can be used on a block to read its RF content.", JAPTA.rfMeter),
				JAPTA.getRecipe(JAPTA.rfMeter)));
		categories.add(new CategoryItemStack(entries, "text.japta.guide.category1", new ItemStack(JAPTA.batteryPotato)));
		BookBuilder builder = new BookBuilder();
		builder.setAuthor("VpzomTrrfrt");
		builder.setBookColor(Color.black);
		builder.setCategories(categories);
		builder.setUnlocDisplayName("text.japta.guide.title");
		builder.setUnlocBookTitle("text.japta.guide.title");
		builder.setUnlocWelcomeMessage("text.japta.guide.title");
		book = builder.build();
		GuideRegistry.registerBook(book);
	}
	
	private static EntryAbstract buildEntry(String name, Object... pages) {
		List<IPage> l = new ArrayList<IPage>();
		for(Object o : pages) {
			if(o instanceof Collection) {
				l.addAll((Collection<? extends IPage>)o);
			}
			else if(o instanceof IPage) {
				l.add((IPage) o);
			}
			else if(o instanceof IRecipe) {
				l.add(new PageIRecipe((IRecipe) o));
			}
		}
		return new EntryBase(l, name);
	}
	
	private static List<IPage> pagesForLongText(String text, Block item) {
		return pagesForLongText(text, Item.getItemFromBlock(item));
	}
	
	private static List<IPage> pagesForLongText(String text, Item item) {
		return pagesForLongText(text, new ItemStack(item));
	}
	
	private static List<IPage> pagesForLongText(String text, ItemStack item) {
		String[] lines = WordUtils.wrap(text, 22).split("\n");
		List<IPage> tr = new ArrayList<IPage>();
		String firstPageText = "";
		int i = 0;
		for(; i < 8 && i < lines.length; i++) {
			firstPageText += lines[i]+"\n";
		}
		tr.add(new PageItemStack(firstPageText, item));
		for(; i < lines.length; i+=16) {
			String pageText = "\n";
			for(int j = i;j < lines.length && j < i+16; j++) {
				pageText += lines[j]+"\n";
			}
			tr.add(new PageText(pageText));
		}
		return tr;
	}
	
}