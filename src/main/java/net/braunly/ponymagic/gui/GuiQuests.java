package net.braunly.ponymagic.gui;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.client.KeyBindings;
import net.braunly.ponymagic.network.packets.RequestPlayerDataPacket;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuiQuests extends GuiIngameForge {
    private IPlayerDataStorage playerData;

    public GuiQuests(Minecraft mc) {
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
        if (this.playerData == null || this.playerData.getLevelData().getCurrentGoals().isEmpty())
            return;

        ScaledResolution resolution = event.getResolution();

        int xPos = 10;
        int yPos = 10;
        int yShift = 0;

//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glDisable(GL11.GL_LIGHTING);

//        GlStateManager.enableBlend();


        for (Map.Entry<String, HashMap<String, Integer>> questEntry : this.playerData.getLevelData().getCurrentGoals().entrySet()) {
            String questName = I18n.format("quest." + questEntry.getKey() + ".name");
            String questString = I18n.format("gui.quest.quest", questName);

            GlStateManager.pushMatrix();
            float scaleFactor = 2.0F;
            GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
            mc.fontRenderer.drawString("§l" + questString, (int) (xPos / scaleFactor), (int)((yPos + yShift) / scaleFactor), 16777215);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
            yShift += 15;

            for (Map.Entry<String, Integer> goalEntry : questEntry.getValue().entrySet()) {
                String goalString = I18n.format(
                        "gui.quest.goal",
                        QuestGoalUtils.getLocalizedGoalName(goalEntry.getKey()),
                        goalEntry.getValue()
                );

                GlStateManager.pushMatrix();
                GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
                mc.fontRenderer.drawString("§l" + goalString, (int) (xPos / scaleFactor), (int)((yPos + yShift) / scaleFactor), 16777215);
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.popMatrix();
                yShift += 15;
            }
            yShift += 15;
        }
    }

    private void getPlayerData() {
        PonyMagic.channel.sendToServer(new RequestPlayerDataPacket());
        this.playerData = PonyMagicAPI.getPlayerDataStorage(mc.player);
    }
}
