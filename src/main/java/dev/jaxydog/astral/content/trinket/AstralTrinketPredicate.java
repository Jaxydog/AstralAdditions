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

package dev.jaxydog.astral.content.trinket;

import com.mojang.datafixers.util.Function3;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import dev.jaxydog.astral.register.Registered;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Wraps a trinket predicate for automatic registration.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralTrinketPredicate implements Registered.Common {

    /**
     * The predicate's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;
    /**
     * The inner predicate function.
     *
     * @since 2.0.0
     */
    private final Function3<ItemStack, SlotReference, LivingEntity, TriState> predicate;

    /**
     * Creates a new predicate wrapper.
     *
     * @param rawId The predicate's identifier path.
     * @param predicate The predicate.
     *
     * @since 2.0.0
     */
    public AstralTrinketPredicate(String rawId, Function3<ItemStack, SlotReference, LivingEntity, TriState> predicate) {
        this.path = rawId;
        this.predicate = predicate;
    }

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        TrinketsApi.registerTrinketPredicate(this.getRegistryId(), this.predicate);
    }

}
