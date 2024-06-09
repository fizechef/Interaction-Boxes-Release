package com.becks.interaction_boxes.common.exampleBlocks.candleAltar;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.CandleBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxEntityBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.candleBoxBlock.CandleBoxBlock;
import com.becks.interaction_boxes.init.BlockEntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.CandleBlock.LIT;
import static net.minecraft.world.phys.shapes.BooleanOp.OR;

/**
 * example for CandleBoxBlock implementation using the interface method,
 * note that this block needs a block entity to properly render the fake candles not baked into the model
 */
public class CandleAltar extends InteractionBoxEntityBlock implements CandleBoxBlock {
    public static final BooleanProperty[] LIT_LIST = new BooleanProperty[]{
            BooleanProperty.create("lit0"),
            BooleanProperty.create("lit1"),
            BooleanProperty.create("lit2"),
            BooleanProperty.create("lit3"),
            BooleanProperty.create("lit4"),
            BooleanProperty.create("lit5"),
            BooleanProperty.create("lit6"),
            BooleanProperty.create("lit7")
    };
    private final VoxelShape SHAPE  = Stream.of(
            Block.box(2, 0, 2, 14, 2, 14),
            Block.box(2, 11, 2, 14, 13, 14),
            Block.box(2, 0, 2, 4, 11, 4),
            Block.box(12, 0, 12, 14, 11, 14),
            Block.box(2, 0, 12, 4, 11, 14),
            Block.box(12, 0, 2, 14, 11, 4),
            Block.box(2.7, 0, 2.7, 13.3, 11, 13.3)
    ).reduce((v1, v2) -> {return Shapes.join(v1, v2, OR);}).get();
    public CandleAltar(Properties p_49224_) {
        super(p_49224_);
        for (BooleanProperty p : LIT_LIST){
            this.defaultBlockState().setValue(p, Boolean.FALSE);
        }
    }
    @Override
    public BlockState lightCandle(BlockState state, int candleIndex){
        return state.setValue(LIT_LIST[candleIndex], true);
    }
    @Override
    public BlockState extinguishCandle(BlockState state, int candleIndex) {
        return state.setValue(LIT_LIST[candleIndex], false);
    }
    @Override
    public boolean isCandleLit(BlockState state, int candleIndex) {
        return state.getValue(LIT_LIST[candleIndex]);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        for (BooleanProperty p : LIT_LIST){
            pBuilder.add(p);
        }
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState superState = super.getStateForPlacement(context);
        for (BooleanProperty p : LIT_LIST){
            assert superState != null;
            superState = superState.setValue(p, Boolean.FALSE);
        }
        return superState;
    }

    /**
     * Defining specific candle box parameters here to more easily pass them in the getBoxes() method, these could be class attributes
     * @return a list of candle boxes
     */
    private Collection<InteractionBox> getCandleBoxes() {
        //These need to be in candleindex clockwise order, starting with the north most candlebox with standard block rotation north
        List<InteractionBox> l = new ArrayList<InteractionBox>();
        l.add(new CandleBox(Shapes.or(Block.box(1.9, 5, 5.5, 2.9, 9, 6.5)), 7,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        l.add(new CandleBox(Shapes.or(Block.box(1.9, 5, 9.5, 2.9, 9, 10.5)), 6,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        l.add(new CandleBox(Shapes.or(Block.box(13.1, 5, 5.5, 14.1, 9, 6.5)), 2,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        l.add(new CandleBox(Shapes.or(Block.box(13.1, 5, 9.5, 14.1, 9, 10.5)), 3,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        l.add(new CandleBox(Shapes.or(Block.box(5.5, 5, 1.9, 6.5, 9, 2.9)), 0,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        l.add(new CandleBox(Shapes.or(Block.box(9.5, 5, 1.9, 10.5, 9, 2.9)), 1,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        l.add(new CandleBox(Shapes.or(Block.box(5.5, 5, 13.1, 6.5, 9, 14.1)), 5,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        l.add(new CandleBox(Shapes.or(Block.box(9.5, 5, 13.1, 10.5, 9, 14.1)), 4,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.LIGHT_BLUE_CANDLE.defaultBlockState().setValue(LIT, true)) : null,
                (level) -> level.isClientSide ? Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WHITE_CANDLE.defaultBlockState()) : null,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.SMOKE
        ));
        return l;
    }
    @Override
    public Collection<InteractionBox> getBoxes() {
        return new ArrayList<>(this.getCandleBoxes());
    }

    @Override
    public boolean renderBaseHighlight() {
        return true;
    }

    @Override
    public BlockEntityType<? extends BlockEntity> getBlockEntityType() {
        return BlockEntityInit.CITY_CENTER.get();
    }
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CandleAltarBlockEntity(pos, state);
    }
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }
}
