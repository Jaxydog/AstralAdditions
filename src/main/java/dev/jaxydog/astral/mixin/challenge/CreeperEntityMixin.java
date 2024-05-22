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
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
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
@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity implements SkinOverlayOwner {

    /**
     * The maximum allowed explosion power.
     *
     * @since 1.1.1
     */
    @Unique
    private static final float MAX_POWER = 50F;

    /**
     * Creates a new instance of this mixin.
     *
     * @param entityType The entity type.
     * @param world The current world.
     *
     * @since 1.1.1
     */
    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Modifies the creeper's explosion strength to account for mob challenge scaling.
     *
     * @param power The original power value.
     *
     * @return The modified power.
     *
     * @since 1.1.1
     */
    @ModifyArg(
        method = "explode", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"
    ), index = 4
    )
    private float powerVarInject(float power) {
        return ChallengeHelper.getScaledExplosion(this, MAX_POWER, power);
    }

}
