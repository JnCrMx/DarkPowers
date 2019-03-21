package de.jcm.darkpowers;

import org.lwjgl.input.Keyboard;

import de.jcm.darkpowers.DarkSkills.Type;
import de.jcm.darkpowers.PlayerData.DarkRole;
import de.jcm.darkpowers.client.ClientEffect;
import de.jcm.darkpowers.client.render.EntityRendererDark;
import de.jcm.darkpowers.client.render.RenderPlayerDark;
import de.jcm.darkpowers.gui.GuiEnergyOverlay;
import de.jcm.darkpowers.gui.GuiSkillOverlay;
import de.jcm.darkpowers.gui.GuiSkills;
import de.jcm.darkpowers.network.PacketDarkAction;
import de.jcm.darkpowers.network.PacketDarkAction.DarkAction;
import de.jcm.darkpowers.network.PacketDispatcher;
import de.jcm.darkpowers.tileentity.TileEntityDarkDome;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import api.player.render.RenderPlayerAPI;

public class ClientProxy extends CommonProxy
{
	public static KeyBinding[] keyBindings;
	
	public GuiSkillOverlay guiSkills;
	public GuiEnergyOverlay guiEnergyOverlay;
	
	public boolean renderDark;
	public TileEntityDarkDome renderDome;
	
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		
		DarkPowers.logger.info("Client proxy pre init");
		
		RenderPlayerAPI.register(DarkPowers.MODID, RenderPlayerDark.class);
	}
	
	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
		
		DarkPowers.logger.info("Client proxy init");
		
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		
		keyBindings = new KeyBinding[5];
		
		keyBindings[0] = new KeyBinding("key.menu.desc", Keyboard.KEY_NUMPAD0, "key.darkpowers.category");
		keyBindings[1] = new KeyBinding("key.skill0.desc", Keyboard.KEY_U, "key.darkpowers.category");
		keyBindings[2] = new KeyBinding("key.skill1.desc", Keyboard.KEY_I, "key.darkpowers.category");
		keyBindings[3] = new KeyBinding("key.skill2.desc", Keyboard.KEY_O, "key.darkpowers.category");
		keyBindings[4] = new KeyBinding("key.skill3.desc", Keyboard.KEY_P, "key.darkpowers.category");
		
		// register all the key bindings
		for(int i = 0; i < keyBindings.length; ++i)
		{
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
		
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e)
	{
		super.postInit(e);
		
		DarkPowers.logger.info("Client proxy post init");
		
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.entityRenderer.getClass().equals(EntityRenderer.class))
		{
			mc.entityRenderer = new EntityRendererDark(mc, mc.getResourceManager());
			DarkPowers.logger.info("Replaced net.minecraft.client.Minecraft.entityRenderer with custom entity renderer.");
		}
		else
		{
			DarkPowers.logger.warn("net.minecraft.client.Minecraft.entityRenderer is an instance of "+mc.entityRenderer.getClass().getName()+" and therefore a subclass of "+EntityRenderer.class.getName()+"!");
			DarkPowers.logger.warn("Another mod might have replaced it already!");
			DarkPowers.logger.warn("It cannot be replaced without affecting this mod!");
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post event)
	{
		if(event.type == ElementType.ALL)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
			if(data != null)
			{
				if(data.getRole() == DarkRole.MAGE || data.getRole() == DarkRole.DARKNESS)
				{
					if(!Minecraft.getMinecraft().gameSettings.showDebugInfo)
					{						
						if(guiSkills == null)
							guiSkills = new GuiSkillOverlay(player);
						
						if(guiSkills.player!=player)
						{
							DarkPowers.logger.info("Change player of skill overlay from "+guiSkills.player+" to "+player);
							guiSkills.player=player;
						}
						
						guiSkills.render();
					}
					if(data.getRole() == DarkRole.MAGE || data.getRole() == DarkRole.DARKNESS)
					{
						boolean survival = Minecraft.getMinecraft().playerController.gameIsSurvivalOrAdventure();
						if(survival)
						{
							if(guiEnergyOverlay == null)
								guiEnergyOverlay = new GuiEnergyOverlay(Minecraft.getMinecraft().thePlayer);
							
							guiEnergyOverlay.render();
						}
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(
		priority = EventPriority.NORMAL,
		receiveCanceled = false)
	public void onEvent(KeyInputEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
		
		if(keyBindings[0].isPressed())
		{
			if(data.getRole() == DarkRole.MAGE || data.getRole() == DarkRole.DARKNESS)
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiSkills(player));
			}
		}
		for(int i = 0; i < 4; i++)
		{
			if(keyBindings[i + 1].isPressed())
			{
				DarkSkills skill = data.getSelectedSkills()[i];
				if(skill != null)
				{
					if(skill.getType()==Type.ACTIVE)
					{
						if(data.getRole() == DarkRole.MAGE || data.getRole() == DarkRole.DARKNESS)
						{
							if(data.getUnlocks().contains(skill))
							{
								if(data.getEnergy() >= skill.getEnergyCost())
								{
									if(data.getCooldown(skill) == 0)
									{
										PacketDispatcher.sendToServer(new PacketDarkAction(DarkAction.USE_SKILL, i));
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : ctx.getServerHandler().playerEntity);
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event)
	{
		PlayerData data = (PlayerData) event.player.getExtendedProperties(PlayerData.IDENTIFIER);
		if(data.getClientEffect() == ClientEffect.BARRIER_BLOCK)
		{
			if(data.getClientEffectTicks() > 0)
			{
				data.setClientEffectTicks(data.getClientEffectTicks() - 1);
			}
			else
			{
				data.setClientEffect(null);
			}
		}
	}
}
