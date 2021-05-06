package net.braunly.ponymagic.entity;

import com.google.common.base.Optional;
import me.braunly.ponymagic.api.enums.EnumRace;
import net.braunly.ponymagic.config.SkillConfig;
import net.braunly.ponymagic.skill.Skill;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityPortal extends Entity
{
    private static final DataParameter<Optional<BlockPos>> TARGET = EntityDataManager.createKey(EntityPortal.class, DataSerializers.OPTIONAL_BLOCK_POS);
    /** Used to create the rotation animation when rendering the portal. */
    public int innerRotation;

    public EntityPortal(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(2.0F, 2.0F);
        this.innerRotation = this.rand.nextInt(100000);
    }

    public EntityPortal(World worldIn, double x, double y, double z, BlockPos target, String name) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setTarget(target);
        this.setCustomNameTag(TextFormatting.BOLD + name);
        this.setAlwaysRenderNameTag(true);
    }

    protected void entityInit() {
        this.getDataManager().register(TARGET, Optional.absent());
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;

        Skill skillConfig = SkillConfig.getRaceSkill(EnumRace.UNICORN, "portal", 1);
        if (this.ticksExisted > skillConfig.getSpellData().get("duration")) {
            this.setDead();
        }
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        if (this.getTarget() != null)
        {
            compound.setTag("Target", NBTUtil.createPosTag(this.getTarget()));
        }
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("Target", 10))
        {
            this.setTarget(NBTUtil.getPosFromTag(compound.getCompoundTag("Target")));
        }
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.world.isRemote)
            {
                this.setDead();
            }

            return true;
        }
    }

    public void setTarget(@Nullable BlockPos target) {
        this.getDataManager().set(TARGET, Optional.fromNullable(target));
    }

    @Nullable
    public BlockPos getTarget() {
        return this.getDataManager().get(TARGET).orNull();
    }
}