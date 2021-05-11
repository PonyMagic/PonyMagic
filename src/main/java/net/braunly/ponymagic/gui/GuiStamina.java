package net.braunly.ponymagic.gui;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumStaminaType;
import me.braunly.ponymagic.api.interfaces.IStaminaStorage;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class GuiStamina extends GuiIngameForge {

	private static final ResourceLocation TEXTURE_PATH = new ResourceLocation(PonyMagic.MODID,
			"textures/gui/stamina_bar.png");

	public GuiStamina(Minecraft mc) {
		super(mc);
	}

	@SubscribeEvent
	public void onRenderExperienceBar(RenderGameOverlayEvent event) {
		if (event.isCancelable() || event.getType() != ElementType.EXPERIENCE) {
			return;
		}

		IStaminaStorage stamina = PonyMagicAPI.getStaminaStorage(mc.player);

		if (stamina == null || mc.player.capabilities.isCreativeMode || mc.player.isSpectator())
			return;

		int barWidth = 182;

		int xPos = event.getResolution().getScaledWidth() / 2 - 91;
		int yPos = event.getResolution().getScaledHeight() - 32 + 5;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.getTextureManager().bindTexture(TEXTURE_PATH);

		int current = (int) (stamina.getStamina(EnumStaminaType.CURRENT) / stamina.getStamina(EnumStaminaType.MAXIMUM)
				* barWidth);

		drawTexturedModalRect(xPos, yPos, 0, 3, barWidth + 3, 3);

		drawTexturedModalRect(xPos, yPos, 0, 0, current, 3);

	}
}
