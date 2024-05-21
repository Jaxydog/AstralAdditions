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

package dev.jaxydog.astral.utility;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

/**
 * Simple extension class to provide type-level information to the sound engine.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
public class PositionedUnboundedSoundInstance extends PositionedSoundInstance {

    /**
     * Creates a new {@link PositionedUnboundedSoundInstance}.
     *
     * @param sound The sound event.
     * @param category The sound category.
     * @param volume The volume.
     * @param pitch The pitch.
     * @param random The randomness instance.
     * @param x The X position.
     * @param y The Y position.
     * @param z The Z position.
     *
     * @since 1.7.0
     */
    public PositionedUnboundedSoundInstance(
        SoundEvent sound, SoundCategory category, float volume, float pitch, Random random, double x, double y, double z
    ) {
        super(sound, category, volume, pitch, random, x, y, z);
    }

}
