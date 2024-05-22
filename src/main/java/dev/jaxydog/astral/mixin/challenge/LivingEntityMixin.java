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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.jaxydog.astral.utility.ChallengeHelper;
import dev.jaxydog.astral.utility.injected.AstralLivingEntity;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements the mob challenge system's health changes.
 *
 * @author Jaxydog
 * @since 1.1.1
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, AstralLivingEntity {

    /**
     * The health tracker.
     *
     * @since 1.1.1
     */
    @Shadow
    @Final
    private static TrackedData<Float> HEALTH;

    /**
     * If true, the mob will *always* scale.
     *
     * @since 1.5.0
     */
    @Unique
    private boolean forceChallengeScaling = false;

    /**
     * Stores whether this entity ignores challenge scaling rules.
     *
     * @since 1.1.1
     */
    @Unique
    private boolean ignoreChallengeScaling = false;
    /**
     * Stores whether the entity needs to reset its health.
     * <p>
     * This should only be true if the gamerules are updated or when the entity is first spawned.
     *
     * @since 1.1.1
     */
    @Unique
    private boolean shouldResetHealth = true;
    /**
     * Stores whether mob challenge was previously enabled to determine whether the entity's health should be updated.
     *
     * @since 1.1.1
     */
    @Unique
    private boolean lastEnableState = ChallengeHelper.isEnabled(this.self().getWorld());
    /**
     * Stores the previously used health additive value to check whether the entity's health should be updated.
     *
     * @since 1.1.1
     */
    @Unique
    private double lastHealthAdditive = ChallengeHelper.getHealthAdditive(this.self().getWorld());
    /**
     * Stores the previously used chunk step value to check whether the entity's health should be updated.
     *
     * @since 1.1.1
     */
    @Unique
    private double lastChunkStep = ChallengeHelper.getChunkStep(this.self().getWorld());

    /**
     * Creates a new instance of this mixin.
     *
     * @param type The entity type.
     * @param world The current world.
     *
     * @since 1.1.1
     */
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean astral$ignoresChallengeScaling() {
        return this.ignoreChallengeScaling;
    }

    @Override
    public boolean astral$forcesChallengeScaling() {
        return this.forceChallengeScaling;
    }

    /**
     * Provides a scaled maximum health value if mob challenge scaling is enabled.
     *
     * @param health The original health.
     *
     * @return The scaled health.
     *
     * @since 1.1.1
     */
    @ModifyReturnValue(method = "getMaxHealth", at = @At("RETURN"))
    private float scaleHealth(float health) {
        if (!ChallengeHelper.shouldApplyScaling(this) || this.getWorld().isClient()) return health;

        final World world = this.getWorld();
        final double additive = ChallengeHelper.getHealthAdditive(world);

        if (this.lastHealthAdditive != additive) {
            this.lastHealthAdditive = additive;
            this.shouldResetHealth = true;
        }

        final int chunkStep = ChallengeHelper.getChunkStep(world);

        if (this.lastChunkStep != chunkStep) {
            this.shouldResetHealth = true;
            this.lastChunkStep = chunkStep;
        }

        return health + (float) ChallengeHelper.getScaledAdditive(this, additive);
    }

    /**
     * Returns the mixin's 'this' instance.
     *
     * @return The mixin's 'this' instance.
     *
     * @since 1.1.1
     */
    @Unique
    private LivingEntity self() {
        return (LivingEntity) (Object) this;
    }

    /**
     * Automatically updates an entity's maximum health if necessary.
     *
     * @param callbackInfo The injection callback information.
     *
     * @since 1.1.1
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickInject(CallbackInfo callbackInfo) {
        if (!ChallengeHelper.shouldApplyScaling(this) || this.getWorld().isClient()) return;

        final boolean enabled = ChallengeHelper.isEnabled(this.getWorld());
        final float maxHealth = this.self().getMaxHealth();

        if (this.lastEnableState != enabled) {
            this.lastEnableState = enabled;
            this.shouldResetHealth = true;
        }

        if (this.shouldResetHealth) {
            this.setHealthData(maxHealth);
            this.shouldResetHealth = false;
        }
    }

    /**
     * Convenience method to set the entity's current health to the given value without calling
     * {@link LivingEntity#getMaxHealth()}.
     *
     * @param health The new health.
     *
     * @since 1.1.1
     */
    @Unique
    private void setHealthData(float health) {
        this.getDataTracker().set(HEALTH, Math.max(0, health));
    }

    /**
     * Deserializes the `ignoreChallengeScaling` field.
     *
     * @param nbt The NBT compound.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.1.1
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtInject(NbtCompound nbt, CallbackInfo callbackInfo) {
        if (nbt.contains(ChallengeHelper.IGNORE_KEY, NbtElement.BYTE_TYPE)) {
            this.ignoreChallengeScaling = nbt.getBoolean(ChallengeHelper.IGNORE_KEY);
        }
        if (nbt.contains(ChallengeHelper.FORCE_KEY, NbtElement.BYTE_TYPE)) {
            this.forceChallengeScaling = nbt.getBoolean(ChallengeHelper.IGNORE_KEY);
        }
    }

    /**
     * Serializes the `ignoreChallengeScaling` field.
     *
     * @param nbt The NBT compound.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.1.1
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtInject(NbtCompound nbt, CallbackInfo callbackInfo) {
        if (this.ignoreChallengeScaling) {
            nbt.putBoolean(ChallengeHelper.IGNORE_KEY, true);
        }
        if (this.forceChallengeScaling) {
            nbt.putBoolean(ChallengeHelper.IGNORE_KEY, true);
        }
    }

}
