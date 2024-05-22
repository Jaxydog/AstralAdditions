/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2023–2024 Jaxydog
 * Copyright © 2024 FunsulYT
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
     * The cobbled sandstone block.
     *
     * @since 2.0.0
     */
    public static final CobbledSandstoneBlock COBBLED_SANDSTONE = new CobbledSandstoneBlock("cobbled_sandstone",
        Settings.copy(Blocks.SANDSTONE)
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
     * The randomizer block.
     *
     * @since 2.0.0
     */
    public static final RandomizerBlock RANDOMIZER = new RandomizerBlock("randomizer",
        Settings.copy(Blocks.IRON_BLOCK)
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
