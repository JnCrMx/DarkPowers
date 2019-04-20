package de.jcm.darkpowers;

import org.apache.logging.log4j.Logger;

import de.jcm.darkpowers.block.BlockAltar;
import de.jcm.darkpowers.block.BlockDarkDome;
import de.jcm.darkpowers.block.BlockDarkness;
import de.jcm.darkpowers.block.BlockRune;
import de.jcm.darkpowers.block.BlockRune.RuneType;
import de.jcm.darkpowers.client.render.RenderDarkSword;
import de.jcm.darkpowers.client.render.entity.RenderLivingEntityBossDarkness;
import de.jcm.darkpowers.client.render.tileentity.RenderDarkDome;
import de.jcm.darkpowers.entity.boss.EntityDarkness;
import de.jcm.darkpowers.entity.projectile.EntityBlackArrow;
import de.jcm.darkpowers.inventory.CreativeTabDarkPowersDarkness;
import de.jcm.darkpowers.item.ItemBookOfDarkMyths;
import de.jcm.darkpowers.item.ItemDarkIngot;
import de.jcm.darkpowers.item.ItemDarkSword;
import de.jcm.darkpowers.network.PacketDispatcher;
import de.jcm.darkpowers.ritual.Ritual;
import de.jcm.darkpowers.ritual.RitualCollision;
import de.jcm.darkpowers.ritual.RitualSummoning;
import de.jcm.darkpowers.tileentity.TileEntityAltar;
import de.jcm.darkpowers.tileentity.TileEntityDarkDome;
import de.jcm.darkpowers.world.dimension.WorldProviderDarkness;

import net.minecraft.client.model.ModelPig;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.stats.Achievement;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.EnumHelper;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = DarkPowers.MODID, version = DarkPowers.VERSION, name = DarkPowers.NAME)
public class DarkPowers
{
	// Mod ID etc.
	public static final String MODID = "DarkPowers";
	public static final String VERSION = "1.0";
	public static final String NAME = "DarkPowers";

	public static SimpleNetworkWrapper wrapper;

	// Blocks
	public static BlockDarkness blockDarkness;
	public static BlockAltar blockAltar;
	public static BlockDarkDome blockDarkDome;

	// Rune blocks
	public static BlockRune blockRuneEmpty;
	public static BlockRune blockRuneBinding;
	public static BlockRune blockRuneOpening;
	public static BlockRune blockRuneHolding;
	public static BlockRune blockRuneInvitation;

	// Items
	public static ItemDarkIngot itemDarkIngot;
	public static ItemBookOfDarkMyths itemBookOfDarkMyths;

	// Armor items


	// Tool items


	public static ItemDarkSword itemDarkSword;

	// Tabs
	public static CreativeTabDarkPowersDarkness creativeTabDarkness;

	// Achievements
	public static Achievement blackArrowMultikillAchievement;

	// Achievement Pages
	public static AchievementPage achievementPage;

	// Dimension IDs
	public static int dimensionEnergyId = DimensionManager.getNextFreeDimId();

	// Biomes

	// Materials
	public static ArmorMaterial armorMaterialEngergium;
	public static ToolMaterial toolMaterialEngerium;

	public static ToolMaterial toolMaterialDark;

	// World & structure generators

	//GUIs
	public static int guiBookOfDarkMyths;

	// Instance
	public static DarkPowers instance;

	// Other stuff
	public static Logger logger;

	@SidedProxy(serverSide = "de.jcm.darkpowers.ServerProxy", clientSide = "de.jcm.darkpowers.ClientProxy", modId=MODID)
	public static CommonProxy commonProxy;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		// Init proxy
		commonProxy.init(event);

		// Init & register materials
		armorMaterialEngergium = EnumHelper.addArmorMaterial("Energium", 50, new int[] { 3, 9, 7, 3 }, 0);
		toolMaterialEngerium = EnumHelper.addToolMaterial("Energium", 10, 3000, 30.0F, 10, 0);

		toolMaterialDark = EnumHelper.addToolMaterial("Darkness", 20, 10000, 100.0F, 50.0F, 0);

		// Init Blocks
		blockDarkness = new BlockDarkness();
		blockAltar = new BlockAltar();
		blockDarkDome = new BlockDarkDome();

		// Init runes
		blockRuneEmpty = new BlockRune(RuneType.EMPTY);
		blockRuneBinding = new BlockRune(RuneType.BINDING);
		blockRuneOpening = new BlockRune(RuneType.OPENING);
		blockRuneHolding = new BlockRune(RuneType.HOLDING);
		blockRuneInvitation = new BlockRune(RuneType.INVITATION);

		// Init items
		itemDarkIngot = new ItemDarkIngot();
		itemBookOfDarkMyths = new ItemBookOfDarkMyths();

		// Init armor items

		// Init tool items

		itemDarkSword = new ItemDarkSword();

		// Init tabs
		creativeTabDarkness = new CreativeTabDarkPowersDarkness();

		// Init world & structure generators

		// Set blocks' tabs
		blockDarkness.setCreativeTab(creativeTabDarkness);
		blockAltar.setCreativeTab(creativeTabDarkness);

		// Set rune blocks' tabs
		blockRuneEmpty.setCreativeTab(creativeTabDarkness);
		blockRuneBinding.setCreativeTab(creativeTabDarkness);
		blockRuneOpening.setCreativeTab(creativeTabDarkness);
		blockRuneHolding.setCreativeTab(creativeTabDarkness);
		blockRuneInvitation.setCreativeTab(creativeTabDarkness);

		// Set items' tabs
		itemDarkIngot.setCreativeTab(creativeTabDarkness);
		itemBookOfDarkMyths.setCreativeTab(creativeTabDarkness);

		// Set armor items' tabs


		// Set tool items' tabs


		itemDarkSword.setCreativeTab(creativeTabDarkness);

		// Register blocks
		GameRegistry.registerBlock(blockDarkness, "darkness_block");
		GameRegistry.registerBlock(blockAltar, "altar");
		GameRegistry.registerBlock(blockDarkDome, "dark_dome");

		// Register rune blocks
		GameRegistry.registerBlock(blockRuneEmpty, "rune_empty");
		GameRegistry.registerBlock(blockRuneBinding, "rune_binding");
		GameRegistry.registerBlock(blockRuneOpening, "rune_opening");
		GameRegistry.registerBlock(blockRuneHolding, "rune_holding");
		GameRegistry.registerBlock(blockRuneInvitation, "rune_invitation");

		// Register items
		GameRegistry.registerItem(itemDarkIngot, "darkness_ingot");
		GameRegistry.registerItem(itemBookOfDarkMyths, "book_of_dark_myths");

		// Register armor items

		// Register tool items

		GameRegistry.registerItem(itemDarkSword, "dark_sword");

		// Register tile entities
		GameRegistry.registerTileEntity(TileEntityAltar.class, "altar");
		GameRegistry.registerTileEntity(TileEntityDarkDome.class, "dark_dome");

		// Bind special renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDarkDome.class, new RenderDarkDome());

		// Init & register gui handlers
		NetworkRegistry.INSTANCE.registerGuiHandler(this, commonProxy);

		// Init achievements
		blackArrowMultikillAchievement =
			new Achievement("achievement.blackArrowMultikill", "black_arrow_multikill", 0, 0, Blocks.dragon_egg, null)
			.setSpecial()
			.registerStat();

		// Init & register achievement pages
		achievementPage = new AchievementPage("DarkPowers", new Achievement[]
			{
				blackArrowMultikillAchievement
			});
		AchievementPage.registerAchievementPage(achievementPage);

		// Register entities
		EntityRegistry.registerGlobalEntityID(EntityDarkness.class, "DarknessBoss", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityBlackArrow.class, "BlackArrow", 0, this, 100, 10, true);

		// Register entity renderers
		RenderingRegistry.registerEntityRenderingHandler(EntityDarkness.class, new RenderLivingEntityBossDarkness(new ModelPig(), 1.0f));

		//Register custom item renderers
		MinecraftForgeClient.registerItemRenderer(itemDarkSword, new RenderDarkSword());

		// Init biomes

		// Register biomes

		// Register entity spawns

		// Init & register dimensions
		DimensionManager.registerProviderType(dimensionEnergyId, WorldProviderDarkness.class, true);
		DimensionManager.registerDimension(dimensionEnergyId, dimensionEnergyId);

		// Register world & structure generators

		// Set GUI IDs
		guiBookOfDarkMyths = 0;

		// Register rituals
		Ritual.registerRitual("collision", RitualCollision.class);
		Ritual.registerRitual("summoning", RitualSummoning.class);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		// Init proxy
		commonProxy.postInit(event);

		// Init & register crafting recipes

		// Init & register crafting recipes of ICraftable blocks

		// Init & register crafting recipes of ICraftable items

		// Init & register crafting recipes of ICraftable armor items

		// Init & register crafting recipes of ICraftable tool items
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		// Set instance
		instance = this;

		// Set logger
		logger = event.getModLog();

		// Init proxy
		commonProxy.preInit(event);

		// Init network wrapper
		wrapper = new SimpleNetworkWrapper(this.MODID);

		// Register packets
		PacketDispatcher.registerPackets();
	}
}
