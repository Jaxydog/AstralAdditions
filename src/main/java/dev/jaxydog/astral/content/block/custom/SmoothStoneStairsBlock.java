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

package dev.jaxydog.astral.content.block.custom;

import dev.jaxydog.astral.content.block.AstralStairsBlock;
import dev.jaxydog.astral.datagen.LootTableGenerator;
import dev.jaxydog.astral.datagen.RecipeGenerator;
import dev.jaxydog.astral.datagen.TagGenerator;
import dev.jaxydog.astral.register.Registered.Generated;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;

/**
 * The smooth stone stairs block.
 *
 * @author Jaxydog
 * @since 2.2.0
 */
public class SmoothStoneStairsBlock extends AstralStairsBlock implements Generated {

    /**
     * Creates a new stair block using the given settings.
     *
     * @param path The block's identifier path.
     * @param baseBlockState The base block state.
     * @param settings The block's settings.
     *
     * @since 2.2.0
     */
    public SmoothStoneStairsBlock(String path, BlockState baseBlockState, Settings settings) {
        super(path, baseBlockState, settings);
    }

    @Override
    public void generate() {
        TagGenerator.getInstance().generate(BlockTags.PICKAXE_MINEABLE, b -> b.add(this));

        LootTableGenerator.getInstance().generate(
            LootContextTypes.BLOCK,
            this.getLootTableId(),
            new Builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1F))
                .with(ItemEntry.builder(this))
                .conditionally(SurvivesExplosionLootCondition.builder()))
        );

        RecipeGenerator.getInstance().generate(
            this.getRegistryId(),
            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this, 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .input('X', Blocks.SMOOTH_STONE)
                .criterion("block", FabricRecipeProvider.conditionsFromItem(Blocks.SMOOTH_STONE))
        );
        RecipeGenerator.getInstance().generate(
            this.getRegistryId().withSuffixedPath("_reversed"),
            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this, 4)
                .pattern("  X")
                .pattern(" XX")
                .pattern("XXX")
                .input('X', Blocks.SMOOTH_STONE)
                .criterion("block", FabricRecipeProvider.conditionsFromItem(Blocks.SMOOTH_STONE))
        );
    }

}
