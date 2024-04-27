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

import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.block.AstralBlocks;
import dev.jaxydog.astral.content.item.AstralBlockItem;
import dev.jaxydog.astral.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.StringUtils;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;

/**
 * Defines dyed budding amethyst blocks.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
@SuppressWarnings("deprecation")
public class DyedBuddingAmethystBlock extends DyedAmethystBlock {

    /**
     * A block tag containing all budding amethysts.
     *
     * @since 2.0.0
     */
    public static final TagKey<Block> BUDDING_AMETHYSTS = TagKey.of(Registries.BLOCK.getKey(),
        Astral.getId("budding_amethysts")
    );
    /**
     * An item tag containing all budding amethysts.
     *
     * @since 2.0.0
     */
    public static final TagKey<Item> BUDDING_AMETHYST_ITEMS = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("budding_amethysts")
    );

    /**
     * Creates a new block using the given settings.
     *
     * @param path The block's identifier path.
     * @param settings The block's settings.
     * @param color The block's color.
     *
     * @since 2.0.0
     */
    public DyedBuddingAmethystBlock(String path, Settings settings, DyeColor color) {
        super(path, settings, color);
    }

    /**
     * Returns the small amethyst bud generated from this block.
     *
     * @return The amethyst bud.
     *
     * @since 2.0.0
     */
    protected Block getSmallBudBlock() {
        return AstralBlocks.DYED_SMALL_AMETHYST_BUDS.get(this.getColor()).orElseThrow();
    }

    /**
     * Returns the medium amethyst bud generated from this block.
     *
     * @return The amethyst bud.
     *
     * @since 2.0.0
     */
    protected Block getMediumBudBlock() {
        return AstralBlocks.DYED_MEDIUM_AMETHYST_BUDS.get(this.getColor()).orElseThrow();
    }

    /**
     * Returns the large amethyst bud generated from this block.
     *
     * @return The amethyst bud.
     *
     * @since 2.0.0
     */
    protected Block getLargeBudBlock() {
        return AstralBlocks.DYED_LARGE_AMETHYST_BUDS.get(this.getColor()).orElseThrow();
    }

    /**
     * Returns the amethyst cluster generated from this block.
     *
     * @return The amethyst cluster.
     *
     * @since 2.0.0
     */
    protected Block getClusterBlock() {
        return AstralBlocks.DYED_AMETHYST_CLUSTERS.get(this.getColor()).orElseThrow();
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(BuddingAmethystBlock.GROW_CHANCE) != 0) return;

        final Direction direction = DIRECTIONS[0];
        final BlockPos blockPos = pos.offset(direction);
        final BlockState blockState = world.getBlockState(blockPos);
        final Block block;

        if (BuddingAmethystBlock.canGrowIn(blockState)) {
            block = this.getSmallBudBlock();
        } else if (blockState.isOf(this.getSmallBudBlock())) {
            if (blockState.get(AmethystClusterBlock.FACING) != direction) return;

            block = this.getMediumBudBlock();
        } else if (blockState.isOf(this.getMediumBudBlock())) {
            if (blockState.get(AmethystClusterBlock.FACING) != direction) return;

            block = this.getLargeBudBlock();
        } else if (blockState.isOf(this.getLargeBudBlock())) {
            if (blockState.get(AmethystClusterBlock.FACING) != direction) return;

            block = this.getClusterBlock();
        } else {
            return;
        }

        final BlockState newState = block.getDefaultState()
            .with(AmethystClusterBlock.FACING, direction)
            .with(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);

        world.setBlockState(blockPos, newState);
    }

    @Override
    public void generate() {
        ModelGenerator.getInstance().generateBlock(g -> g.registerSimpleCubeAll(this));

        TagGenerator.getInstance().generate(BUDDING_AMETHYSTS, g -> g.add(this));
        TagGenerator.getInstance().generate(BUDDING_AMETHYST_ITEMS, g -> g.add(this.asItem()));
        TagGenerator.getInstance().generate(BlockTags.PICKAXE_MINEABLE, g -> g.add(this));

        TextureGenerator.getInstance().generate(Registries.BLOCK.getKey(), instance -> {
            final Optional<BufferedImage> maybeSource = instance.getImage("budding_amethyst");

            if (maybeSource.isPresent()) {
                final BufferedImage image = DYE_MAPPER.convert(maybeSource.get(), this.getColor());

                instance.generate(this.getRegistryId(), image);
            }
        });

        LootTableGenerator.getInstance().generate(LootContextTypes.BLOCK,
            this.getLootTableId(),
            new Builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1F))
                .with(ItemEntry.builder(this))
                .conditionally(SurvivesExplosionLootCondition.builder().build())
                .build())
        );

        RecipeGenerator.getInstance().generate(((AstralBlockItem) this.asItem()).getRegistryId(),
            ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this)
                .group("dyed_budding_amethysts")
                .input(BUDDING_AMETHYST_ITEMS)
                .input(DyeItem.byColor(this.getColor()))
                .criterion("block", FabricRecipeProvider.conditionsFromTag(BUDDING_AMETHYST_ITEMS))
        );

        LanguageGenerator.getInstance().generate(builder -> {
            final String[] parts = this.getColor().getName().split("_");
            final String value = Arrays.stream(parts)
                .map(s -> StringUtils.capitalize(s) + " ")
                .reduce(String::concat)
                .orElse("Dyed ");

            builder.add(this, value + "Budding Amethyst");
        });
    }

}
