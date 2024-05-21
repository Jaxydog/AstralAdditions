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

package dev.jaxydog.astral.mixin;

import dev.jaxydog.astral.utility.injected.AstralLightningEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements the {@value #PRESERVE_ITEMS_KEY} NBT key.
 *
 * @author Jaxydog
 * @since 1.4.0
 */
@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin extends Entity implements AstralLightningEntity {

    /**
     * The NBT key that determines whether a lightning bolt should preserve ground items.
     *
     * @since 1.4.0
     */
    @Unique
    private static final String PRESERVE_ITEMS_KEY = "PreserveItems";

    /**
     * Tracks whether this entity preserves ground items.
     *
     * @since 1.4.0
     */
    @Unique
    private boolean preservesItems = false;

    /**
     * Creates a new instance of this mixin.
     *
     * @param type The entity type.
     * @param world The current world.
     *
     * @since 1.4.0
     */
    public LightningEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean astral$preservesItems() {
        return this.preservesItems;
    }

    @Override
    public void astral$setPreservesItems(boolean preserve) {
        this.preservesItems = preserve;
    }

    /**
     * Reads the {@link #PRESERVE_ITEMS_KEY} NBT key.
     *
     * @param nbt The nbt compound.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.4.0
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtInject(NbtCompound nbt, CallbackInfo callbackInfo) {
        if (nbt.contains(PRESERVE_ITEMS_KEY)) {
            this.preservesItems = nbt.getBoolean(PRESERVE_ITEMS_KEY);
        }
    }

    /**
     * Writes the {@link #PRESERVE_ITEMS_KEY} NBT key.
     *
     * @param nbt The nbt compound.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.4.0
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtInject(NbtCompound nbt, CallbackInfo callbackInfo) {
        if (this.preservesItems) {
            nbt.putBoolean(PRESERVE_ITEMS_KEY, true);
        }
    }

}
