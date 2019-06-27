package de.jcm.darkpowers.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.DarkSkills;
import de.jcm.darkpowers.DarkSkills.Type;
import de.jcm.darkpowers.PlayerData;
import de.jcm.darkpowers.PlayerData.DarkRole;
import de.jcm.darkpowers.item.DarkItems;
import de.jcm.darkpowers.network.PacketDarkAction;
import de.jcm.darkpowers.network.PacketDarkAction.DarkAction;
import de.jcm.darkpowers.util.GUIHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiSkills extends GuiScreen
{
	private EntityPlayer player;
	private int levelFlash;
	private int levelJump;
	private PlayerData data;

	private List<GuiSkillTab> skillTabs;
	private int selectedSkill;

	private boolean dragPossible;
	private boolean drag;
	private int dragX, dragY;

	private GuiButton unlockButton;
	private int fragmentCount;

	private GuiButton passiveButton;

	public GuiSkills(EntityPlayer player)
	{
		super();

		this.player=player;
		data=PlayerData.get(player);

		this.skillTabs=new ArrayList<GuiSkillTab>();

		for(int i=0;i<player.inventory.mainInventory.length;i++)
		{
			ItemStack stack = player.inventory.mainInventory[i];
			if(stack!=null)
			{
				if(stack.getItem()==DarkItems.itemDarkIngot)
				{
					fragmentCount+=stack.stackSize;
				}
			}
		}
	}

	@Override
	public void initGui()
	{
		this.buttonList.clear();
		GuiButton doneButton = new GuiButton(0, this.width/2 - 100, (int) (this.height/2)+70, StatCollector.translateToLocal("gui.done"));
		this.buttonList.add(doneButton);
		this.unlockButton = new GuiButton(1, this.width/2 - 100, (int) (this.height/2), "");
		this.buttonList.add(unlockButton);
		this.passiveButton = new GuiButton(2, this.width/2 - 100, (int) (this.height/2), "");
		this.buttonList.add(passiveButton);

		this.skillTabs.clear();
		if(data.getRole()==DarkRole.MAGE || data.getRole()==DarkRole.DARKNESS)
		{
			for (DarkSkills skill : DarkSkills.values())
			{
				this.skillTabs.add(new GuiSkillTab(skill, data));
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		GL11.glColor3d(1, 1, 1);

		this.drawBackground(p_73863_1_);
		this.drawForeground();

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

		this.drawHover(p_73863_1_, p_73863_2_);

		if(drag)
		{
			skillTabs.get(selectedSkill).draw(dragX-16, dragY-16);
		}
	}

	public void drawForeground()
	{
		for(int i=0;i<data.getSelectedSkills().length;i++)
		{
			DarkSkills skill = data.getSelectedSkills()[i];
			int x = this.width/2 - 128 + 10 + 16 + i*(38+17);
			int y = this.height/2 + unlockButton.height + 10;
			if(skill!=null)
			{
				GuiSkillTab tab = skillTabs.get(skill.ordinal());
				tab.draw(x, y);
			}
			else
			{
				ResourceLocation border = new ResourceLocation(DarkPowers.MODID.toLowerCase(), "textures/gui/skill_normal_border.png");
				this.mc.getTextureManager().bindTexture(border);
				if(drag && dragX>=x && dragX<=x+38 && dragY>=y && dragY<=y+38)
				{
					GUIHelper.drawTexturedRect(x-2, y-2, 0, 0, 42, 42, 42, 42);
				}
				else
				{
					GUIHelper.drawTexturedRect(x, y, 0, 0, 38, 38, 38, 38);
				}
			}
		}

		for (int i=0;i<skillTabs.size();i++)
		{
			GuiSkillTab tab=skillTabs.get(i);
			tab.draw(this.width/2 - 128 + 10 + i*(38+5), this.height/2 - 100 + 10);
		}

		if(data.getRole()==DarkRole.MAGE || data.getRole()==DarkRole.DARKNESS)
		{
			DarkSkills skill = DarkSkills.values()[selectedSkill];

			fontRendererObj.drawSplitString(skill.getLocalizedDescription(), this.width/2 - 128 + 10, this.height/2 - 100 + 20 + 32, 256-10, Color.BLACK.getRGB());

			if(!data.getUnlocks().contains(skill))
			{
				int cost = skill.getUnlockCost();
				if(fragmentCount>=cost)
				{
					unlockButton.displayString = StatCollector.translateToLocalFormatted("gui.unlock.enough"+(cost==1?".sg":""), skill.getUnlockCost());
					unlockButton.enabled=true;
				}
				else
				{
					unlockButton.displayString = StatCollector.translateToLocalFormatted("gui.unlock.not_enough"+(cost==1?".sg":""), skill.getUnlockCost());
					unlockButton.enabled=false;
				}
				unlockButton.visible=true;
				passiveButton.visible=false;
			}
			else
			{
				unlockButton.visible=false;
				if(skill.getType()==Type.ACTIVE)
				{
					passiveButton.visible=false;
				}
				else if(skill.getType()==Type.PASSIVE)
				{
					if(data.getEnabledPassiveSkills().contains(skill))
					{
						passiveButton.displayString = StatCollector.translateToLocalFormatted("gui.passive.disable", skill.getLocalizedName());
					}
					else
					{
						passiveButton.displayString = StatCollector.translateToLocalFormatted("gui.passive.enable", skill.getLocalizedName());
					}
					passiveButton.visible=true;
				}
			}
		}
	}

	public void drawHover(int x, int y)
	{
		int minY=this.height/2 - 100 + 10;
		int maxY=minY+32;

		if(y>=minY && y<maxY)
		{
			int element=(x-this.width/2+128-10)/(38+5);
			if(element>=0 && element<DarkSkills.values().length)
			{
				int x1=this.width/2 - 128 + 10 + element*(38+5);
				int x2=x1+32;

				if(x>=x1 && x<x2)
				{
					if(data.getRole()==DarkRole.MAGE || data.getRole()==DarkRole.DARKNESS)
					{
						String skill=DarkSkills.values()[element].getLocalizedName();

						drawHoveringText(Arrays.asList(new String[]{skill}), x, y, Minecraft.getMinecraft().fontRenderer);
					}
				}
			}
		}
	}

	@Override
	public void drawBackground(int p_146278_1_)
	{
		ResourceLocation background = new ResourceLocation(DarkPowers.MODID.toLowerCase(), "textures/gui/skills.png");
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);

		drawTexturedModalRect(this.width/2 - 256/2, this.height/2 - 200/2, 0, 0, 256, 200);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void mouseClicked(int x, int y, int flag)
	{
		int minY=this.height/2 - 100 + 10;
		int maxY=minY+32;

		dragPossible = false;

		if(flag==0)
		{
			if(y>=minY && y<maxY)
			{
				int element=(x-this.width/2+128-10)/(38+5);
				if(element>=0 && element<skillTabs.size())
				{
					int x1=this.width/2 - 128 + 10 + element*(38+5);
					int x2=x1+32;

					if(x>=x1 && x<x2)
					{
						selectedSkill=element;
						DarkSkills skill = DarkSkills.values()[selectedSkill];
						if(skill.getType()==Type.ACTIVE)
						{
							if(data.getUnlocks().contains(skill))
							{
								dragPossible = true;
							}
						}
					}
				}
			}
		}

		if(flag==1)
		{
			for(int slot=0;slot<data.getSelectedSkills().length;slot++)
			{
				int tx = this.width/2 - 128 + 10 + 16 + slot*(38+17);
				int ty = this.height/2 + unlockButton.height + 10;
				if(x>=tx && x<=tx+32 && y>=ty && y<=ty+32)
				{
					DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.EQUIP_SKILL, slot, -1));
				}
			}
		}

		super.mouseClicked(x, y, flag);
	}

	@Override
	protected void mouseClickMove(int x, int y, int button, long time)
	{
		if(dragPossible)
		{
			this.drag = true;
			this.dragX = x;
			this.dragY = y;
		}

		super.mouseClickMove(x, y, button, time);
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int which)
	{
		if(which==0 || which==1)
		{
			if(drag)
			{
				drag = false;
				for(int slot=0;slot<data.getSelectedSkills().length;slot++)
				{
					int tx = this.width/2 - 128 + 10 + 16 + slot*(38+17);
					int ty = this.height/2 + unlockButton.height + 10;
					if(dragX>=tx && dragX<=tx+32 && dragY>=ty && dragY<=ty+32)
					{
						DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.EQUIP_SKILL, slot, selectedSkill));
					}
				}
			}
		}

		super.mouseMovedOrUp(x, y, which);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id==0)
		{
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
		if(button.id==1)
		{
			DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.UNLOCK_SKILL, selectedSkill));
		}
		if(button.id==2)
		{
			if(data.getEnabledPassiveSkills().contains(DarkSkills.values()[selectedSkill]))
			{
				DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.EQUIP_SKILL, 0, selectedSkill));
			}
			else
			{
				DarkPowers.wrapper.sendToServer(new PacketDarkAction(DarkAction.EQUIP_SKILL, 1, selectedSkill));
			}
		}
	}
}
