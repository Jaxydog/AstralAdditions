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
import dev.jaxydog.astral.utility.CurrencyHelper;
import dev.jaxydog.astral.utility.CurrencyHelper.Reward;
import dev.jaxydog.astral.utility.CurrencyHelper.Unit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin {

    @ModifyReturnValue(
        method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
        at = @At("RETURN")
    )
    private ItemStack preventExchange(ItemStack stack) {
        final Item item = stack.getItem();

        if (Unit.UNITS.find(item).isPresent() || Reward.REWARDS.find(item).isPresent()) {
            stack.getOrCreateNbt().putBoolean(CurrencyHelper.EXCHANGE_KEY, false);
        }

        return stack;
    }

}
