/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2023–2024 Jaxydog
 *
 * This file is part of Astral.
 *
 * Astral is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Astral is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with Astral. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jaxydog.astral.mixin.bonemeal;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Implements the {@link  Fertilizable} interface.
 *
 * @author Jaxydog
 * @since 1.0.0
 */
@Mixin(SugarCaneBlock.class)
@Implements(@Interface(iface = Fertilizable.class, prefix = "impl$"))
public abstract class SugarCaneBlockMixin {

    /**
     * Configures the maximum fertilizable height of sugar cane.
     *
     * @since 1.0.0
     */
    @Unique
    private static final int MAX_HEIGHT = 3;
    /**
     * Configures how likely sugar cane is to grow when fertilizing it.
     *
     * @since 1.0.0
     */
    @Unique
    private static final float GROW_CHANCE = 2F / 3F;
    /**
     * Configures how likely sugar cane is to grow an extra time when fertilizing it.
     *
     * @since 1.0.0
     */
    @Unique
    private static final float BONUS_CHANCE = 1F / 3F;

    /**
     * Returns whether the given block is fertilizable.
     *
     * @param world The current world.
     * @param pos The block position.
     * @param state The block state.
     * @param isClient Whether this is being run on the client.
     *
     * @return Whether the given block is fertilizable.
     *
     * @since 1.0.0
     */
    public boolean impl$isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        final BlockPos top = this.getTop(world, pos);

        if (!world.getBlockState(top).isAir()) return false;
        if (!this.self().canPlaceAt(world.getBlockState(top), world, top)) return false;

        return this.getHeight(world, top, true) < MAX_HEIGHT;
    }

    /**
     * Returns the top-most block of the sugar cane.
     *
     * @param world The current world.
     * @param current The current block position.
     *
     * @return The top-most block of the sugar cane.
     *
     * @since 1.0.0
     */
    @Unique
    private BlockPos getTop(WorldView world, BlockPos current) {
        while (true) {
            final BlockPos above = current.up();

            if (world.getBlockState(above).getBlock() instanceof SugarCaneBlock) {
                current = above;
            } else {
                return current;
            }
        }
    }

    /**
     * Returns the mixin's {@code this} instance.
     *
     * @return The mixin's {@code this} instance.
     *
     * @since 1.0.0
     */
    @Unique
    private SugarCaneBlock self() {
        return (SugarCaneBlock) (Object) this;
    }

    /**
     * Returns the height of the sugar cane, counting downwards from the given position.
     *
     * @param world The current world.
     * @param pos The current position.
     * @param fromTop Whether to run this check from the computed top.
     *
     * @since 1.0.0
     */
    @Unique
    private int getHeight(WorldView world, BlockPos pos, boolean fromTop) {
        int height = 1;

        if (fromTop) pos = this.getTop(world, pos);

        while (world.getBlockState(pos.down(height)).getBlock() instanceof SugarCaneBlock) {
            height += 1;
        }

        return height;
    }

    /**
     * Returns whether this block can be grown.
     *
     * @param world The current world.
     * @param random The randomness instance.
     * @param pos The block position.
     * @param state The block state.
     *
     * @return Whether this block can be grown.
     *
     * @since 1.0.0
     */
    public boolean impl$canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Grows this block.
     *
     * @param world The current world.
     * @param random The randomness instance.
     * @param pos The block position.
     * @param state The block state.
     *
     * @since 1.0.0
     */
    public void impl$grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos top = this.getTop(world, pos).up();

        if (random.nextFloat() > GROW_CHANCE) return;

        world.setBlockState(top, Blocks.SUGAR_CANE.getDefaultState());

        if (this.getHeight(world, top, false) >= MAX_HEIGHT) return;

        top = top.up();

        if (!this.self().canPlaceAt(world.getBlockState(top), world, top)) return;
        if (random.nextFloat() > BONUS_CHANCE) return;

        world.setBlockState(top, Blocks.SUGAR_CANE.getDefaultState());
    }

}
