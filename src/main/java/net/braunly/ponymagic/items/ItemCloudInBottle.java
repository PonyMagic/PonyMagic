package net.braunly.ponymagic.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCloudInBottle extends ItemBase {

	public ItemCloudInBottle(String name) {
		super(name);
		setMaxStackSize(64);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		}
		BlockPos blockPos = player.getPosition();
		blockPos = blockPos.down(1);
		if (world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
			world.setBlockState(blockPos, Block.getBlockFromItem(player.getHeldItemOffhand().getItem()).getDefaultState());

			player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
			player.getHeldItemOffhand().setCount(player.getHeldItemOffhand().getCount() - 1);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

}
