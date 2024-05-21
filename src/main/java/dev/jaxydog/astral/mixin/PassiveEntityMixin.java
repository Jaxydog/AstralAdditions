/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2024 Icepenguin
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

import dev.jaxydog.astral.utility.CowType;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Partial implementation of cow entity types.
 *
 * @author Icepenguin
 * @since 1.7.0
 */
@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMixin extends PathAwareEntity {

    /**
     * Creates a new instance of this mixin.
     *
     * @param entityType The entity type.
     * @param world The current world.
     *
     * @since 1.7.0
     */
    protected PassiveEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Returns whether this entity is a baby.
     *
     * @return Whether this entity is a baby.
     *
     * @since 1.7.0
     */
    @Shadow
    public abstract boolean isBaby();

    /**
     * Provides an overridable method for randomly setting a cow to be pink.
     *
     * @param cow The cow entity.
     *
     * @since 1.7.0
     */
    @Unique
    protected void pinkCowRng(CowEntity cow) { }

    /**
     * Randomly assigns a cow's type.
     *
     * @param world The current world.
     * @param difficulty The world's difficulty.
     * @param spawnReason The reason this entity is spawning.
     * @param entityData The entity's data.
     * @param entityNbt The entity's NBT data.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.7.0
     */
    @SuppressWarnings("UnreachableCode")
    @Inject(method = "initialize", at = @At("TAIL"))
    private void randomizeCowType(
        ServerWorldAccess world,
        LocalDifficulty difficulty,
        SpawnReason spawnReason,
        EntityData entityData,
        NbtCompound entityNbt,
        CallbackInfoReturnable<EntityData> callbackInfo
    ) {
        final PassiveEntity self = (PassiveEntity) (Object) this;

        if (self instanceof final CowEntity cow && !(self instanceof MooshroomEntity)) {
            this.pinkCowRng(cow);
        }
    }

    /**
     * Tells this entity to track what type of cow it is, if it is a cow.
     *
     * @param callbackInfo The injection callback information.
     *
     * @since 1.7.0
     */
    @SuppressWarnings("UnreachableCode")
    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void trackCowType(CallbackInfo callbackInfo) {
        if ((PassiveEntity) (Object) this instanceof CowEntity) {
            this.dataTracker.startTracking(CowType.COW_TYPE, CowType.BROWN.asString());
        }
    }

}
