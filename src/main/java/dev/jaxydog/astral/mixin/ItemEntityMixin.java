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

import dev.jaxydog.astral.utility.injected.AstralLightningEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevents this from taking damage from lightning entities that preserve ground items.
 *
 * @author Jaxydog
 * @since 1.4.0
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    /**
     * Creates a new instance of this mixin.
     *
     * @param type The entity type.
     * @param world The current world.
     *
     * @since 1.4.0
     */
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Prevent any damage from being done by preserving lightning.
     *
     * @param source The damage source.
     * @param amount The amount of damage.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.4.0
     */
    @SuppressWarnings("RedundantCast")
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damageInject(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!(source.getAttacker() instanceof final LightningEntity lightning)) return;

        if (((AstralLightningEntity) lightning).astral$preservesItems()) {
            callbackInfo.setReturnValue(false);
        }
    }

}
