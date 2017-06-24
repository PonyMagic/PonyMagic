package net.braunly.ponymagic.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.braunly.ponymagic.exp.Exp;
import net.braunly.ponymagic.network.packets.RequestPlayerDataPacket;
import net.braunly.ponymagic.network.packets.SkillUpPacket;
import net.braunly.ponymagic.race.EnumRace;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiSkills extends GuiScreen {
	
	private final ResourceLocation bg = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills_bg.png");
	private final ResourceLocation expBar = new ResourceLocation(PonyMagic.MODID, "textures/gui/exp_bar.png");
	private final ResourceLocation reset = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills_reset.png");
	private final ResourceLocation lvlUp = new ResourceLocation(PonyMagic.MODID, "textures/gui/lvl_up.png");
	private final ResourceLocation skillActive = new ResourceLocation(PonyMagic.MODID, "textures/gui/skill_active.png");
	private final ResourceLocation skillAvailable = new ResourceLocation(PonyMagic.MODID, "textures/gui/skill_available.png");
	private final ResourceLocation skillUnAvailable = new ResourceLocation(PonyMagic.MODID, "textures/gui/skill_unavailable.png");
	
	private PlayerData playerData = null;
	
	private HashMap<EnumRace, ArrayList<GuiButtonSkill>> skillsNet = new HashMap<EnumRace, ArrayList<GuiButtonSkill>>();
	
	@Override
	public void initGui() {
		// FIXME ?
		initPlayerData();
		
		ArrayList zebraSkills = new ArrayList<GuiButtonSkill>();
		
		// Zebras		
		zebraSkills.add(new GuiButtonSkill("jump", 0, 41, 97, 0, 1));
		// T1
		zebraSkills.add(new GuiButtonSkill("dispel", 1, 105, 65, 1, 5));
		zebraSkills.add(new GuiButtonSkill("fireresistance", 2, 105, 129, 1, 5));
		zebraSkills.add(new GuiButtonSkill("drown", 3, 105, 193, 1, 5));
		// T2
		zebraSkills.add(new GuiButtonSkill("slow", new String[]{"dispel"}, 4, 169, 65, 1, 10));
		zebraSkills.add(new GuiButtonSkill("purity", new String[]{"fireresistance"}, 5, 169, 129, 1, 10));
		zebraSkills.add(new GuiButtonSkill("nightvision", new String[]{"drown"}, 6, 169, 193, 1, 10));
		// T3
		zebraSkills.add(new GuiButtonSkill("vulnerable", new String[]{"slow"}, 7, 233, 65, 1, 15));
		zebraSkills.add(new GuiButtonSkill("antidote", new String[]{"purity"}, 8, 233, 129, 1, 15));
		zebraSkills.add(new GuiButtonSkill("climb", new String[]{"nightvision"}, 9, 233, 193, 1, 15));
		
		// Stamina
		zebraSkills.add(new GuiButtonSkill("staminaPool", 10, 41, 161, 0, 1));
		
		zebraSkills.add(new GuiButtonSkill("staminaPool", 11, 105, 257, 1, 5));
		zebraSkills.add(new GuiButtonSkill("staminaPool", 12, 233, 257, 2, 10));
		zebraSkills.add(new GuiButtonSkill("staminaRegen", 13, 169, 257, 1, 15));
		zebraSkills.add(new GuiButtonSkill("staminaRegen", 14, 297, 257, 2, 20));
		zebraSkills.add(new GuiButtonSkill("staminaHealthRegen", 15, 361, 257, 1, 25));
		zebraSkills.add(new GuiButtonSkill("staminaFoodRegen", 16, 425, 257, 1, 30));
		
		this.buttonList.addAll(zebraSkills);
		
		skillsNet.put(EnumRace.ZEBRA, zebraSkills);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		GuiButtonSkill skill = (GuiButtonSkill) button;
	    
		// :FIXME: Move to SERVER side
		if (!isSkillActive(skill) && isSkillAvailable(skill)) {
			PonyMagic.log.info("SEND SKILL UP");
			PonyMagic.channel.sendToServer(new SkillUpPacket(skill.skillName, skill.skillLevel));
			initPlayerData();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {		
	    this.drawDefaultBackground();
	    
	    String race = "";
	    if (this.playerData.race != null) {
	    	race = this.playerData.race.getLocalizedName();
	    } else {
	    	race = "нет";
	    }	    
	    this.fontRendererObj.drawString("Раса - " + race, 0, 0, 16777215, false);
	    
	    
	    int w = 496;
	    int h = 334;
	    
	    int x = (this.width - w) / 2;
	    int y = (this.height - h) / 2;
	    
	    this.mc.getTextureManager().bindTexture(this.bg);
	    this.func_146110_a(x, y, 0, 0, w, h, 512, 512); //496.334
	    
	    this.mc.getTextureManager().bindTexture(this.reset);
	    this.func_146110_a(x + 20, y + 290, 0, 0, 32, 32, 32, 32);
	    
	    // Draw exp bar
	    int currentExp = (int)((float)(this.playerData.levelData.getExp() / ((this.playerData.levelData.getLevel() + 1) * Config.expPerLevel)) * 364);
        this.mc.getTextureManager().bindTexture(this.expBar);
	    this.func_146110_a(x + 67, y + 310, 0, 0, 364, 10, 364, 20);
	    this.func_146110_a(x + 67, y + 310, 0, 10, currentExp, 10, 364, 20);

	    // Draw skills
	    if (this.playerData.race != null && this.playerData.race != EnumRace.REGULAR) {
			for (GuiButtonSkill skill : this.skillsNet.get(this.playerData.race)) {
				// Skill background
				if (this.isSkillActive(skill)) {
					this.mc.getTextureManager().bindTexture(this.skillActive);
				} else if (this.isSkillAvailable(skill)) {
					this.mc.getTextureManager().bindTexture(this.skillAvailable);
				} else {
					this.mc.getTextureManager().bindTexture(this.skillUnAvailable);
				}
				this.func_146110_a(skill.posX-2, skill.posY-2, 0, 0, 36, 36, 36, 36);

				// Skill icon
				skill.drawButton(this.mc, mouseX, mouseY, x, y);				
			}
			
			// Showing info for skill
			for (GuiButtonSkill skill : this.skillsNet.get(this.playerData.race)) {				
				if (skill.isUnderMouse(mouseX, mouseY)) {
		        	 String[] text = {StatCollector.translateToLocal("skill." + skill.skillName + skill.skillLevel + ".name"),
		        			 StatCollector.translateToLocal("skill." + skill.skillName + skill.skillLevel + ".descr")};
		        	 List temp = Arrays.asList(text);
		        	 // Lightning shadow fix
		        	 RenderHelper.enableStandardItemLighting();
		        	 func_146283_a(temp, mouseX, mouseY);
		        	 RenderHelper.disableStandardItemLighting();
		        }			
			}
		}
	    
	}
	
	private boolean isSkillActive(GuiButtonSkill skill) {
		return this.playerData.skillData.getSkillLevel(skill.skillName) >= skill.skillLevel;
	}
	
	private boolean isSkillAvailable(GuiButtonSkill skill) {
		boolean dependencies = true;
		
		if (skill.depends != null) {
			for (String skillName: skill.depends) {
				if (this.playerData.skillData.isSkillLearned(skillName)) {
					break;
				}
				dependencies = false;
			}
		}
		
		if (dependencies && this.playerData.levelData.getLevel() >= skill.minLevel &&
				this.playerData.levelData.getFreeSkillPoints() > 0) {
			return true;
		}
		return false;
	}
	
	private void initPlayerData() {
		PonyMagic.channel.sendToServer(new RequestPlayerDataPacket());
		this.playerData = PlayerDataController.instance.getPlayerData((EntityPlayer) this.mc.thePlayer);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
	    return false;
	}
}
