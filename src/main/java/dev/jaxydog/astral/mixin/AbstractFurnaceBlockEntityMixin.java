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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.jaxydog.astral.content.item.AstralItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

/**
 * Allows log slabs to be used as a fuel source.
 *
 * @author Jaxydog
 * @since 2.2.1
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider {

    /**
     * Creates a new instance of this mixin.
     *
     * @param blockEntityType The block entity type.
     * @param blockPos The block position.
     * @param blockState The block state.
     *
     * @since 2.2.1
     */
    protected AbstractFurnaceBlockEntityMixin(
        BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState
    ) {
        super(blockEntityType, blockPos, blockState);
    }

    @Shadow
    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) { }

    /**
     * Adds log slabs to the furnace's fuel map.
     *
     * @param original The original map.
     *
     * @return The modified map.
     *
     * @since 2.2.1
     */
    @ModifyReturnValue(method = "createFuelTimeMap", at = @At("RETURN"))
    private static Map<Item, Integer> modifyFuelTimeMap(Map<Item, Integer> original) {
        addFuel(original, AstralItems.ACACIA_LOG_SLAB, 150);
        addFuel(original, AstralItems.BAMBOO_BLOCK_SLAB, 150);
        addFuel(original, AstralItems.BIRCH_LOG_SLAB, 150);
        addFuel(original, AstralItems.CHERRY_LOG_SLAB, 150);
        addFuel(original, AstralItems.CRIMSON_STEM_SLAB, 150);
        addFuel(original, AstralItems.DARK_OAK_LOG_SLAB, 150);
        addFuel(original, AstralItems.JUNGLE_LOG_SLAB, 150);
        addFuel(original, AstralItems.MANGROVE_LOG_SLAB, 150);
        addFuel(original, AstralItems.OAK_LOG_SLAB, 150);
        addFuel(original, AstralItems.SPRUCE_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_ACACIA_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_BAMBOO_BLOCK_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_BIRCH_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_CHERRY_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_CRIMSON_STEM_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_DARK_OAK_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_JUNGLE_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_MANGROVE_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_OAK_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_SPRUCE_LOG_SLAB, 150);
        addFuel(original, AstralItems.STRIPPED_WARPED_STEM_SLAB, 150);
        addFuel(original, AstralItems.WARPED_STEM_SLAB, 150);

        return original;
    }

}
