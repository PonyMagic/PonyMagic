package com.tmtravlr.potioncore.potion;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPotionCorePotion extends RenderSnowball
{
    public RenderPotionCorePotion()
    {
        super(ItemPotionCorePotion.instance);
    }

    public void doRender(EntityPotionCorePotion potionEntity, double x, double y, double z, float pitch, float yaw)
    {
        IIcon iicon = ItemPotion.func_94589_d("bottle_splash");

        if (iicon != null)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.bindEntityTexture(potionEntity);
            Tessellator tessellator = Tessellator.instance;

            int liquidColour = PotionCoreHelper.getCustomPotionColor(ItemPotionCorePotion.instance.getEffects(potionEntity.potion == null ? new ItemStack(ItemPotionCorePotion.instance) : potionEntity.potion));
            float red = (float)(liquidColour >> 16 & 255) / 255.0F;
            float green = (float)(liquidColour >> 8 & 255) / 255.0F;
            float blue = (float)(liquidColour & 255) / 255.0F;
            GL11.glColor3f(red, green, blue);
            GL11.glPushMatrix();
            this.renderPotion(tessellator, ItemPotion.func_94589_d("overlay"));
            GL11.glPopMatrix();
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            

            this.renderPotion(tessellator, iicon);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    private void renderPotion(Tessellator p_77026_1_, IIcon p_77026_2_)
    {
        float f = p_77026_2_.getMinU();
        float f1 = p_77026_2_.getMaxU();
        float f2 = p_77026_2_.getMinV();
        float f3 = p_77026_2_.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        p_77026_1_.startDrawingQuads();
        p_77026_1_.setNormal(0.0F, 1.0F, 0.0F);
        p_77026_1_.addVertexWithUV((double)(0.0F - f5), (double)(0.0F - f6), 0.0D, (double)f, (double)f3);
        p_77026_1_.addVertexWithUV((double)(f4 - f5), (double)(0.0F - f6), 0.0D, (double)f1, (double)f3);
        p_77026_1_.addVertexWithUV((double)(f4 - f5), (double)(f4 - f6), 0.0D, (double)f1, (double)f2);
        p_77026_1_.addVertexWithUV((double)(0.0F - f5), (double)(f4 - f6), 0.0D, (double)f, (double)f2);
        p_77026_1_.draw();
    }
    
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double x, double y, double z, float pitch, float yaw) {
    	doRender((EntityPotionCorePotion)entity, x, y, z, pitch, yaw);
    }
}
