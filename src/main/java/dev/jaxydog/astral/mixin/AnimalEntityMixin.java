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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {

    @Unique
    private static final String COW_TYPE_KEY = "CowType";

    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCowTypeInject(NbtCompound nbt, CallbackInfo callbackInfo) {
        final AnimalEntity self = (AnimalEntity) (Object) this;

        if (!(self instanceof CowEntity) || self instanceof MooshroomEntity) return;
        if (!this.getDataTracker().containsKey(CowType.COW_TYPE)) return;

        final String type;

        if (nbt.contains(COW_TYPE_KEY, NbtElement.STRING_TYPE)) {
            type = nbt.getString(COW_TYPE_KEY);
        } else {
            type = CowType.BROWN.asString();
        }

        this.getDataTracker().set(CowType.COW_TYPE, type);
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCowTypeInject(NbtCompound nbt, CallbackInfo callbackInfo) {
        final AnimalEntity self = (AnimalEntity) (Object) this;

        if (!(self instanceof CowEntity) || self instanceof MooshroomEntity) return;
        if (!this.getDataTracker().containsKey(CowType.COW_TYPE)) return;

        nbt.putString(COW_TYPE_KEY, this.getDataTracker().get(CowType.COW_TYPE));
    }

}
