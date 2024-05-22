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

import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.astral.utility.ChallengeHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Hoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Implements mob scaling.
 *
 * @author Jaxydog
 * @since 1.1.1
 */
@Mixin(Hoglin.class)
public interface HoglinMixin {

    /**
     * Scales the hoglin's shove damage.
     *
     * @param damage The original damage.
     * @param entity The source entity.
     *
     * @return The scaled damage.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "tryAttack", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
    )
    )
    private static float tryAttackArgsInject(float damage, @Local(ordinal = 0, argsOnly = true) LivingEntity entity) {
        return ChallengeHelper.getScaledAttack(entity, damage);
    }

}
