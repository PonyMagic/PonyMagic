package net.braunly.ponymagic.gui;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonSkill extends GuiButton {
	public String skillName;
	public String[] depends;
	public Integer[] lines;
	public int posX;
	public int posY;
	public int minLevel;
	public int skillLevel;
	
//	public GuiButtonSkill (String name, int id, int x, int y) {
//		this(name, null, id, x, y, null, 1, 5);
//	}
//	
//	public GuiButtonSkill (String name, String[] depends, int id, int x, int y) {
//		this(name, depends, id, x, y, null, 1);
//	}
	
	public GuiButtonSkill (String name, int id, int x, int y, int skillLevel, int minLevel) {
		this(name, null, id, x, y, null, skillLevel, minLevel);
	}
	
	public GuiButtonSkill (String name, String[] depends, int id, int x, int y, int skillLevel, int minLevel) {
		this(name, depends, id, x, y, null, skillLevel, minLevel);
	}
	
	public GuiButtonSkill (String name, String[] depends, int id, int x, int y, Integer[] lines, int skillLevel, int minLevel) {
		super(id, x, y, 32, 32, "");
		this.skillName = name;
		this.lines = lines;
		this.minLevel = minLevel;
		this.skillLevel = skillLevel;
		this.depends = depends;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY, int x, int y) {
		if (this.visible) {
	         ResourceLocation resLoc = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills/" + this.skillName + ".png");
	         mc.getTextureManager().bindTexture(resLoc);

	         this.posX = x + this.xPosition;
	         this.posY = y + this.yPosition - 32;
	         
	         this.func_146110_a(this.posX, this.posY, 0, 0, 32, 32, 32, 32);
	    }    
	}

	   public boolean isUnderMouse(int mouseX, int mouseY) {
	      return mouseX >= this.posX && mouseY >= this.posY && (float)mouseX < (float)this.posX + (float)this.width && (float)mouseY < (float)this.posY + (float)this.height;
	   }

	   @Override
	   public boolean mousePressed(Minecraft par1Minecraft, int mouseX, int mouseY) {
	      return this.enabled && this.visible && isUnderMouse(mouseX, mouseY); //mouseX >= this.xPosition && mouseY >= this.yPosition && (float)mouseX < (float)this.xPosition + (float)this.width * GuiStats.SCALE && (float)mouseY < (float)this.yPosition + (float)this.height * GuiStats.SCALE;
	   }
}