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

package dev.jaxydog.astral.content.sound;

import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.register.Registered;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

/**
 * An extension of a {@link SoundEvent} that provides commonly used functionality.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralSoundEvent extends SoundEvent implements Registered.Common {

    /**
     * The sound event's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;

    /**
     * Creates a new sound event.
     *
     * @param path The sound event's identifier path.
     * @param distanceTraveled The distance that the sound travels in blocks.
     * @param useStaticDistance Whether to use static distance. This should be {@code true} if {@code distanceTraveled}
     * is not 16 blocks.
     *
     * @since 2.0.0
     */
    private AstralSoundEvent(String path, float distanceTraveled, boolean useStaticDistance) {
        super(Astral.getId(path), distanceTraveled, useStaticDistance);

        this.path = path;
    }

    /**
     * Creates a new sound event.
     *
     * @param path The sound event's identifier path.
     * @param distanceTraveled The distance that the sound travels in blocks.
     *
     * @since 2.0.0
     */
    public AstralSoundEvent(String path, float distanceTraveled) {
        this(path, distanceTraveled, true);
    }

    /**
     * Creates a new sound event.
     *
     * @param path The sound event's identifier path.
     *
     * @since 2.0.0
     */
    public AstralSoundEvent(String path) {
        this(path, 16F, false);
    }

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        Registry.register(Registries.SOUND_EVENT, this.getId(), this);
    }

}
