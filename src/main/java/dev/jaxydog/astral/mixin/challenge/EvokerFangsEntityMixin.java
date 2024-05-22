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

package dev.jaxydog.astral.mixin.challenge;

import dev.jaxydog.astral.utility.ChallengeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Implements mob scaling.
 *
 * @author Jaxydog
 * @since 1.1.1
 */
@Mixin(EvokerFangsEntity.class)
public abstract class EvokerFangsEntityMixin extends Entity implements Ownable {

    /**
     * Creates a new instance of this mixin.
     *
     * @param type The entity type.
     * @param world The current world.
     *
     * @since 1.1.1
     */
    public EvokerFangsEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Scales the bite damage.
     *
     * @param damage The original damage.
     *
     * @return The scaled damage.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "damage", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
    ), index = 1
    )
    private float damageArgsInject(float damage) {
        final Entity entity = this.getOwner() != null ? this.getOwner() : this;

        if (!ChallengeHelper.shouldApplyScaling(entity)) return damage;

        final double additive = ChallengeHelper.getAttackAdditive(entity.getWorld());

        return damage + (float) ChallengeHelper.getScaledAdditive(entity, additive);
    }

}
