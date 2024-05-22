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
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.GuardianEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Implements mob scaling.
 *
 * @author Jaxydog
 * @since 1.1.1
 */
@Mixin(targets = "net.minecraft.entity.mob.GuardianEntity$FireBeamGoal")
public abstract class GuardianEntityFireBeamGoalMixin extends Goal {

    /**
     * The source guardian.
     *
     * @since 1.1.1
     */
    @Shadow
    @Final
    private GuardianEntity guardian;

    /**
     * Scales the beam's damage.
     *
     * @param damage The original damage.
     *
     * @return The scaled damage.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "tick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
    ), index = 1
    )
    private float tickArgsInject(float damage) {
        return ChallengeHelper.getScaledAttack(this.guardian, damage);
    }

}
