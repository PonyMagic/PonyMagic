package net.braunly.ponymagic.client;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.client.renderer.MagicShieldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * Created by Simeon on 4/17/2015.
 */
public class RenderHandler
{
	
	private MagicShieldRenderer shieldRenderer;
	
	public static final Function<ResourceLocation, TextureAtlasSprite> modelTextureBakeFunc = new Function<ResourceLocation, TextureAtlasSprite>()
	{
		@Nullable
		@Override
		public TextureAtlasSprite apply(@Nullable ResourceLocation input)
		{
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
		}
	};
//	public static int stencilBuffer;

	public RenderHandler()
	{
		shieldRenderer = new MagicShieldRenderer();
	}

	public void init(World world, TextureManager textureManager) {
		OBJLoader.INSTANCE.addDomain(PonyMagic.MODID);

//		if (Minecraft.getMinecraft().getFramebuffer().enableStencil())
//		{
//			stencilBuffer = MinecraftForgeClient.reserveStencilBit();
//		}
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		shieldRenderer.onWorldRender(event);
	}

	public void registerModelTextures(TextureMap textureMap, OBJModel model)
	{
		model.getTextures().forEach(textureMap::registerSprite);
	}

	public OBJModel getObjModel(ResourceLocation location, ImmutableMap<String, String> customOptions)
	{
		try
		{
			OBJModel model = (OBJModel)OBJLoader.INSTANCE.loadModel(location);
			model = (OBJModel)model.process(customOptions);
			return model;
		}
		catch (Exception e)
		{
			PonyMagic.log.error("There was a problem while baking %s model", e);
		}
		return null;
	}
}
