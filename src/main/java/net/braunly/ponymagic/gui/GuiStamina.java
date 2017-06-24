package net.braunly.ponymagic.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;


public class GuiStamina extends GuiIngameForge 
{
	
	private static Minecraft mc;
	private static final ResourceLocation texturepath = new ResourceLocation(PonyMagic.MODID, "textures/gui/stamina_bar.png");

	public GuiStamina(Minecraft mc) {
		super(mc);
		GuiStamina.mc = mc;
		
	}
	
	@SubscribeEvent
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE)
		{
			return;
		}

		StaminaPlayer props = StaminaPlayer.get(mc.thePlayer);

		if (props == null || mc.thePlayer.capabilities.isCreativeMode) return;
		
//		int xPos = 2;
//		int yPos = 2;
		
		int barWidth = 182;

		int xPos = event.resolution.getScaledWidth() / 2 - 91;
		int yPos = event.resolution.getScaledHeight() - 32 + 5;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.mc.getTextureManager().bindTexture(texturepath);
		
		int current = (int)((float)(props.getStaminaValue(StaminaType.CURRENT) / props.getStaminaValue(StaminaType.MAXIMUM)) * barWidth);
		
		drawTexturedModalRect(xPos, yPos, 0, 3, barWidth + 3, 3);
		
		drawTexturedModalRect(xPos, yPos, 0, 0, current, 3);
	
	}
}
