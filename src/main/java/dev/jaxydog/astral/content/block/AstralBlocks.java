/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2023–2024 Jaxydog
 * Copyright © 2024 FunsulYT
 * Copyright © 2024 IcePenguin
 *
 * This file is part of Astral.
 *
 * Astral is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Astral is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with Astral. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jaxydog.astral.content.block;

import dev.jaxydog.astral.content.block.custom.*;
import dev.jaxydog.astral.content.block.custom.DyedAmethystClusterBlock.Type;
import dev.jaxydog.astral.datagen.TagGenerator;
import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.register.DyeMap;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Blocks;

/**
 * Contains definitions of all modded-in blocks.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public final class AstralBlocks extends ContentRegistrar {

    /**
     * The acacia log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock ACACIA_LOG_SLAB = new LogSlabBlock("acacia_log_slab",
        Blocks.ACACIA_LOG,
        Settings.copy(Blocks.ACACIA_LOG)
    );

    /**
     * The bamboo block slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock BAMBOO_BLOCK_SLAB = new LogSlabBlock("bamboo_block_slab",
        Blocks.BAMBOO_BLOCK,
        Settings.copy(Blocks.BAMBOO_BLOCK)
    );

    /**
     * The birch log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock BIRCH_LOG_SLAB = new LogSlabBlock("birch_log_slab",
        Blocks.BIRCH_LOG,
        Settings.copy(Blocks.BIRCH_LOG)
    );

    /**
     * The cherry log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock CHERRY_LOG_SLAB = new LogSlabBlock("cherry_log_slab",
        Blocks.CHERRY_LOG,
        Settings.copy(Blocks.CHERRY_LOG)
    );

    /**
     * The crimson stem slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock CRIMSON_STEM_SLAB = new LogSlabBlock("crimson_stem_slab",
        Blocks.CRIMSON_STEM,
        Settings.copy(Blocks.CRIMSON_STEM)
    );

    /**
     * The cobbled sandstone block.
     *
     * @since 2.0.0
     */
    public static final CobbledSandstoneBlock COBBLED_SANDSTONE = new CobbledSandstoneBlock("cobbled_sandstone",
        Settings.copy(Blocks.SANDSTONE)
    );

    /**
     * The dark oak log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock DARK_OAK_LOG_SLAB = new LogSlabBlock("dark_oak_log_slab",
        Blocks.DARK_OAK_LOG,
        Settings.copy(Blocks.DARK_OAK_LOG)
    );

    /**
     * The set of dyed amethyst blocks.
     *
     * @since 2.0.0
     */
    public static final DyeMap<DyedAmethystBlock> DYED_AMETHYST_BLOCKS = new DyeMap<>("amethyst_block",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.AMETHYST_BLOCK).mapColor(color);

            return new DyedAmethystBlock(rawId, settings, color);
        }
    );

    /**
     * The set of dyed amethyst cluster blocks.
     *
     * @since 2.0.0
     */
    public static final DyeMap<DyedAmethystClusterBlock> DYED_AMETHYST_CLUSTERS = new DyeMap<>("amethyst_cluster",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.AMETHYST_CLUSTER).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.CLUSTER);
        }
    );

    /**
     * The set of dyed budding amethyst blocks.
     *
     * @since 2.0.0
     */
    public static final DyeMap<DyedBuddingAmethystBlock> DYED_BUDDING_AMETHYST_BLOCKS = new DyeMap<>("budding_amethyst",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.BUDDING_AMETHYST).mapColor(color);

            return new DyedBuddingAmethystBlock(rawId, settings, color);
        }
    );

    /**
     * The set of dyed large amethyst bud blocks.
     *
     * @since 2.0.0
     */
    public static final DyeMap<DyedAmethystClusterBlock> DYED_LARGE_AMETHYST_BUDS = new DyeMap<>("large_amethyst_bud",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.LARGE_AMETHYST_BUD).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.LARGE_BUD);
        }
    );

    /**
     * The set of dyed medium amethyst bud blocks.
     *
     * @since 2.0.0
     */
    public static final DyeMap<DyedAmethystClusterBlock> DYED_MEDIUM_AMETHYST_BUDS = new DyeMap<>("medium_amethyst_bud",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.MEDIUM_AMETHYST_BUD).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.MEDIUM_BUD);
        }
    );

    /**
     * The set of dyed small amethyst bud blocks.
     *
     * @since 2.0.0
     */
    public static final DyeMap<DyedAmethystClusterBlock> DYED_SMALL_AMETHYST_BUDS = new DyeMap<>("small_amethyst_bud",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.SMALL_AMETHYST_BUD).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.SMALL_BUD);
        }
    );

    /**
     * The jungle log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock JUNGLE_LOG_SLAB = new LogSlabBlock("jungle_log_slab",
        Blocks.JUNGLE_LOG,
        Settings.copy(Blocks.JUNGLE_LOG)
    );

    /**
     * The mangrove log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock MANGROVE_LOG_SLAB = new LogSlabBlock("mangrove_log_slab",
        Blocks.MANGROVE_LOG,
        Settings.copy(Blocks.MANGROVE_LOG)
    );

    /**
     * The oak  log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock OAK_LOG_SLAB = new LogSlabBlock("oak_log_slab",
        Blocks.OAK_LOG,
        Settings.copy(Blocks.OAK_LOG)
    );

    /**
     * The randomizer block.
     *
     * @since 2.0.0
     */
    public static final RandomizerBlock RANDOMIZER = new RandomizerBlock("randomizer",
        Settings.copy(Blocks.IRON_BLOCK)
    );

    /**
     * The smooth stone stairs block.
     *
     * @since 2.2.0
     */
    public static final SmoothStoneStairsBlock SMOOTH_STONE_STAIRS = new SmoothStoneStairsBlock("smooth_stone_stairs",
        Blocks.SMOOTH_STONE.getDefaultState(),
        Settings.copy(Blocks.SMOOTH_STONE)
    );

    /**
     * The spruce log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock SPRUCE_LOG_SLAB = new LogSlabBlock("spruce_log_slab",
        Blocks.SPRUCE_LOG,
        Settings.copy(Blocks.SPRUCE_LOG)
    );

    /**
     * The stripped acacia log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_ACACIA_LOG_SLAB = new LogSlabBlock("stripped_acacia_log_slab",
        Blocks.STRIPPED_ACACIA_LOG,
        Settings.copy(Blocks.STRIPPED_ACACIA_LOG)
    );

    /**
     * The stripped bamboo block slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_BAMBOO_BLOCK_SLAB = new LogSlabBlock("stripped_bamboo_block_slab",
        Blocks.STRIPPED_BAMBOO_BLOCK,
        Settings.copy(Blocks.STRIPPED_BAMBOO_BLOCK)
    );

    /**
     * The stripped birch log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_BIRCH_LOG_SLAB = new LogSlabBlock("stripped_birch_log_slab",
        Blocks.STRIPPED_BIRCH_LOG,
        Settings.copy(Blocks.STRIPPED_BIRCH_LOG)
    );

    /**
     * The stripped cherry log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_CHERRY_LOG_SLAB = new LogSlabBlock("stripped_cherry_log_slab",
        Blocks.STRIPPED_CHERRY_LOG,
        Settings.copy(Blocks.STRIPPED_CHERRY_LOG)
    );

    /**
     * The stripped crimson stem slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_CRIMSON_STEM_SLAB = new LogSlabBlock("stripped_crimson_stem_slab",
        Blocks.STRIPPED_CRIMSON_STEM,
        Settings.copy(Blocks.STRIPPED_CRIMSON_STEM)
    );

    /**
     * The stripped dark oak log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_DARK_OAK_LOG_SLAB = new LogSlabBlock("stripped_dark_oak_log_slab",
        Blocks.STRIPPED_DARK_OAK_LOG,
        Settings.copy(Blocks.STRIPPED_DARK_OAK_LOG)
    );

    /**
     * The stripped jungle log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_JUNGLE_LOG_SLAB = new LogSlabBlock("stripped_jungle_log_slab",
        Blocks.STRIPPED_JUNGLE_LOG,
        Settings.copy(Blocks.STRIPPED_JUNGLE_LOG)
    );

    /**
     * The stripped mangrove log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_MANGROVE_LOG_SLAB = new LogSlabBlock("stripped_mangrove_log_slab",
        Blocks.STRIPPED_MANGROVE_LOG,
        Settings.copy(Blocks.STRIPPED_MANGROVE_LOG)
    );

    /**
     * The stripped oak log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_OAK_LOG_SLAB = new LogSlabBlock("stripped_oak_log_slab",
        Blocks.STRIPPED_OAK_LOG,
        Settings.copy(Blocks.STRIPPED_OAK_LOG)
    );

    /**
     * The stripped spruce log slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_SPRUCE_LOG_SLAB = new LogSlabBlock("stripped_spruce_log_slab",
        Blocks.STRIPPED_SPRUCE_LOG,
        Settings.copy(Blocks.STRIPPED_SPRUCE_LOG)
    );

    /**
     * The stripped warped stem slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock STRIPPED_WARPED_STEM_SLAB = new LogSlabBlock("stripped_warped_stem_slab",
        Blocks.STRIPPED_WARPED_STEM,
        Settings.copy(Blocks.STRIPPED_WARPED_STEM)
    );

    /**
     * The warped stem slab block.
     *
     * @since 2.2.0
     */
    public static final LogSlabBlock WARPED_STEM_SLAB = new LogSlabBlock("warped_stem_slab",
        Blocks.WARPED_STEM,
        Settings.copy(Blocks.WARPED_STEM)
    );

    @Override
    public void generate() {
        super.generate();

        final TagGenerator tag = TagGenerator.getInstance();

        tag.generate(DyedAmethystBlock.AMETHYST_BLOCKS, b -> b.add(Blocks.AMETHYST_BLOCK));
        tag.generate(DyedBuddingAmethystBlock.BUDDING_AMETHYSTS, b -> b.add(Blocks.BUDDING_AMETHYST));
        tag.generate(DyedAmethystClusterBlock.AMETHYST_CLUSTERS, b -> b.add(Blocks.AMETHYST_CLUSTER));
        tag.generate(DyedAmethystClusterBlock.LARGE_AMETHYST_BUDS, b -> b.add(Blocks.LARGE_AMETHYST_BUD));
        tag.generate(DyedAmethystClusterBlock.MEDIUM_AMETHYST_BUDS, b -> b.add(Blocks.MEDIUM_AMETHYST_BUD));
        tag.generate(DyedAmethystClusterBlock.SMALL_AMETHYST_BUDS, b -> b.add(Blocks.SMALL_AMETHYST_BUD));
    }

}
