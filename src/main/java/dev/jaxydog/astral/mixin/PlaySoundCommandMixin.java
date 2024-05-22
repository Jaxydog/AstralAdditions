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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.jaxydog.astral.utility.PlayUnboundedSoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.PlaySoundCommand;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Provides the primary implementation for un-clamped {@code /playsound} pitches.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
@Mixin(value = PlaySoundCommand.class, priority = 0)
public abstract class PlaySoundCommandMixin {

    /**
     * Changes the maximum pitch of the {@code /playsound} command.
     *
     * @param min The previous value.
     *
     * @return The new maximum pitch.
     *
     * @since 1.7.0
     */
    @ModifyArg(
        method = "makeArgumentsForCategory", at = @At(
        value = "INVOKE",
        target = "Lcom/mojang/brigadier/arguments/FloatArgumentType;floatArg(FF)Lcom/mojang/brigadier/arguments/FloatArgumentType;",
        ordinal = 0
    ), index = 1
    )
    private static float changeMaxPitch(float min) {
        return Float.MAX_VALUE;
    }

    /**
     * Replaces the original packet with a custom one that uses our own methods.
     *
     * @param sound The sound event.
     * @param category The sound category.
     * @param x The X position.
     * @param y The Y position.
     * @param z The Z position.
     * @param volume The sound's volume.
     * @param pitch The sound's pitch.
     * @param seed The randomness seed.
     * @param original The original packet constructor.
     *
     * @return A custom sound packet.
     *
     * @since 1.7.0
     */
    @WrapOperation(
        method = "execute", at = @At(
        value = "NEW",
        target = "(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;DDDFFJ)Lnet/minecraft/network/packet/s2c/play/PlaySoundS2CPacket;"
    )
    )
    private static PlaySoundS2CPacket replacePacket(
        RegistryEntry<SoundEvent> sound,
        SoundCategory category,
        double x,
        double y,
        double z,
        float volume,
        float pitch,
        long seed,
        Operation<PlaySoundS2CPacket> original
    ) {
        return new PlayUnboundedSoundS2CPacket(sound, category, x, y, z, volume, pitch, seed);
    }

}
