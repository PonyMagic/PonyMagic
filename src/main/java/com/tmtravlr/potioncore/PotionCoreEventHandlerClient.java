package com.tmtravlr.potioncore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.tmtravlr.potioncore.PotionCoreEffects.PotionData;
import com.tmtravlr.potioncore.effects.PotionDrown;
import com.tmtravlr.potioncore.effects.PotionPerplexity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@SideOnly(Side.CLIENT)
public class PotionCoreEventHandlerClient {

	public static boolean inverted = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderAir(RenderGameOverlayEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(event.type == RenderGameOverlayEvent.ElementType.AIR) {

			EntityLivingBase player = mc.renderViewEntity;
			PotionData drown = PotionCoreEffects.potionMap.get(PotionDrown.NAME);

			if (player != null && drown != null && drown.potion != null && player.isPotionActive(drown.potion)) {
				event.setCanceled(true);

				if(!player.isInsideOfMaterial(Material.water)) {
					int air = player.getEntityData().getInteger(PotionDrown.TAG_NAME);

					mc.mcProfiler.startSection("air");
					GL11.glEnable(GL11.GL_BLEND);
					int width = event.resolution.getScaledWidth();
					int height = event.resolution.getScaledHeight();
					int left = width / 2 + 91;
					int top = height - GuiIngameForge.right_height;

					int full = MathHelper.ceiling_double_int((double)(air - 2) * 10.0D / 300.0D);
					int partial = MathHelper.ceiling_double_int((double)air * 10.0D / 300.0D) - full;

					for (int i = 0; i < full + partial; ++i)
					{
						mc.ingameGUI.drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
					}
					GuiIngameForge.right_height += 10;


					GL11.glDisable(GL11.GL_BLEND);
					mc.mcProfiler.endSection();
				}
			}
		}
	}

	@SubscribeEvent
	public void onFogRender(FogDensity event) {

		if(ConfigLoader.fixBlindness) {
			Minecraft mc = Minecraft.getMinecraft();

			if(event.entity == mc.thePlayer && mc.thePlayer.isPotionActive(Potion.blindness)) {
				float f1 = 5.0F;
				int duration = mc.thePlayer.getActivePotionEffect(Potion.blindness).getDuration();
				int amplifier = mc.thePlayer.getActivePotionEffect(Potion.blindness).getAmplifier()+1;

				if (duration < 20)
				{
					f1 = 5.0F + (mc.gameSettings.renderDistanceChunks * 16 - 5.0F) * (1.0F - (float)duration / 20.0F);
				}

				float multiplier = 0.25F / amplifier;

				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

				GL11.glFogf(GL11.GL_FOG_START, f1 * multiplier);
                GL11.glFogf(GL11.GL_FOG_END, f1 * multiplier*4);


				if (GLContext.getCapabilities().GL_NV_fog_distance)
				{
					GL11.glFogi(34138, 34139);
				}

				event.density = 2.0f;
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		NBTTagCompound persisted = new NBTTagCompound();

		if(player != null) {
			persisted = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			if(!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
				player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persisted);
			}
		}

		if (mc.theWorld != null && player != null && PotionPerplexity.instance != null && player.isPotionActive(PotionPerplexity.instance)) {
			if(!inverted) {
				saveInverted(true);

				KeyBinding temp = mc.gameSettings.keyBindForward;

				mc.gameSettings.keyBindForward = mc.gameSettings.keyBindBack;
				mc.gameSettings.keyBindBack = temp;

				temp = mc.gameSettings.keyBindLeft;
				mc.gameSettings.keyBindLeft = mc.gameSettings.keyBindRight;
				mc.gameSettings.keyBindRight = temp;

				temp = mc.gameSettings.keyBindSneak;
				mc.gameSettings.keyBindSneak = mc.gameSettings.keyBindJump;
				mc.gameSettings.keyBindJump = temp;

				mc.gameSettings.invertMouse = !mc.gameSettings.invertMouse;

				mc.gameSettings.saveOptions();
			}
		}
		else {
			if(inverted) {

				saveInverted(false);
				
				KeyBinding temp = mc.gameSettings.keyBindForward;
				mc.gameSettings.keyBindForward = mc.gameSettings.keyBindBack;
				mc.gameSettings.keyBindBack = temp;

				temp = mc.gameSettings.keyBindLeft;
				mc.gameSettings.keyBindLeft = mc.gameSettings.keyBindRight;
				mc.gameSettings.keyBindRight = temp;

				temp = mc.gameSettings.keyBindSneak;
				mc.gameSettings.keyBindSneak = mc.gameSettings.keyBindJump;
				mc.gameSettings.keyBindJump = temp;

				mc.gameSettings.invertMouse = !mc.gameSettings.invertMouse;

				mc.gameSettings.saveOptions();
			}
		}
	}

	public static void loadInverted() {
		File options = ObfuscationReflectionHelper.getPrivateValue(GameSettings.class, Minecraft.getMinecraft().gameSettings, "optionsFile", "field_74354_ai");

		if(!options.exists()) {
			return;
		}

		try
		{
			BufferedReader bufferedreader = new BufferedReader(new FileReader(options));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				try
				{
					String[] astring = s.split(":");

					if (astring[0].equals("inverted"))
					{
						inverted = Boolean.valueOf(astring[1]);
					}
				}
				catch (Exception var8)
				{
					FMLLog.warning("Skipping bad option: " + s);
				}
			}
			bufferedreader.close();
		}
		catch (Exception exception)
		{
			FMLLog.severe((String)"Failed to load options", (Throwable)exception);
		}
	}

	public static void saveInverted(boolean toSave) {

		inverted = toSave;

		if (FMLClientHandler.instance().isLoading()) return;
		try
		{
			File options = ObfuscationReflectionHelper.getPrivateValue(GameSettings.class, Minecraft.getMinecraft().gameSettings, "optionsFile", "field_74354_ai");

			if(!options.exists()) {
				return;
			}

			PrintWriter printwriter = new PrintWriter(new FileWriter(options));
			printwriter.println("inverted:" + inverted);
			printwriter.close();
		}
		catch (Exception exception)
		{
			FMLLog.warning((String)"Failed to save options", (Throwable)exception);
		}
	}
}
