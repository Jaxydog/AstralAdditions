/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2024 Jaxydog
 *
 * This file is part of Astral.
 *
 * Astral is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Astral is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with Astral. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jaxydog.astral.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.astral.content.block.AstralBlocks;
import dev.jaxydog.astral.content.block.AstralSlabBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

/**
 * Allows log slabs to be stripped.
 *
 * @author Jaxydog
 * @since 2.2.0
 */
@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItem {

    /**
     * Creates a new instance of this mixin.
     *
     * @param attackDamage The item's attack damage.
     * @param attackSpeed The item's attack speed.
     * @param material The item's tool material.
     * @param effectiveBlocks A tag of effective blocks.
     * @param settings The item's settings.
     *
     * @since 2.2.0
     */
    public AxeItemMixin(
        float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings
    ) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    /**
     * Modify the immutable map during initialization.
     *
     * @param instance The builder instance.
     * @param original The build method.
     *
     * @return The finished map.
     *
     * @since 2.2.0
     */
    @WrapOperation(
        method = "<clinit>", at = @At(
        value = "INVOKE",
        target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"
    )
    )
    private static ImmutableMap<Block, Block> modifyStrippedBlocks(
        Builder<Block, Block> instance, Operation<ImmutableMap<Block, Block>> original
    ) {
        return original.call(instance.put(AstralBlocks.ACACIA_LOG_SLAB, AstralBlocks.STRIPPED_ACACIA_LOG_SLAB)
            .put(AstralBlocks.BAMBOO_BLOCK_SLAB, AstralBlocks.STRIPPED_BAMBOO_BLOCK_SLAB)
            .put(AstralBlocks.BIRCH_LOG_SLAB, AstralBlocks.STRIPPED_BIRCH_LOG_SLAB)
            .put(AstralBlocks.CHERRY_LOG_SLAB, AstralBlocks.STRIPPED_CHERRY_LOG_SLAB)
            .put(AstralBlocks.CRIMSON_STEM_SLAB, AstralBlocks.STRIPPED_CRIMSON_STEM_SLAB)
            .put(AstralBlocks.DARK_OAK_LOG_SLAB, AstralBlocks.STRIPPED_DARK_OAK_LOG_SLAB)
            .put(AstralBlocks.JUNGLE_LOG_SLAB, AstralBlocks.STRIPPED_JUNGLE_LOG_SLAB)
            .put(AstralBlocks.MANGROVE_LOG_SLAB, AstralBlocks.STRIPPED_MANGROVE_LOG_SLAB)
            .put(AstralBlocks.OAK_LOG_SLAB, AstralBlocks.STRIPPED_OAK_LOG_SLAB)
            .put(AstralBlocks.SPRUCE_LOG_SLAB, AstralBlocks.STRIPPED_SPRUCE_LOG_SLAB)
            .put(AstralBlocks.WARPED_STEM_SLAB, AstralBlocks.STRIPPED_WARPED_STEM_SLAB));
    }

    /**
     * Retain additional block state for slabs.
     *
     * @param original The original state.
     *
     * @return The block state.
     *
     * @since 2.2.0
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ModifyReturnValue(method = "getStrippedState", at = @At("RETURN"))
    private Optional<BlockState> modifyStrippedState(
        Optional<BlockState> original, @Local(argsOnly = true) BlockState state
    ) {
        if (state.getBlock() instanceof AstralSlabBlock) {
            return original.map(s -> s.with(AstralSlabBlock.TYPE, state.get(AstralSlabBlock.TYPE))
                .with(AstralSlabBlock.WATERLOGGED, state.get(AstralSlabBlock.WATERLOGGED)));
        }

        return original;
    }

}
