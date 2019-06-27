package de.jcm.darkpowers;

import org.apache.logging.log4j.Logger;

import de.jcm.darkpowers.block.DarkBlocks;
import de.jcm.darkpowers.client.render.entity.RenderLivingEntityBossDarkness;
import de.jcm.darkpowers.client.render.tileentity.RenderDarkDome;
import de.jcm.darkpowers.entity.boss.EntityDarkness;
import de.jcm.darkpowers.entity.projectile.EntityBlackArrow;
import de.jcm.darkpowers.inventory.CreativeTabDarkPowersDarkness;
import de.jcm.darkpowers.item.DarkItems;
import de.jcm.darkpowers.network.PacketDispatcher;
import de.jcm.darkpowers.ritual.Ritual;
import de.jcm.darkpowers.ritual.RitualCollision;
import de.jcm.darkpowers.ritual.RitualSummoning;
import de.jcm.darkpowers.tileentity.TileEntityAltar;
import de.jcm.darkpowers.tileentity.TileEntityDarkDome;
import de.jcm.darkpowers.world.dimension.WorldProviderDarkness;

import net.minecraft.client.model.ModelPig;
import net.minecraft.init.Blocks;
import net.minecraft.stats.Achievement;

import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.DimensionManager;

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

	// Tabs
	public static CreativeTabDarkPowersDarkness creativeTabDarkness;

	// Achievements
	public static Achievement blackArrowMultikillAchievement;

	// Achievement Pages
	public static AchievementPage achievementPage;

	// Dimension IDs
	public static int dimensionEnergyId = DimensionManager.getNextFreeDimId();

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
		commonProxy.init(event);

		creativeTabDarkness = new CreativeTabDarkPowersDarkness();
		DarkBlocks.init();
		DarkItems.init();

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
		commonProxy.postInit(event);
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
