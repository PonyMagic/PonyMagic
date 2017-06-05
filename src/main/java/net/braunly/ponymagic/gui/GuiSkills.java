package net.braunly.ponymagic.gui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.network.packets.RequestPlayerDataPacket;
import net.braunly.ponymagic.race.EnumRace;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import scala.collection.concurrent.Map;

public class GuiSkills extends GuiScreen {
	
	private final ResourceLocation bg = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills_bg.png");
	private final ResourceLocation expBar = new ResourceLocation(PonyMagic.MODID, "textures/gui/exp_bar.png");
	private final ResourceLocation reset = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills_reset.png");
	private final ResourceLocation lvlUp = new ResourceLocation(PonyMagic.MODID, "textures/gui/lvl_up.png");
	
	private PlayerData playerData;
	
	private HashMap<EnumRace, ArrayList<GuiButtonSkill>> skillsNet = new HashMap<EnumRace, ArrayList<GuiButtonSkill>>();
	
	@Override
	public void initGui() {
		PonyMagic.channel.sendToServer(new RequestPlayerDataPacket());
		
		
		ArrayList zebraSkills = new ArrayList<GuiButtonSkill>();
		
		// Zebras		
		zebraSkills.add(new GuiButtonSkill("jump", 0, 41, 97));
		// T1
		zebraSkills.add(new GuiButtonSkill("dispel", 1, 105, 65));
		zebraSkills.add(new GuiButtonSkill("fireresistance", 2, 105, 129));
		zebraSkills.add(new GuiButtonSkill("drown", 3, 105, 193));
		// T2
		zebraSkills.add(new GuiButtonSkill("slow", 4, 169, 65));
		zebraSkills.add(new GuiButtonSkill("purity", 5, 169, 129));
		zebraSkills.add(new GuiButtonSkill("nightvision", 6, 169, 193));
		// T3
		zebraSkills.add(new GuiButtonSkill("vulnerable", 7, 233, 65));
		zebraSkills.add(new GuiButtonSkill("antidote", 8, 233, 129));
		zebraSkills.add(new GuiButtonSkill("climb", 9, 233, 193));
		
		// Stamina
		zebraSkills.add(new GuiButtonSkill("staminaPool", 10, 41, 161));
		zebraSkills.add(new GuiButtonSkill("staminaPool", 11, 105, 257));
		zebraSkills.add(new GuiButtonSkill("staminaPool", 12, 233, 257));
		zebraSkills.add(new GuiButtonSkill("staminaRegen", 13, 169, 257));
		zebraSkills.add(new GuiButtonSkill("staminaRegen", 14, 297, 257));
		zebraSkills.add(new GuiButtonSkill("staminaHealthRegen", 15, 361, 257));
		zebraSkills.add(new GuiButtonSkill("staminaFoodRegen", 16, 425, 257));
		
		skillsNet.put(EnumRace.ZEBRA, zebraSkills);
		// INIT PlayerData
	    this.playerData = PlayerDataController.instance.getPlayerData(this.mc.thePlayer);
		
//	    this.buttonList.add(this.bClose = new GuiButton(1, this.width - 25, 5, 20, 20,  "X"));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
//	    if (button == this.bClose) {
//	        this.mc.displayGuiScreen(null);
//	        if (this.mc.currentScreen == null)
//	            this.mc.setIngameFocus();
//	    }
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		
	    this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	    
	    String race = "";
	    if (playerData.race != null) {
	    	race = playerData.race.getLocalizedName();
	    } else {
	    	race = "нет";
	    }	    
	    this.fontRendererObj.drawString("Раса - " + race, 0, 0, 16777215, false);
	    
	    
	    int w = 496;
	    int h = 334;
	    
	    int x = (this.width - w) / 2;
	    int y = (this.height - h) / 2;
	    
	    this.mc.getTextureManager().bindTexture(bg);
	    this.func_146110_a(x, y, 0, 0, w, h, 512, 512); //496.334
	    
	    
	    GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	    
        this.mc.getTextureManager().bindTexture(expBar);
	    this.func_146110_a(x + 67, y + 310, 0, 0, 364, 10, 364, 20);
	    this.func_146110_a(x + 67, y + 310, 0, 10, 150, 10, 364, 20);
	    
	    this.mc.getTextureManager().bindTexture(reset);
	    this.func_146110_a(x + 20, y + 290, 0, 0, 32, 32, 32, 32);

	    
	    if (this.playerData.race != null && this.playerData.race != EnumRace.REGULAR) {
			for (GuiButtonSkill skill : this.skillsNet.get(this.playerData.race)) {
//				if (this.playerData.skillData.isSkillLearned(skill)) {
//					drawActiveSkill();
//				} else {
//					drawInActiveSkill();
//				}
				
//				ResourceLocation resLoc = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills/" + skill + ".png");
//				this.mc.getTextureManager().bindTexture(resLoc);
//				Integer[] skillCoords = this.skillsNet.get(this.playerData.race).get(skill).net;
//				for (int i = 0; i < skillCoords.length; i += 2) {
//				    this.func_146110_a(x + skillCoords[i], y + skillCoords[i+1] - 32, 0, 0, 32, 32, 32, 32);
//				}
				
				skill.drawButton(this.mc, mouseX, mouseY, x, y);
				
				if (skill.isUnderMouse(mouseX, mouseY)) {
		        	 String[] text = {skill.skillName};
		        	 List temp = Arrays.asList(text);
		        	 drawHoveringText(temp, mouseX, mouseY, mc.fontRenderer);
		         }
				
				if (skill.mousePressed(this.mc, mouseX, mouseY)) {
					PonyMagic.log.info(skill.skillName);
				}
				
			}
		}
	    
	}
	
	@Override
	public void updateScreen()
    {
        super.updateScreen();
    }
	
	@Override
	protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
//        if (par2 == 28) {  // ENTER
//        	actionPerformed(this.bClose);
//        }
    }
	
	@Override
	public boolean doesGuiPauseGame() {
	    return false;
	}
}
