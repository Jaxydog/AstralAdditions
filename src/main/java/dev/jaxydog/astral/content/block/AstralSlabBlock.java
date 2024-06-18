/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2024 IcePenguin
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

package dev.jaxydog.astral.content.block;

import dev.jaxydog.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;

import java.util.Optional;

/**
 * An extension of an {@link SlabBlock} that provides commonly used functionality.
 * <p>
 * This is one of the various instances of already-provided wrapper classes for commonly used types.
 * <p>
 * In future code, you should prefer to extend this class over {@link SlabBlock} if at all possible.
 * <p>
 * This type is automatically registered.
 *
 * @author IcePenguin
 * @see Custom
 * @since 2.2.0
 */
public class AstralSlabBlock extends SlabBlock implements Custom {

    /**
     * The texture key for side 0.
     *
     * @since 2.2.0
     */
    protected static final TextureKey SIDE_0 = TextureKey.of("side_0");

    /**
     * The texture key for side 1.
     *
     * @since 2.2.0
     */
    protected static final TextureKey SIDE_1 = TextureKey.of("side_1");

    /**
     * The base directional slab model.
     *
     * @since 2.2.0
     */
    protected static final Model VERTICAL_MODEL = new Model(
        Optional.ofNullable(Astral.getId("block/directional_vertical_slab")),
        Optional.empty(),
        TextureKey.TOP,
        TextureKey.BOTTOM,
        SIDE_0,
        SIDE_1
    );

    /**
     * The base directional slab model.
     *
     * @since 2.2.0
     */
    protected static final Model VERTICAL_MODEL_TOP = new Model(
        Optional.ofNullable(Astral.getId("block/directional_vertical_slab_top")),
        Optional.empty(),
        TextureKey.TOP,
        TextureKey.BOTTOM,
        SIDE_0,
        SIDE_1
    );

    /**
     * The base directional slab model.
     *
     * @since 2.2.0
     */
    protected static final Model HORIZONTAL_MODEL = new Model(
        Optional.ofNullable(Astral.getId("block/directional_horizontal_slab")),
        Optional.empty(),
        TextureKey.TOP,
        TextureKey.BOTTOM,
        SIDE_0,
        SIDE_1
    );

    /**
     * The base directional slab model.
     *
     * @since 2.2.0
     */
    protected static final Model HORIZONTAL_MODEL_TOP = new Model(
        Optional.ofNullable(Astral.getId("block/directional_horizontal_slab_top")),
        Optional.empty(),
        TextureKey.TOP,
        TextureKey.BOTTOM,
        SIDE_0,
        SIDE_1
    );

    /**
     * The slab block's identifier path used within the registration system.
     *
     * @since 2.2.0
     */
    private final String path;

    /**
     * Creates a new slab block using given settings.
     *
     * @param path The slab block's identifier path.
     * @param settings the slab block's settings.
     *
     * @since 2.2.0
     */
    public AstralSlabBlock(String path, Settings settings) {
        super(settings);

        this.path = path;
    }

    @Override
    public Block asBlock() {
        return this;
    }

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return state.contains(TYPE) && super.hasSidedTransparency(state);
    }

}
