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

import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

/**
 * Simple extension class to provide type-level information to the sound engine.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
public class EntityTrackingUnboundedSoundInstance extends EntityTrackingSoundInstance {

    /**
     * Creates a new {@link EntityTrackingUnboundedSoundInstance}.
     *
     * @param sound The sound event.
     * @param category The sound category.
     * @param volume The volume.
     * @param pitch The pitch.
     * @param entity The entity to track.
     * @param seed The randomness seed.
     *
     * @since 1.7.0
     */
    public EntityTrackingUnboundedSoundInstance(
        SoundEvent sound, SoundCategory category, float volume, float pitch, Entity entity, long seed
    ) {
        super(sound, category, volume, pitch, entity, seed);
    }

}
