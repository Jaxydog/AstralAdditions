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

package dev.jaxydog.astral.mixin;

import dev.jaxydog.astral.utility.injected.AstralItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Implements the {@link AstralItemStack} interface.
 *
 * @author Jaxydog
 * @since 1.4.0
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AstralItemStack {

    /**
     * The represented item.
     *
     * @since 1.4.0
     */
    @Shadow
    @Final
    @Mutable
    private Item item;

    @Override
    public void astral$setItem(Item item) {
        this.item = item;
    }

    @SuppressWarnings({ "RedundantCast", "UnreachableCode", "DataFlowIssue" })
    @Override
    public ItemStack astral$copyWithItemStack(Item item) {
        if (((ItemStack) (Object) this).isEmpty()) {
            return ItemStack.EMPTY;
        }

        final ItemStack copy = ((ItemStack) (Object) this).copy();

        ((AstralItemStack) copy).astral$setItem(item);

        return copy;
    }

}
