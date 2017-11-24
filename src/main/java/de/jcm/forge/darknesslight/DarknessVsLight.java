package de.jcm.forge.darknesslight;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import de.jcm.forge.darknesslight.block.BlockDarkness;
import de.jcm.forge.darknesslight.block.BlockRitualTable;
import de.jcm.forge.darknesslight.block.BlockRune;
import de.jcm.forge.darknesslight.block.BlockRune.RuneType;
import de.jcm.forge.darknesslight.client.render.RenderDarkSword;
import de.jcm.forge.darknesslight.inventory.CreativeTabDarkPowersDarkness;
import de.jcm.forge.darknesslight.item.ItemDarkIngot;
import de.jcm.forge.darknesslight.item.ItemDarkSword;
import de.jcm.forge.darknesslight.network.PacketDispatcher;
import de.jcm.forge.darknesslight.world.dimension.WorldProviderDarkness;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.EnumHelper;

@Mod(modid = DarknessVsLight.MODID, version = DarknessVsLight.VERSION, name = DarknessVsLight.NAME)
public class DarknessVsLight
{
	// Mod ID etc.
	public static final String MODID = "darknesslight";
	public static final String VERSION = "1.0";
	public static final String NAME = "DarknessVsLight";
	
	public static SimpleNetworkWrapper wrapper;
	
	// Blocks
	public static BlockDarkness blockDarkness;
	public static BlockRitualTable blockRitualTable;
	
	// Rune blocks
	public static BlockRune blockRuneEmpty;
	public static BlockRune blockRuneBinding;
	public static BlockRune blockRuneOpening;
	public static BlockRune blockRuneHolding;
	public static BlockRune blockRuneInvitation;
	
	// Items
	public static ItemDarkIngot itemDarkIngot;
	
	// Armor items
	
	
	// Tool items

	
	public static ItemDarkSword itemDarkSword;
	
	// Tabs
	public static CreativeTabDarkPowersDarkness creativeTabDarkness;
	
	// Achievements
	
	// Dimension IDs
	public static int dimensionEnergyId = 2;
	
	// Biomes
	
	// Materials
	public static ArmorMaterial armorMaterialEngergium;
	public static ToolMaterial toolMaterialEngerium;
	
	public static ToolMaterial toolMaterialDark;

	//World & structure generators
	
	// Instance
	public static DarknessVsLight instance;
	
	// Other stuff
	private Item ThermalFoundation_material;
	private Block ThermalExpansion_machineFrame;
	private ItemStack ThermalFoundation_material_copperIngot;
	private ItemStack ThermalFoundation_material_goldGear;
	
	@SidedProxy(serverSide = "de.jcm.forge."+MODID+".ServerProxy", clientSide = "de.jcm.forge."+MODID+".ClientProxy", modId=MODID)
	public static CommonProxy commonProxy;
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		commonProxy.init(event);
		
		wrapper = new SimpleNetworkWrapper(this.MODID );
		
		// Set instance
		instance = this;
		
		// Init & register materials
		armorMaterialEngergium = EnumHelper.addArmorMaterial("Energium", 50, new int[] { 3, 9, 7, 3 }, 0);
		toolMaterialEngerium = EnumHelper.addToolMaterial("Energium", 10, 3000, 30.0F, 10, 0);
		
		toolMaterialDark = EnumHelper.addToolMaterial("Darkness", 20, 10000, 100.0F, 50, 0);
		
		// Init Blocks
		blockDarkness = new BlockDarkness();
		blockRitualTable = new BlockRitualTable();
		
		// Init runes
		blockRuneEmpty = new BlockRune(RuneType.EMPTY);
		blockRuneBinding = new BlockRune(RuneType.BINDING);
		blockRuneOpening = new BlockRune(RuneType.OPENING);
		blockRuneHolding = new BlockRune(RuneType.HOLDING);
		blockRuneInvitation = new BlockRune(RuneType.INVITATION);
		
		// Init items
		itemDarkIngot = new ItemDarkIngot();
		
		// Init armor items
		
		// Init tool items
		
		itemDarkSword = new ItemDarkSword();
		
		// Init tabs
		creativeTabDarkness = new CreativeTabDarkPowersDarkness();
		
		// Init world & structure generators
		
		// Set blocks´ tabs
		blockDarkness.setCreativeTab(creativeTabDarkness);
		blockRitualTable.setCreativeTab(creativeTabDarkness);
		
		// Set rune blocks´ tabs
		blockRuneEmpty.setCreativeTab(creativeTabDarkness);
		blockRuneBinding.setCreativeTab(creativeTabDarkness);
		blockRuneOpening.setCreativeTab(creativeTabDarkness);
		blockRuneHolding.setCreativeTab(creativeTabDarkness);
		blockRuneInvitation.setCreativeTab(creativeTabDarkness);
		
		// Set items´ tabs
		itemDarkIngot.setCreativeTab(creativeTabDarkness);
		
		// Set armor items´ tabs

		
		// Set tool items´ tabs

		
		itemDarkSword.setCreativeTab(creativeTabDarkness);
		
		// Register blocks
		GameRegistry.registerBlock(blockDarkness, "darkness_block");
		GameRegistry.registerBlock(blockRitualTable, "ritual_table");
		
		// Register rune blocks
		GameRegistry.registerBlock(blockRuneEmpty, "rune_empty");
		GameRegistry.registerBlock(blockRuneBinding, "rune_binding");
		GameRegistry.registerBlock(blockRuneOpening, "rune_opening");
		GameRegistry.registerBlock(blockRuneHolding, "rune_holding");
		GameRegistry.registerBlock(blockRuneInvitation, "rune_invitation");
		
		// Register items
		GameRegistry.registerItem(itemDarkIngot, "darkness_ingot");
		
		// Register armor items
		
		// Register tool items
		
		GameRegistry.registerItem(itemDarkSword, "dark_sword");
		
		// Register tile entities
		
		// Bind special renderers
		
		// Init & register gui handlers
		NetworkRegistry.INSTANCE.registerGuiHandler(this, commonProxy);
		
		// Init achievements
		
		// Init & register achievement pages
		AchievementPage page1 = new AchievementPage("Darkness", new Achievement[0]);
		AchievementPage.registerAchievementPage(page1);
		AchievementPage page2 = new AchievementPage("Light", new Achievement[0]);
		AchievementPage.registerAchievementPage(page2);
		
		// Register entities
	
		// Register entity renderers

		
		//Register custom item renderers
		MinecraftForgeClient.registerItemRenderer(itemDarkSword, new RenderDarkSword());
		
		// Init biomes
		
		// Register biomes
		
		// Register entity spawns
		
		// Init & register dimensions
		dimensionEnergyId = DimensionManager.getNextFreeDimId();
		DimensionManager.registerProviderType(dimensionEnergyId, WorldProviderDarkness.class, true);
		DimensionManager.registerDimension(dimensionEnergyId, dimensionEnergyId);
		
		//Register world & structure generators
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		commonProxy.postInit(event);
		
		// Init & register crafting recipes
		ThermalExpansion_machineFrame = GameRegistry.findBlock("ThermalExpansion", "Frame");
		ThermalFoundation_material = GameRegistry.findItem("ThermalFoundation", "material");
		Item wrench = GameRegistry.findItem("ThermalExpansion", "wrench");
		
		ThermalFoundation_material_goldGear = new ItemStack(ThermalFoundation_material, 1, 13);
		ThermalFoundation_material_copperIngot = new ItemStack(ThermalFoundation_material, 1, 64);
		
		// Init & register crafting recipes of ICraftable blocks
		
		// Init & register crafting recipes of ICraftable items
		
		// Init & register crafting recipes of ICraftable armor items
		
		// Init & register crafting recipes of ICraftable tool items
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		commonProxy.preInit(event);
		
		PacketDispatcher.registerPackets();
	}
}
