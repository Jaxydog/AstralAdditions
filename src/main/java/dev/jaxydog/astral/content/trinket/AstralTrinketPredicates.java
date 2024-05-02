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

import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.register.IgnoreRegistration;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Pair;

import java.util.Optional;

/**
 * Contains definitions of all modded-in trinket predicates.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public final class AstralTrinketPredicates extends ContentRegistrar {

    /**
     * The tag containing all items that may not be worn in the cosmetic helmet slot.
     *
     * @since 2.0.0
     */
    @IgnoreRegistration
    public static final TagKey<Item> COSMETIC_HELMET_BLACKLIST = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("cosmetic_helmet_blacklist")
    );

    /**
     * The tag containing all items that may not be obscured by the cosmetic helmet slot.
     *
     * @since 2.0.0
     */
    @IgnoreRegistration
    public static final TagKey<Item> COSMETIC_HELMET_UNHIDEABLE = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("cosmetic_helmet_unhideable")
    );

    /**
     * The cosmetic helmet predicate.
     *
     * @since 2.0.0
     */
    public static final AstralTrinketPredicate COSMETIC_HELMET = new AstralTrinketPredicate("cosmetic_helmet",
        (stack, slot, entity) -> {
            if (stack.isIn(COSMETIC_HELMET_BLACKLIST)) return TriState.FALSE;
            if (MobEntity.getPreferredEquipmentSlot(stack) == EquipmentSlot.HEAD) return TriState.TRUE;

            return TriState.DEFAULT;
        }
    );

    /**
     * Returns the given entity's equipped cosmetic helmet item.
     *
     * @param entity The target entity.
     *
     * @return The given entity's equipped cosmetic helmet item.
     *
     * @since 2.0.0
     */
    public static ItemStack getCosmeticHelmet(LivingEntity entity) {
        final Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);

        return component.flatMap(c -> c.getEquipped(stack -> MobEntity.getPreferredEquipmentSlot(stack)
            == EquipmentSlot.HEAD).stream().filter(pair -> {
            final SlotType slot = pair.getLeft().inventory().getSlotType();

            return slot.getName().equals("cosmetic_helmet") && slot.getGroup().equals("head");
        }).map(Pair::getRight).findAny()).orElse(ItemStack.EMPTY);
    }

}
