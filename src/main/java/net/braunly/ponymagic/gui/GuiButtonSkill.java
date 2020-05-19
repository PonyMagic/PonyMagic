package net.braunly.ponymagic.gui;

import com.google.common.collect.ImmutableSet;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class GuiButtonSkill extends GuiButton {
	private Minecraft mc;
	private ResourceLocation resLoc;
	public int posX;
	public int posY;
	public float scale;

	public String skillName;
	public int skillLevel;
	public Set<String> lines;

	public GuiButtonSkill(String name, int id, int x, int y, int skillLevel) {
		this(name, id, x, y, ImmutableSet.of(), skillLevel);
	}

	public GuiButtonSkill(String name, int id, int x, int y, Set<String> lines, int skillLevel) {
		super(id, x, y, 32, 32, "");
		this.skillName = name;
		this.lines = lines;
		this.skillLevel = skillLevel;
	}

	public void initButton(Minecraft mc, int x, int y, float scale) {
		this.mc = mc;
		this.resLoc = new ResourceLocation(PonyMagic.MODID,
				"textures/gui/skills/" +  this.skillName + ".png");
		this.posX = x + this.x;
		this.posY = y + this.y - 32;
		this.scale = scale;
	}

	public void drawButton() {
		if (this.visible) {
			this.mc.getTextureManager().bindTexture(this.resLoc);
			GlStateManager.pushMatrix();
			GlStateManager.scale(this.scale, this.scale, this.scale);
			drawModalRectWithCustomSizedTexture(this.posX, this.posY, 0, 0, 32, 32, 32, 32);
			GlStateManager.popMatrix();
		}
	}

	public boolean isUnderMouse(int mouseX, int mouseY) {
		float minX, maxX, minY, maxY;
		minX = this.posX * this.scale; maxX = (this.posX + this.width) * this.scale;
		minY = this.posY * this.scale; maxY = (this.posY + this.height) * this.scale;
		return mouseX >= minX && mouseX < maxX && mouseY >= minY && mouseY < maxY;
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int mouseX, int mouseY) {
		return this.enabled && this.visible && isUnderMouse(mouseX, mouseY);
	}
}