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
import dev.jaxydog.astral.utility.injected.SprayableEntity;
import dev.onyxstudios.cca.api.v3.component.ComponentAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Nameable;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput, ComponentAccess {

    @Shadow
    public abstract World getWorld();

    @SuppressWarnings({ "RedundantCast", "UnreachableCode" })
    @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
    private void onStruckByLightningInject(ServerWorld world, LightningEntity bolt, CallbackInfo callbackInfo) {
        if (((Entity) (Object) this) instanceof ItemEntity) {
            if (((AstralLightningEntity) bolt).astral$preservesItems()) callbackInfo.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInject(CallbackInfo callbackInfo) {
        if (this.getWorld().isClient()) return;
        if (!(this instanceof final SprayableEntity sprayable)) return;

        sprayable.astral$sprayTick();
    }

}
