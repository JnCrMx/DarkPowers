package de.jcm.forge.darknesslight;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.jcm.forge.darknesslight.gui.GuiEnergyOverlay;
import de.jcm.forge.darknesslight.gui.GuiSkillOverlay;
import de.jcm.forge.darknesslight.gui.GuiSkills;
import de.jcm.forge.darknesslight.network.PacketDispatcher;
import de.jcm.forge.darknesslight.network.PacketSyncDarkPlayerData;
import de.jcm.math.geo.vector.Vector2D;
import de.jcm.math.geo.vector.helpers.VectorAngle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;

//TDOD: Add light side
public class ClientProxy extends CommonProxy
{	
	public static KeyBinding[] keyBindings;
	
	private GuiSkillOverlay guiSkills;
	private GuiEnergyOverlay guiEnergyOverlay;
	
	@Override
	public void preInit(FMLPreInitializationEvent e)
	{
		System.out.println("Client proxy pre init");
	}
	
	@Override
	public void init(FMLInitializationEvent e)
	{
		System.out.println("Client proxy init");
		
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		
		keyBindings = new KeyBinding[3];
		
		keyBindings[0] = new KeyBinding("key.menu.desc", Keyboard.KEY_NUMPAD0, "key.darkpowers.category");
		keyBindings[1] = new KeyBinding("key.flash.desc", Keyboard.KEY_P, "key.darkpowers.category");
		keyBindings[2] = new KeyBinding("key.jump.desc", Keyboard.KEY_H, "key.darkpowers.category");
		
		// register all the key bindings
		for (int i = 0; i < keyBindings.length; ++i)
		{
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e)
	{
		System.out.println("Client proxy post init");
		DarknessVsLight.wrapper.registerMessage(PacketSyncDarkPlayerData.Handler.class, PacketSyncDarkPlayerData.class, 0, Side.CLIENT);
		DarknessVsLight.wrapper.registerMessage(PacketSyncDarkPlayerData.Handler.class, PacketSyncDarkPlayerData.class, 0, Side.SERVER);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post event)
	{
		if (event.type == ElementType.ALL)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
			if (data != null) if (data.isDarkness() || data.isDarkMage())
			{
				if (!Minecraft.getMinecraft().gameSettings.showDebugInfo)
				{
					if (guiSkills == null) guiSkills = new GuiSkillOverlay(Minecraft.getMinecraft().thePlayer);

					guiSkills.tick();
					guiSkills.render();
				}
				if(data.isDarkness()) //TODO: fix
				{
					boolean survival=Minecraft.getMinecraft().playerController.gameIsSurvivalOrAdventure();
					if(survival)
					{
						if(guiEnergyOverlay==null) guiEnergyOverlay=new GuiEnergyOverlay(Minecraft.getMinecraft().thePlayer);
						
						guiEnergyOverlay.render();
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public void onEvent(KeyInputEvent event)
	{		
		if(keyBindings[0].isPressed())
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			PlayerData data=PlayerData.get(player);
			System.out.println(data.getEnergy());
			if(data.isDarkness() || data.isDarkMage())
			{
				System.out.println("a");
				Minecraft.getMinecraft().displayGuiScreen(new GuiSkills(player));
			}
		}
		if (keyBindings[1].isPressed())
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
			
			data.requestSync();
			
			if ((data.isDarkness() || data.isDarkMage()) && data.getEnergy()>=Constants.FLASH_ENERGY_COST)
			{				
				if (data.getCooldown(DarkSkills.FLASH) == 0)
				{
					float yaw = player.rotationYaw;
					
					Vector2D vector = VectorAngle.createVector2D(-yaw, 5);
					
					player.motionX = vector.getX();
					player.motionY = 0.5;
					player.motionZ = vector.getY();
					
					data.setCooldown(DarkSkills.FLASH, Constants.FLASH_COOLDOWN);
					
					if(data.isDarkness()) //TODO: fix
					{
						data.setEnergy(data.getEnergy()-Constants.FLASH_ENERGY_COST);
					}
					
					PacketSyncDarkPlayerData sync=new PacketSyncDarkPlayerData(player, data);
					PacketDispatcher.sendToServer(sync);
				}
			}
		}
		if (keyBindings[2].isPressed())
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			PlayerData data = (PlayerData) player.getExtendedProperties(PlayerData.IDENTIFIER);
			
			data.requestSync();
			
			if ((data.isDarkness() || data.isDarkMage()) && data.getEnergy()>=Constants.JUMP_ENERGY_COST)
			{				
				if (data.getCooldown(DarkSkills.JUMP) == 0)
				{
					float yaw = player.rotationYaw;
					
					Vector2D vector = VectorAngle.createVector2D(-yaw, 5);
					
					player.motionX = vector.getX();
					player.motionY = 0.5;
					player.motionZ = vector.getY();
					
					data.setCooldown(DarkSkills.JUMP, Constants.JUMP_COOLDOWN);
					
					if(data.isDarkness()) //TODO: fix
					{
						data.setEnergy(data.getEnergy()-Constants.JUMP_ENERGY_COST);
					}
					PacketSyncDarkPlayerData sync=new PacketSyncDarkPlayerData(player, data);
					PacketDispatcher.sendToServer(sync);
				}
			}
		}
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : ctx.getServerHandler().playerEntity);
	}
}
