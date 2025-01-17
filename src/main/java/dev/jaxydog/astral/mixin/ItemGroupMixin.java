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

import dev.jaxydog.astral.utility.injected.AstralItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Implements the {@link AstralItemGroup} interface.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin implements AstralItemGroup {

    /**
     * Returns the item group's icon.
     *
     * @return The item group's icon.
     *
     * @since 1.7.0
     */
    @Shadow
    public abstract ItemStack getIcon();

    @Override
    public ItemStack astral$getIcon(float delta) {
        return this.getIcon();
    }

}
