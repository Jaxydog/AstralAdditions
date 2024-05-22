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

import dev.jaxydog.astral.content.block.AstralBlock;
import dev.jaxydog.astral.datagen.TagGenerator;
import dev.jaxydog.astral.register.Registered.Generated;
import net.minecraft.registry.tag.BlockTags;

/**
 * The cobbled sandstone block.
 *
 * @author Jaxydog
 * @since 2.0.1
 */
public class CobbledSandstoneBlock extends AstralBlock implements Generated {

    /**
     * Creates a new block using the given settings.
     *
     * @param path The block's identifier path.
     * @param settings The block's settings.
     *
     * @since 2.0.0
     */
    public CobbledSandstoneBlock(String path, Settings settings) {
        super(path, settings);
    }

    @Override
    public void generate() {
        TagGenerator.getInstance().generate(BlockTags.PICKAXE_MINEABLE, b -> b.add(this));
    }

}
