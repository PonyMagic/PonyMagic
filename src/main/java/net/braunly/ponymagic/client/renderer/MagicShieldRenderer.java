package net.braunly.ponymagic.client.renderer;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.client.Color;
import net.braunly.ponymagic.potions.PotionShield;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;

import static org.lwjgl.opengl.GL11.GL_ONE;

//import net.braunly.ponymagic.client.RenderUtils;

/**
 * Created by Simeon on 6/7/2015.
 */
public class MagicShieldRenderer {
	
	public static final ResourceLocation model_path = new ResourceLocation(PonyMagic.MODID, "models/shield_sphere.obj");
	private static final ResourceLocation forcefield_damage_tex = new ResourceLocation(PonyMagic.MODID, "textures/fx/shield_damage.png");
	private static final ResourceLocation forcefield_tex = new ResourceLocation(PonyMagic.MODID, "textures/fx/forcefield_plasma.png");
	private static final ResourceLocation forcefield_plasma_tex = new ResourceLocation(PonyMagic.MODID, "textures/fx/forcefield_plasma_2.png");
	private static final ResourceLocation shield_texture = new ResourceLocation(PonyMagic.MODID, "textures/fx/shield.png");
	IModel normal_sphere;
	IModel shield_model;
	
	private final Color COLOR_HOLO = new Color(169, 226, 251);

	private float opacityLerp;

	public MagicShieldRenderer()
	{
		try
		{
            shield_model = OBJLoader.INSTANCE.loadModel(model_path);
            normal_sphere = OBJLoader.INSTANCE.loadModel(new ResourceLocation(PonyMagic.MODID, "models/block/sphere.obj"));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
		opacityLerp = 0;
	}

	public void onWorldRender(RenderWorldLastEvent event)
	{
		for (Object entity : Minecraft.getMinecraft().world.playerEntities)
		{
			renderPlayerShield(event, (EntityPlayer)entity);
		}
	}

	private void renderPlayerShield(RenderWorldLastEvent event, EntityPlayer player)
	{
		boolean isVisible = manageOpacityLerp(player, event.getPartialTicks());

		if (isVisible)
		{
			double time = Minecraft.getMinecraft().world.getWorldTime();

			GlStateManager.pushMatrix();
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.disableCull();
			GlStateManager.blendFunc(GL_ONE, GL_ONE);
			Vec3d playerPosition = player.getPositionEyes(event.getPartialTicks());
			Vec3d clientPosition = Minecraft.getMinecraft().player.getPositionEyes(event.getPartialTicks());
			applyColorWithMultipy(COLOR_HOLO, 0.2f * getOpacityLerp(player));
			Minecraft.getMinecraft().renderEngine.bindTexture(shield_texture);
			if (!isClient(player))
			{
				GlStateManager.translate(0, player.height - 0.5, 0);
				GlStateManager.enableCull();
			}
			else
			{
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0)
				{
					GlStateManager.enableCull();
				}
			}
			GlStateManager.translate(playerPosition.x - clientPosition.x, playerPosition.y - clientPosition.y, playerPosition.z - clientPosition.z);
			GlStateManager.translate(0, -0.5, 0);
			GlStateManager.scale(3, 3, 3);
			GlStateManager.rotate((float)player.motionZ * 45, -1, 0, 0);
			GlStateManager.rotate((float)player.motionX * 45, 0, 0, 1);
			//shield_model.renderAll();

			GlStateManager.disableCull();
//			renderAttacks(player);

			applyColorWithMultipy(COLOR_HOLO, 0.1f * getOpacityLerp(player));
			Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_tex);
			GlStateManager.scale(1.02, 1.02, 1.02);
			//normal_sphere.renderAll();
			applyColorWithMultipy(COLOR_HOLO, 0.05f * getOpacityLerp(player));
			GlStateManager.pushMatrix();
			GlStateManager.rotate((float)time * 0.005f, (float)Math.sin(time * 0.01), (float)Math.cos(time * 0.01), 0);
			Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_plasma_tex);
			GlStateManager.scale(1.01, 1.01, 1.01);
			//normal_sphere.renderAll();
			GlStateManager.popMatrix();
			GlStateManager.disableBlend();
			GlStateManager.enableCull();
			GlStateManager.enableAlpha();
			GlStateManager.depthMask(true);
			GlStateManager.popMatrix();
		}
	}

	private boolean manageOpacityLerp(EntityPlayer player, float partialTicks)
	{
		if (player.isPotionActive(PotionShield.instance))
		{
			if (isClient(player))
			{
				if (opacityLerp < 1)
				{
					opacityLerp = Math.min(1, opacityLerp + partialTicks * 0.1f);
				}
			}

			return true;
		}
		else
		{
			if (isClient(player) && opacityLerp > 0)
			{
				opacityLerp = Math.max(0, opacityLerp - partialTicks * 0.2f);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	private boolean isClient(EntityPlayer player)
	{
		return player == Minecraft.getMinecraft().player;
	}

	private float getOpacityLerp(EntityPlayer player)
	{
		if (Minecraft.getMinecraft().player == player)
		{
			return opacityLerp;
		}
		return 1;
	}
	
	public void applyColorWithMultipy(Color color, float mul)
	{
		GlStateManager.color(color.getFloatR() * mul, color.getFloatG() * mul, color.getFloatB() * mul);
	}

//	private void renderAttacks(EntityPlayer player)
//	{
//		float opacity = getOpacityLerp(player);
//        /*if (androidPlayer.hasEffect(AndroidPlayer.NBT_HITS))
//        {
//            NBTTagList hits = androidPlayer.getEffectTagList(AndroidPlayer.NBT_HITS, 10);
//            for (int i = 0; i < hits.tagCount(); i++) {
//                renderAttack(new Vector3f(hits.getCompoundTagAt(i).getFloat("x"), -hits.getCompoundTagAt(i).getFloat("y"), -hits.getCompoundTagAt(i).getFloat("z")).normalise(null), (hits.getCompoundTagAt(i).getInteger("time") / 10f) * opacity);
//            }
//        }*/
//	}
//
//	private void renderAttack(Vector3f dir, float percent)
//	{
//		GlStateManager.pushMatrix();
//		Vector3f front = new Vector3f(1, 0, 0);
//		Vector3f c = Vector3f.cross(dir, front, null);
//		double omega = Math.acos(Vector3f.dot(dir, front));
//		GlStateManager.rotate((float)(omega * (180 / Math.PI)), c.x, c.y, c.z);
//		RenderUtils.applyColorWithMultipy(COLOR_HOLO, 1 * percent);
//		Minecraft.getMinecraft().renderEngine.bindTexture(forcefield_damage_tex);
//		//normal_sphere.renderAll();
//		GlStateManager.popMatrix();
//	}
}
