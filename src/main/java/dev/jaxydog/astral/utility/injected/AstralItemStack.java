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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Provides extensions to the {@link ItemStack} class.
 *
 * @since 1.4.0
 */
public interface AstralItemStack {

    /**
     * Sets the stack's item.
     *
     * @param item The new item.
     *
     * @since 1.4.0
     */
    void astral$setItem(Item item);

    /**
     * Copies this stack, and swaps out the inner item.
     *
     * @param item The new item.
     *
     * @return The copied stack.
     *
     * @since 1.4.0
     */
    ItemStack astral$copyWithItemStack(Item item);

}
