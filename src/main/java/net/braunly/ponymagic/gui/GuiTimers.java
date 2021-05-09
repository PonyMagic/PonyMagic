package net.braunly.ponymagic.gui;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class GuiTimers extends GuiIngameForge {

    private IPlayerDataStorage playerData;
    private final ResourceLocation skillAvailable = new ResourceLocation(PonyMagic.MODID,
            "textures/gui/skill_available.png");
    private final ResourceLocation skillActive = new ResourceLocation(PonyMagic.MODID, "textures/gui/skill_active.png");

    public GuiTimers(Minecraft mc) {
        super(mc);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        if (mc.player.capabilities.isCreativeMode || mc.player.isSpectator())
            return;

        getPlayerData();
        if (this.playerData == null || !this.playerData.getTickData().isTicking())
            return;

        ScaledResolution resolution = event.getResolution();

        int xPos = resolution.getScaledWidth() / 2 - 95;
        int yPos = resolution.getScaledHeight() - 36;

//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glDisable(GL11.GL_LIGHTING);

//        GlStateManager.enableBlend();

        int c = 1;

        for (Map.Entry<String, Integer> entry : this.playerData.getTickData().getTimers().entrySet()) {
            ResourceLocation skillResLoc = new ResourceLocation(PonyMagic.MODID, "textures/gui/skills/" + entry.getKey() + ".png");
            int ticks = entry.getValue();
            String text = "" + ticks / 40;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float f = 1.0F;

            // Some distance between timer icons
            int x = xPos - 36 * c;
            if (c > 1) {
                x -= 5 * (c - 1) ;
            }

            // draw bg
//            mc.getTextureManager().bindTexture(this.skillAvailable);
//            drawModalRectWithCustomSizedTexture(x - 2, yPos - 2, 0, 0, 36, 36, 36, 36);

            // draw border as timer
            mc.getTextureManager().bindTexture(this.skillActive);
            drawModalRectWithCustomSizedTexture(x - 2, yPos - 2, 0, 0, 36, 36, 36, 36);

            // draw skill icon
            mc.getTextureManager().bindTexture(skillResLoc);
            drawModalRectWithCustomSizedTexture(x, yPos, 0, 0, 32, 32, 32, 32);

            GlStateManager.pushMatrix();
            float scaleFactor = 2.0F;
            GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
            mc.fontRenderer.drawString("§l" + text, (int) ((x + 6 - mc.fontRenderer.getStringWidth(text) / 2) / scaleFactor), (int)((yPos + 17) / scaleFactor), 16777215);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();

            c += 1;
        }
    }

    private void getPlayerData() {
//        PonyMagic.channel.sendToServer(new RequestPlayerDataPacket());
        this.playerData = PonyMagicAPI.getPlayerDataStorage(mc.player);
    }
}
