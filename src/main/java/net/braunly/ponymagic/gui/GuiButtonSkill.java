package net.braunly.ponymagic.gui;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.braunly.ponymagic.PonyMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonSkill extends GuiButton {
	private Minecraft mc;
	private ResourceLocation resLoc;
	public boolean knownSkill;
	public String skillName;
	public Set<String> depends;
	public Set<String> lines;
	public int posX;
	public int posY;
	public int minLevel;
	public int skillLevel;

	// public GuiButtonSkill (String name, int id, int x, int y) {
	// this(name, null, id, x, y, null, 1, 5);
	// }
	//
	// public GuiButtonSkill (String name, String[] depends, int id, int x, int y) {
	// this(name, depends, id, x, y, null, 1);
	// }

	public GuiButtonSkill(String name, int id, int x, int y, int skillLevel, int minLevel) {
		this(name, ImmutableSet.of(), id, x, y, ImmutableSet.of(), skillLevel, minLevel);
	}

	public GuiButtonSkill(String name, Set<String> depends, int id, int x, int y, int skillLevel, int minLevel) {
		this(name, depends, id, x, y, ImmutableSet.of(), skillLevel, minLevel);
	}

	public GuiButtonSkill(String name, Set<String> depends, int id, int x, int y, Set<String> lines, int skillLevel,
			int minLevel) {
		super(id, x, y, 32, 32, "");
		this.skillName = name;
		this.lines = lines;
		this.minLevel = minLevel;
		this.skillLevel = skillLevel;
		this.depends = depends;
	}

	public void initButton(Minecraft mc, int mouseX, int mouseY, int x, int y, boolean knownSkill) {
		this.mc = mc;
		this.knownSkill = knownSkill;
		this.resLoc = new ResourceLocation(PonyMagic.MODID,
				"textures/gui/skills/" + (this.knownSkill ? this.skillName : "unknown") + ".png");
		this.posX = x + this.x;
		this.posY = y + this.y - 32;
	}

	public void drawButton() {
		if (this.visible) {
			this.mc.getTextureManager().bindTexture(this.resLoc);
			drawModalRectWithCustomSizedTexture(this.posX, this.posY, 0, 0, 32, 32, 32, 32);
		}
	}

	public boolean isUnderMouse(int mouseX, int mouseY) {
		return mouseX >= this.posX && mouseY >= this.posY && mouseX < (float) this.posX + (float) this.width
				&& mouseY < (float) this.posY + (float) this.height;
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int mouseX, int mouseY) {
		return this.enabled && this.visible && isUnderMouse(mouseX, mouseY); // mouseX >= this.xPosition && mouseY >=
																				// this.yPosition && (float)mouseX <
																				// (float)this.xPosition +
																				// (float)this.width * GuiStats.SCALE &&
																				// (float)mouseY < (float)this.yPosition
																				// + (float)this.height *
																				// GuiStats.SCALE;
	}
}