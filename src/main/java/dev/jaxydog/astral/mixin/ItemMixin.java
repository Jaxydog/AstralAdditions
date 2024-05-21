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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/** Provide an NBT tag to disable enchantment glint */
@Mixin(Item.class)
public abstract class ItemMixin {

    /** The NBT key that corresponds to the modded-in enchantment glint disable tag */
    @Unique
    private static final String SET_GLINT_KEY = "SetGlint";

    @ModifyReturnValue(method = "hasGlint", at = @At("RETURN"))
    private boolean forceGlint(boolean result, @Local(argsOnly = true) ItemStack stack) {
        final NbtCompound compound = stack.getNbt();

        if (compound != null && compound.contains(SET_GLINT_KEY)) {
            return compound.getBoolean(SET_GLINT_KEY);
        } else {
            return result;
        }
    }

}
