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
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.mob.WardenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

/**
 * Implements mob scaling.
 *
 * @author Jaxydog
 * @since 1.1.1
 */
@Mixin(SonicBoomTask.class)
public abstract class SonicBoomTaskMixin extends MultiTickTask<WardenEntity> {

    /**
     * Creates a new instance of this mixin.
     *
     * @param requiredMemoryState The required memory states.
     * @param minRunTime The minimum task run time.
     * @param maxRunTime The maximum task run time.
     *
     * @since 1.1.1
     */
    public SonicBoomTaskMixin(
        Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState, int minRunTime, int maxRunTime
    ) {
        super(requiredMemoryState, minRunTime, maxRunTime);
    }

    /**
     * Scales the impact damage.
     *
     * @param damage The original damage.
     * @param entity The source entity.
     *
     * @return The scaled damage.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "method_43265(Lnet/minecraft/entity/mob/WardenEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
        )
    )
    private static float scaleDamage(float damage, @Local(argsOnly = true) WardenEntity entity) {
        return ChallengeHelper.getScaledAttack(entity, damage);
    }

}
