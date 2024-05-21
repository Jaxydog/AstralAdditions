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

package dev.jaxydog.astral.utility.injected;

/**
 * Extends the {@link net.minecraft.entity.LightningEntity} class.
 *
 * @author Jaxydog
 * @since 1.4.0
 */
public interface AstralLightningEntity {

    /**
     * Returns whether this entity should preserve ground items.
     *
     * @return Whether this entity should preserve ground items.
     *
     * @since 1.4.0
     */
    boolean astral$preservesItems();

    /**
     * Sets whether this entity should preserve ground items.
     *
     * @param preserve Whether to preserve ground items.
     *
     * @since 1.4.0
     */
    void astral$setPreservesItems(boolean preserve);

}
