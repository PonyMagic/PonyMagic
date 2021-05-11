package net.braunly.ponymagic.items;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSpellBook extends ItemBase{

	private final String spellName;

	public ItemSpellBook(String name, String spellName) {
		super(name);
		this.setUnlocalizedName(name + "_" + spellName);
		setMaxStackSize(1);

		this.spellName = spellName;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
		}
		IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
		if (playerData.getRace().hasSpell(this.spellName) &&
			!playerData.getSkillData().isSkillLearned(this.spellName)
		) {
			playerData.getSkillData().upSkillLevel(this.spellName);

			ItemStack itemStackInHand = player.getHeldItem(hand);
			itemStackInHand.setCount(itemStackInHand.getCount() - 1);
			PonyMagicAPI.playerDataController.savePlayerData(playerData);
			return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

}
