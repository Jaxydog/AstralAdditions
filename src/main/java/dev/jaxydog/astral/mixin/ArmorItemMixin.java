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

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import dev.jaxydog.astral.content.item.AstralArmorItem.Material;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item.Settings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.UUID;

/**
 * Implements knockback resistance for modded armor items.
 *
 * @author Jaxydog
 * @since 1.0.0
 */
@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {

    /**
     * The item class' attribute modifiers.
     *
     * @since 1.0.0
     */
    @Shadow
    @Final
    private static EnumMap<Type, UUID> MODIFIERS;

    /**
     * The item's knockback resistance.
     *
     * @since 1.0.0
     */
    @Shadow
    @Final
    protected float knockbackResistance;

    /**
     * The entity's attribute modifiers.
     *
     * @since 1.0.0
     */
    @Shadow
    @Final
    @Mutable
    private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    /**
     * Initializes the armor resistance attribute modifier.
     *
     * @param material The armor material.
     * @param type The item's type.
     * @param settings The item's settings.
     * @param info The injection callback information.
     *
     * @since 1.0.0
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(ArmorMaterial material, Type type, Settings settings, CallbackInfo info) {
        if (!(material instanceof Material)) return;

        final UUID uuid = MODIFIERS.get(type);
        final Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        final EntityAttributeModifier modifier = new EntityAttributeModifier(uuid,
            "Armor knockback resistance",
            this.knockbackResistance,
            Operation.ADDITION
        );

        this.attributeModifiers.forEach(builder::put);
        builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, modifier);
        this.attributeModifiers = builder.build();
    }

}
