package net.braunly.ponymagic.gui;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiButtonSkill extends GuiButton {
	public String skillName;
	public Integer[] lines;
	private int posX;
	private int posY;
	
	public GuiButtonSkill (String name, int id, int x, int y) {
		this(name, id, x, y, null);
	}
	
	public GuiButtonSkill (String name, int id, int x, int y, Integer[] lines) {
		super(id, x, y, 32, 32, "");
		this.skillName = name;
		this.lines = lines;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY, int x, int y) {
		if (this.visible) {
//	         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//	         this.field_146123_n = this.isUnderMouse(mouseX, mouseY);

	         ResourceLocation resLoc = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills/" + this.skillName + ".png");
	         mc.getTextureManager().bindTexture(resLoc);

//	         GL11.glPushMatrix();
//	         GL11.glTranslatef((float)this.xPosition, (float)this.yPosition, 0.0F);
	         
	         this.posX = x + this.xPosition;
	         this.posY = y + this.yPosition - 32;
	         
	         this.func_146110_a(this.posX, this.posY, 0, 0, 32, 32, 32, 32);
	         
//	         GL11.glPopMatrix();
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