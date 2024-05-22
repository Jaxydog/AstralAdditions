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

package dev.jaxydog.astral.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.astral.utility.EntityTrackingUnboundedSoundInstance;
import dev.jaxydog.astral.utility.PositionedUnboundedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Un-clamps the sounds played when receiving the right packet type.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {

    /**
     * Returns the given category's sound volume.
     *
     * @param category The sound category.
     *
     * @return The volume.
     *
     * @since 1.7.0
     */
    @Shadow
    protected abstract float getSoundVolume(@Nullable SoundCategory category);

    /**
     * Un-binds the volume if possible.
     *
     * @param self The current instance.
     * @param instance The sound instance.
     * @param original The original operation.
     *
     * @return The unbounded volume.
     *
     * @since 1.7.0
     */
    @WrapOperation(
        method = "method_19754(Lnet/minecraft/client/sound/SoundInstance;Lnet/minecraft/client/sound/Channel$SourceManager;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/SoundSystem;getAdjustedVolume(Lnet/minecraft/client/sound/SoundInstance;)F"
        )
    )
    private float updateUnboundedVolume(
        SoundSystem self, SoundInstance instance, Operation<Float> original
    ) {
        if (instance instanceof PositionedUnboundedSoundInstance) {
            return Math.max(0F, instance.getVolume() * this.getSoundVolume(instance.getCategory()));
        } else {
            return original.call(self, instance);
        }
    }

    /**
     * Un-binds the volume if possible.
     *
     * @param self The current instance.
     * @param volume The volume.
     * @param category The sound category.
     * @param original The original operation.
     * @param instance The sound instance.
     *
     * @return The unbounded volume.
     *
     * @since 1.7.0
     */
    @WrapOperation(
        method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/sound/SoundSystem;getAdjustedVolume(FLnet/minecraft/sound/SoundCategory;)F"
    )
    )
    private float playUnboundedVolume(
        SoundSystem self,
        float volume,
        SoundCategory category,
        Operation<Float> original,
        @Local(argsOnly = true) SoundInstance instance
    ) {
        if (instance instanceof PositionedUnboundedSoundInstance) {
            return Math.max(0F, volume * this.getSoundVolume(category));
        } else {
            return original.call(self, volume, category);
        }
    }

    /**
     * Un-binds the pitch if possible.
     *
     * @param self The current instance.
     * @param instance The sound instance.
     * @param original The original operation.
     *
     * @return The unbounded pitch.
     *
     * @since 1.7.0
     */
    @WrapOperation(
        method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/sound/SoundSystem;getAdjustedPitch(Lnet/minecraft/client/sound/SoundInstance;)F"
    )
    )
    private float playUnboundedPitch(SoundSystem self, SoundInstance instance, Operation<Float> original) {
        if (instance instanceof PositionedUnboundedSoundInstance) {
            // Not true zero, but there's no check for a pitch of `0`, and my worry is that it will produce a sound that never finishes playing.
            return Math.max(0.001F, instance.getPitch());
        } else {
            return original.call(self, instance);
        }
    }

    /**
     * Un-binds the volume if possible.
     *
     * @param self The current instance.
     * @param instance The sound instance.
     * @param original The original operation.
     *
     * @return The unbounded volume.
     *
     * @since 1.7.0
     */
    @WrapOperation(
        method = "tick()V", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/sound/SoundSystem;getAdjustedVolume(Lnet/minecraft/client/sound/SoundInstance;)F"
    )
    )
    private float tickUnboundedVolume(SoundSystem self, SoundInstance instance, Operation<Float> original) {
        if (instance instanceof EntityTrackingUnboundedSoundInstance) {
            return Math.max(0F, instance.getVolume() * this.getSoundVolume(instance.getCategory()));
        } else {
            return original.call(self, instance);
        }
    }

    /**
     * Un-binds the pitch if possible.
     *
     * @param self The current instance.
     * @param instance The sound instance.
     * @param original The original operation.
     *
     * @return The unbounded pitch.
     *
     * @since 1.7.0
     */
    @WrapOperation(
        method = "tick()V", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/sound/SoundSystem;getAdjustedPitch(Lnet/minecraft/client/sound/SoundInstance;)F"
    )
    )
    private float tickUnboundedPitch(SoundSystem self, SoundInstance instance, Operation<Float> original) {
        if (instance instanceof EntityTrackingUnboundedSoundInstance) {
            // Not true zero, but there's no check for a pitch of `0`, and my worry is that it will produce a sound that never finishes playing.
            return Math.max(0.001F, instance.getPitch());
        } else {
            return original.call(self, instance);
        }
    }

}
