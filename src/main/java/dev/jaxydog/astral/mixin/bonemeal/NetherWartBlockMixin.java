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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Implements the {@link  Fertilizable} interface.
 *
 * @author Jaxydog
 * @since 1.0.0
 */
@Mixin(NetherWartBlock.class)
@Implements(@Interface(iface = Fertilizable.class, prefix = "impl$"))
public abstract class NetherWartBlockMixin {

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
        return state.get(NetherWartBlock.AGE) < 3;
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
        return state.get(NetherWartBlock.AGE) < 3;
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
        final int growth = random.nextInt(2) + 1;
        final int current = state.get(NetherWartBlock.AGE);

        state = state.with(NetherWartBlock.AGE, Math.min(current + growth, 3));
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
    }

}
