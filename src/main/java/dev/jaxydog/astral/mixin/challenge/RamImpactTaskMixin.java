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
import net.minecraft.entity.ai.brain.task.RamImpactTask;
import net.minecraft.entity.passive.GoatEntity;
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
@Mixin(RamImpactTask.class)
public abstract class RamImpactTaskMixin extends MultiTickTask<GoatEntity> {

    /**
     * Creates a new instance of this mixin.
     *
     * @param requiredMemoryState The entity's required memory states.
     * @param minRunTime The minimum task run time.
     * @param maxRunTime The maximum task run time.
     *
     * @since 1.1.1
     */
    public RamImpactTaskMixin(
        Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState, int minRunTime, int maxRunTime
    ) {
        super(requiredMemoryState, minRunTime, maxRunTime);
    }

    /**
     * Scales the goat's impact damage.
     *
     * @param damage The original damage.
     * @param entity The source entity.
     *
     * @return The scaled damage.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/GoatEntity;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
        )
    )
    private float keepRunningArgsInject(float damage, @Local(argsOnly = true) GoatEntity entity) {
        if (!ChallengeHelper.shouldApplyScaling(entity)) return damage;

        final double additive = ChallengeHelper.getAttackAdditive(entity.getWorld());

        return damage + (float) ChallengeHelper.getScaledAdditive(entity, additive);
    }

}
