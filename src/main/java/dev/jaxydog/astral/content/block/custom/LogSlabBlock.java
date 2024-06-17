package dev.jaxydog.astral.content.block.custom;

import dev.jaxydog.astral.content.block.AstralSlabBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * The log slab block.
 *
 * @author IcePenguin
 * @since 2.2.0
 */
@SuppressWarnings("deprecation")
public class LogSlabBlock extends AstralSlabBlock {

    /**
     * The block's axis property.
     *
     * @since 2.2.0
     */
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;

    /**
     * Creates a new log slab block using given settings.
     *
     * @param path The slab block's identifier path.
     * @param settings the slab block's settings.
     * @since 2.2.0
     */
    public LogSlabBlock(String path, Settings settings) {
        super(path, settings);

        this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = super.getPlacementState(ctx);

        return state == null ? null : state.with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.get(AXIS)) {
                case X -> state.with(AXIS, Direction.Axis.Z);
                case Z -> state.with(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }

}
