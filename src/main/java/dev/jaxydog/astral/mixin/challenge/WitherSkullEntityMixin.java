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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Implements mob scaling.
 *
 * @author Jaxydog
 * @since 1.1.1
 */
@Mixin(WitherSkullEntity.class)
public abstract class WitherSkullEntityMixin extends ExplosiveProjectileEntity {

    /**
     * The maximum explosion power.
     *
     * @since 1.1.1
     */
    @Unique
    private static final float MAX_POWER = 25F;

    /**
     * Creates a new instance of this mixin.
     *
     * @param type The entity type.
     * @param owner The source entity.
     * @param x The X velocity.
     * @param y The Y velocity.
     * @param z The Z velocity.
     * @param world The current world.
     *
     * @since 1.1.1
     */
    public WitherSkullEntityMixin(
        EntityType<? extends ExplosiveProjectileEntity> type,
        LivingEntity owner,
        double x,
        double y,
        double z,
        World world
    ) {
        super(type, owner, x, y, z, world);
    }

    /**
     * Scales the collision explosion.
     *
     * @param power The original power.
     *
     * @return The scaled power.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "onCollision", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"
    ), index = 4
    )
    private float onCollisionArgsInject(float power) {
        return ChallengeHelper.getScaledExplosion(this.getOwner(), MAX_POWER, power);
    }

    /**
     * Scales the collision damage.
     *
     * @param damage The original damage.
     *
     * @return The scaled damage.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "onEntityHit", at = @At(
        value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
    ), index = 1
    )
    private float onEntityHitArgsInject(float damage) {
        return ChallengeHelper.getScaledAttack(this.getOwner(), damage);
    }

}
