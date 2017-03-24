package net.braunly.ponymagic.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tmtravlr.potioncore.PotionCoreEffects;
import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.data.LevelData;
import net.braunly.ponymagic.data.PlayerData;
import net.braunly.ponymagic.data.PlayerDataController;
import net.braunly.ponymagic.exp.Exp;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;

public class CommandMagic extends CommandBase 
{	
	@Override
	public List getCommandAliases()
	{
		List aliases = new ArrayList();
	    aliases.add("magic");
	    return aliases;
	}
	
	@Override
	public String getCommandName() {
		return "magic";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "commands.magic.usage";
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		if (commandSender instanceof EntityPlayer) {
			
			EntityPlayerMP player = (EntityPlayerMP) commandSender;
			
			for (String s : args) {
				System.out.println(s);
			}
			
			
//			Potion potion = PotionCoreHelper.potions.get("potion.levitate");
//			int range = 5;
//			List<EntityLivingBase> entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(range, range, range));
//			
//			for (EntityLivingBase e: entities) {
//				if (e instanceof EntityPlayer) continue;
//				e.addPotionEffect(new PotionEffect(potion.getId(), 1200, 10));
//			}
	    	
		} else {
			System.out.println("NONE");
		}
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2){
		return null;
	}
		
}