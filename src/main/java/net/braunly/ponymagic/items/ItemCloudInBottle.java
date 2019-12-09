package net.braunly.ponymagic.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
		ItemStack itemStackInHand = player.getHeldItemOffhand();
		Item itemInHand = itemStackInHand.getItem();
		Block blockInHand = Block.getBlockFromItem(itemInHand);
		if (world.getBlockState(blockPos).getBlock() == Blocks.AIR 
				&& itemInHand instanceof ItemBlock
				&& !(blockInHand instanceof BlockFalling)
				&& blockInHand.isFullBlock(blockInHand.getDefaultState())) {
			world.setBlockState(blockPos, blockInHand.getStateFromMeta(itemInHand.getMetadata(itemStackInHand)));

			player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
			itemStackInHand.setCount(itemStackInHand.getCount() - 1);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

}
