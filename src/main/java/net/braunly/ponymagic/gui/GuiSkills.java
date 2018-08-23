package net.braunly.ponymagic.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.swing.text.JTextComponent.KeyBinding;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.client.KeyBindings;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.network.packets.RequestPlayerDataPacket;
import net.braunly.ponymagic.network.packets.ResetPacket;
import net.braunly.ponymagic.network.packets.SkillUpPacket;
import net.braunly.ponymagic.race.EnumRace;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiSkills extends GuiScreen {

	private final ResourceLocation bg = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills_bg.png");
	private final ResourceLocation expBar = new ResourceLocation(PonyMagic.MODID, "textures/gui/exp_bar.png");
	private final ResourceLocation lvlUp = new ResourceLocation(PonyMagic.MODID, "textures/gui/lvl_up.png");
	private final ResourceLocation skillActive = new ResourceLocation(PonyMagic.MODID, "textures/gui/skill_active.png");
	private final ResourceLocation skillAvailable = new ResourceLocation(PonyMagic.MODID,
			"textures/gui/skill_available.png");
	private final ResourceLocation skillLearned = new ResourceLocation(PonyMagic.MODID,
			"textures/gui/skill_learned.png");
	private final ResourceLocation skillUnAvailable = new ResourceLocation(PonyMagic.MODID,
			"textures/gui/skill_unavailable.png");
	// Lines
	private final ResourceLocation line_uub = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_uub.png");
	private final ResourceLocation line_uug = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_uug.png");
	private final ResourceLocation line_ub = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_ub.png");
	private final ResourceLocation line_ug = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_ug.png");
	private final ResourceLocation line_hb = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_hb.png");
	private final ResourceLocation line_hg = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_hg.png");
	private final ResourceLocation line_db = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_db.png");
	private final ResourceLocation line_dg = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_dg.png");
	private final ResourceLocation line_ddb = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_ddb.png");
	private final ResourceLocation line_ddg = new ResourceLocation(PonyMagic.MODID, "textures/gui/line_ddg.png");

	private PlayerData playerData = null;
	private Set<GuiButtonSkill> skillsNet = null;
	private GuiButtonSkill skillClicked = null;

	@Override
	public void initGui() {
		// FIXME ?
		initPlayerData();

		// FIXME: on first open not showing skills
		if (this.playerData != null && this.playerData.race != EnumRace.REGULAR) {
			// Init skills net
			this.skillsNet = GuiSkillsNet.getInstance().getSkillNet(this.playerData.race);
			// Needs for actionPerformed function
			this.buttonList.addAll(this.skillsNet);
//			PonyMagic.log.info("[GUI] Skillnet inited!");
		} else {
			this.mc.displayGuiScreen(null);
		}

	}

	private void initPlayerData() {
		PonyMagic.channel.sendToServer(new RequestPlayerDataPacket());
		this.playerData = this.mc.player.getCapability(PlayerData.PLAYERDATA_CAPABILITY, null);
//		PonyMagic.log.info("[GUI] Player data inited!");
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		GuiButtonSkill skill = (GuiButtonSkill) button;
//		PonyMagic.log.info("[GUI] Action - " + skill.skillName);
		if (this.skillClicked == skill) {
			processButton(skill);
		} else {
			this.skillClicked = skill;
		}
	}

	@Nonnull
	private void processButton(GuiButtonSkill skill) {
		if (skill.skillName == "reset") {
//			PonyMagic.log.info("[GUI] Reset");
			PonyMagic.channel.sendToServer(new ResetPacket());
			initPlayerData();
			return;
		}

		// :FIXME: Move to SERVER side
		if (!isSkillLearned(skill) && isSkillAvailable(skill)) {
//			PonyMagic.log.info("[GUI] Send skillUp");
			PonyMagic.channel.sendToServer(new SkillUpPacket(skill.skillName, skill.skillLevel));
			initPlayerData();
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {      
        if (keyCode == KeyBindings.skills_gui.getKeyCode()) {
        	this.mc.displayGuiScreen(null);
        }
        super.keyTyped(typedChar, keyCode);
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		PonyMagic.log.info("[GUI] Start draw...");
		this.drawDefaultBackground();
		
		// Background
		int w = 496;
		int h = 334;

		int x = (this.width - w) / 2;
		int y = (this.height - h) / 2;

//		PonyMagic.log.info("[GUI] Draw BG");
		this.mc.getTextureManager().bindTexture(this.bg);
		drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, 512, 512); // 496.334

		// Draw exp bar
//		PonyMagic.log.info("[GUI] Draw expbar");
		int currentExp = (int) ((float) (this.playerData.levelData.getExp()
				/ ((this.playerData.levelData.getLevel() + 1) * Config.expPerLevel)) * 364);
		this.mc.getTextureManager().bindTexture(this.expBar);
		drawModalRectWithCustomSizedTexture(x + 67, y + 310, 0, 0, 364, 10, 364, 20);
		drawModalRectWithCustomSizedTexture(x + 67, y + 310, 0, 10, currentExp, 10, 364, 20);

		
		// Draw hovering text on exp bar
//		PonyMagic.log.info("[GUI] Draw hover expbar");
		if (mouseX > x + 67 && mouseX < x + 67 + 364 && mouseY > y + 310 && mouseY < y + 320) {
			List<String> temp = Arrays
					.asList(new TextComponentTranslation("gui.exp", String.format("%s", Math.round(this.playerData.levelData.getExp())),
							(this.playerData.levelData.getLevel() + 1) * Config.expPerLevel).getFormattedText());
			// Lightning shadow fix
			RenderHelper.enableStandardItemLighting();
			drawHoveringText(temp, mouseX, mouseY);
			RenderHelper.disableStandardItemLighting();
		}
		
//		try {
//			PonyMagic.log.info("[GUI] Draw skills");
			// Draw skills
			if (this.playerData.race != null && this.playerData.race != EnumRace.REGULAR && this.skillsNet != null) {
				// TODO: rewrite cycles with this.zIndex

				// Draw skills lines
				for (GuiButtonSkill skill : this.skillsNet) {

					// Skill button init
					if (PonyMagic.MAX_LVL >= skill.minLevel) {
						skill.initButton(this.mc, mouseX, mouseY, x, y, true);
					} else {
						skill.initButton(this.mc, mouseX, mouseY, x, y, false);
					}

					// // Lines
					if (!skill.lines.isEmpty()) {
						for (String itLines : skill.lines) {
							boolean lineActive = false;
							GuiButtonSkill lineSkill = GuiSkillsNet.getInstance().getRaceSkill(this.playerData.race,
									itLines);
							if (this.isSkillLearned(skill) && this.isSkillLearned(lineSkill)) {
								lineActive = true;
							}

							int lineDirection = lineSkill.posY - skill.posY;

							switch (lineDirection) {
							case -64:
								this.mc.getTextureManager().bindTexture(lineActive ? this.line_uug : this.line_uub);
								drawModalRectWithCustomSizedTexture(skill.posX + 32, skill.posY - 48, 0, 0, 32, 64, 32, 64);
								break;
							case -32:
								this.mc.getTextureManager().bindTexture(lineActive ? this.line_ug : this.line_ub);
								drawModalRectWithCustomSizedTexture(skill.posX + 32, skill.posY - 16, 0, 0, 32, 32, 32, 32);
								break;
							case 0:
								this.mc.getTextureManager().bindTexture(lineActive ? this.line_hg : this.line_hb);
								drawModalRectWithCustomSizedTexture(skill.posX + 16, skill.posY, 0, 0, 64, 32, 64, 32);
								break;
							case 32:
								this.mc.getTextureManager().bindTexture(lineActive ? this.line_dg : this.line_db);
								drawModalRectWithCustomSizedTexture(skill.posX + 32, skill.posY + 16, 0, 0, 32, 32, 32, 32);
								break;
							case 64:
								this.mc.getTextureManager().bindTexture(lineActive ? this.line_ddg : this.line_ddb);
								drawModalRectWithCustomSizedTexture(skill.posX + 32, skill.posY + 16, 0, 0, 32, 64, 32, 64);
								break;
							default:
								break;
							}
						}
					}
				}

				// Draw skills background
				for (GuiButtonSkill skill : this.skillsNet) {
					// Skill background
					if (this.isSkillLearned(skill)) {
						this.mc.getTextureManager().bindTexture(this.skillLearned);
					} else if (this.skillClicked == skill) {
						this.mc.getTextureManager().bindTexture(this.skillActive);
					} else if (this.isSkillAvailable(skill)) {
						this.mc.getTextureManager().bindTexture(this.skillAvailable);
					} else {
						this.mc.getTextureManager().bindTexture(this.skillUnAvailable);
					}
					drawModalRectWithCustomSizedTexture(skill.posX - 2, skill.posY - 2, 0, 0, 36, 36, 36, 36);

					// Draw icon
					skill.drawButton();
				}

				// Showing info for skill
				for (GuiButtonSkill skill : this.skillsNet) {

					if (skill.isUnderMouse(mouseX, mouseY) && skill.knownSkill) {
						String[] text = {
								new TextComponentTranslation("gui.skill.name").getFormattedText()
										+ this.playerData.race.getColor()
										+ new TextComponentTranslation(
												"skill." + skill.skillName + skill.skillLevel + ".name").getFormattedText(),
								new TextComponentTranslation("gui.skill.usage").getFormattedText()
										+ new TextComponentTranslation(
												"skill." + skill.skillName + skill.skillLevel + ".command")
														.getFormattedText(),
								new TextComponentTranslation("skill." + skill.skillName + skill.skillLevel + ".descr")
										.getFormattedText(),
								// new TextComponentTranslation("skill.stamina", Config.).getFormattedText(),

						};

						List<String> skillHoverText = Arrays.asList(text);
						// Lightning shadow fix
						RenderHelper.enableStandardItemLighting();
						drawHoveringText(skillHoverText, mouseX, mouseY);
						RenderHelper.disableStandardItemLighting();
					}
				}
			}
//		} catch (NullPointerException e) {
//			PonyMagic.log.info("[GUI] ERROR - NullPointerException");
//			this.mc.displayGuiScreen(null);
//		}
		
		// Draw player level and free points
		// FIXME: ?
		drawCenteredString(this.fontRenderer,
				new TextComponentTranslation("gui.level", this.playerData.levelData.getLevel()).getFormattedText()
						+ "                    "
						+ new TextComponentTranslation("gui.freeskillpoints",
								this.playerData.levelData.getFreeSkillPoints()).getFormattedText(),
				x + 250, y + 300, 16773290);
	}

	private boolean isSkillLearned(GuiButtonSkill skill) {
		return this.playerData.skillData.getSkillLevel(skill.skillName) >= skill.skillLevel;
	}

	private boolean isSkillAvailable(GuiButtonSkill skill) {
		boolean dependencies = true;

		// Check for learned dependencies
		if (skill.depends != null) {
			for (String skillName : skill.depends) {
				if (skillName.contains("#")) {
					String[] parts = skillName.split("#");
					if (this.playerData.skillData.isSkillLearned(parts[0])
							&& this.playerData.skillData.getSkillLevel(parts[0]) >= Integer.parseInt(parts[1])) {
						dependencies = true;
						break;
					}
				} else if (this.playerData.skillData.isSkillLearned(skillName)) {
					dependencies = true;
					break;
				}
				dependencies = false;
			}
		}

		// Check for free skill points and player level and current skill level (need
		// for stamina branch)
		if (dependencies && this.playerData.levelData.getLevel() >= skill.minLevel
				&& this.playerData.levelData.getFreeSkillPoints() > 0
				&& this.playerData.skillData.getSkillLevel(skill.skillName) - skill.skillLevel == -1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
