package com.becks.interaction_boxes.common.exampleBlocks.lockPedestal;

import com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes.HorizontalFlatItem;
import com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes.VerticalFlatItem;
import com.becks.interaction_boxes.common.functional.itemBoxPuttableItems.NoBlockItems;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox.SingleItemInventoryBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox.LockItemBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.lockingItemBoxBlock.LockingItemBoxBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import com.becks.interaction_boxes.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import static net.minecraft.world.phys.shapes.BooleanOp.OR;

/**
 * example implementation of a LockingItemBoxBlock
 */
public class LockPedestal extends LockingItemBoxBlock {
	private final SingleItemInventoryBox itemBox = new SingleItemInventoryBox(Shapes.or(Block.box(4.5D, 13.49D, 4.5D, 11.5, 14.5, 11.5)), 0, new HorizontalFlatItem(), new NoBlockItems());
	private final LockItemBox lockBox = new LockItemBox(itemBox,Shapes.or(Block.box(6D, 6D, 3.9D, 10, 10, 5)), 1, new VerticalFlatItem(), new NoBlockItems());
	private final VoxelShape SHAPE  = Stream.of(
			Block.box(2, 0, 2, 14, 2, 14),
			Block.box(3, 2, 3, 13, 4, 13),
			Block.box(4, 4, 4, 12, 11, 12),
			Block.box(3, 11, 3, 13, 13, 13),
			Block.box(2.5, 13, 4, 13.5, 13.5, 12),
			Block.box(13, 8, 4, 13.5, 13, 12),
			Block.box(2.5, 8, 4, 3, 13, 12)
			).reduce((v1, v2) -> {return Shapes.join(v1, v2, OR);}).get();
	private final VoxelShape SHAPE_1  = Stream.of(
			Block.box(2, 0, 2, 14, 2, 14),
			Block.box(3, 2, 3, 13, 4, 13),
			Block.box(4, 4, 4, 12, 11, 12),
			Block.box(3, 11, 3, 13, 13, 13),
			Block.box(4, 13, 2.5, 12, 13.5, 13.5),
			Block.box(4, 8, 13, 12, 13, 13.5),
			Block.box(4, 8, 2.5, 12, 13, 3)
	).reduce((v1, v2) -> {return Shapes.join(v1, v2, OR);}).get();
	public LockPedestal(Properties properties) {
		super(properties);
	}
	@Override
	public InteractionBox getLockItemBox() {
		return lockBox;
	}
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LockPedestalBlockEntity(pos, state);
	}
	@Override
	public Collection<InteractionBox> getBoxes() {
		List<InteractionBox> l = new ArrayList<>();
		l.add(itemBox);
		l.add(lockBox);
		return l;
	}

	@Override
	public boolean renderBaseHighlight() {
		return true;
	}

	@Override
	public BlockEntityType<? extends BlockEntity> getBlockEntityType() {
		return BlockEntityInit.LOCK_PEDESTAL.get();
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {

		if (state.getValue(FACING).equals(Direction.WEST)){
			return SHAPE_1;
		} if (state.getValue(FACING).equals(Direction.EAST)){
			return SHAPE_1;
		} if (state.getValue(FACING).equals(Direction.SOUTH)){
			return SHAPE;
		}
		return SHAPE;
	}
}
