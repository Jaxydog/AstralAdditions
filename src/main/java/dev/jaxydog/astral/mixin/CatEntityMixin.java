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

import dev.jaxydog.astral.utility.injected.SprayableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements the {@link SprayableEntity} interface.
 *
 * @author Jaxydog
 * @since 1.6.0
 */
@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity implements SprayableEntity, VariantHolder<CatVariant> {

    /**
     * The entity that sprayed this cat.
     *
     * @since 1.6.0
     */
    @Unique
    private @Nullable LivingEntity spraySource;

    /**
     * The remaining spray duration.
     *
     * @since 1.6.0
     */
    @Unique
    private int sprayDuration = 0;

    /**
     * Creates a new instance of this mixin.
     *
     * @param entityType The entity type.
     * @param world The current world.
     */
    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Causes this cat to hiss.
     *
     * @since 1.6.0
     */
    @Shadow
    public abstract void hiss();

    @Override
    public void astral$setSprayed(@Nullable LivingEntity source, int ticks, boolean initialSpray) {
        if (source == null) return;

        this.spraySource = source;
        this.sprayDuration = Math.max(0, ticks);

        if (initialSpray && this.astral$isSprayed()) {
            this.hiss();

            if (this.isOnGround()) this.jump();
        }
    }

    @Override
    public @Nullable LivingEntity astral$getSpraySource() {
        return this.spraySource;
    }

    @Override
    public int astral$getSprayTicks() {
        return this.sprayDuration;
    }

    @Override
    public boolean astral$canSpray() {
        return !this.astral$isSprayed() && !(this.isTamed() && this.isSitting());
    }

    /**
     * Initializes the escape-spray goal.
     *
     * @param callbackInfo The injection callback information.
     *
     * @since 1.6.0
     */
    @Inject(method = "initGoals", at = @At("HEAD"))
    private void initGoalsInject(CallbackInfo callbackInfo) {
        this.goalSelector.add(1, new EscapeSprayGoal<>(this, 1.5));
    }

}
