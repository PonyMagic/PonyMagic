package net.braunly.ponymagic.gui;

import lombok.Getter;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiQuests {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;

    private IPlayerDataStorage playerData;
    @Getter
    private static boolean isGuiOpen = false;

    public GuiQuests(Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = this.mc.fontRenderer;
    }

    public static void openGui() {
        GuiQuests.isGuiOpen = true;
    }

    public static void closeGui() {
        GuiQuests.isGuiOpen = false;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (!GuiQuests.isGuiOpen || event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        if (this.mc.player.capabilities.isCreativeMode || this.mc.player.isSpectator())
            return;

        getPlayerData();
        if (this.playerData == null || this.playerData.getLevelData().getCurrentGoals().isEmpty())
            return;

        int xPos = 10;
        int yPos = 10;
        int yShift = 0;

        for (Map.Entry<String, HashMap<String, Integer>> questEntry : this.playerData.getLevelData().getCurrentGoals().entrySet()) {
            String questName = I18n.format("quest." + questEntry.getKey() + ".name");
            String questString = I18n.format("gui.quest.quest", questName);

            GlStateManager.pushMatrix();
            float scaleFactor = 1.25F;
            GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
            this.fontRenderer.drawStringWithShadow("§6§l" + questString, (int) (xPos / scaleFactor), (int)((yPos + yShift) / scaleFactor), 16777215);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
            yShift += 10;

            for (Map.Entry<String, Integer> goalEntry : questEntry.getValue().entrySet()) {
                String goalString = I18n.format(
                        "gui.quest.goal",
                        QuestGoalUtils.getLocalizedGoalName(goalEntry.getKey()),
                        goalEntry.getValue()
                );

                GlStateManager.pushMatrix();
                GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
                this.fontRenderer.drawStringWithShadow(goalString, (int) (xPos / scaleFactor), (int)((yPos + yShift) / scaleFactor), 16777215);
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.popMatrix();
                yShift += 10;
            }
            yShift += 10;
        }
    }

    private void getPlayerData() {
        this.playerData = PonyMagicAPI.getPlayerDataStorage(mc.player);
    }
}
