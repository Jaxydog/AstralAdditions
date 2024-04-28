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

package dev.jaxydog.astral.content.data;

import net.minecraft.world.LunarWorldView;

/**
 * Represents the phases of the moon
 *
 * @author Jaxydog
 * @since 1.0.0
 */
public enum MoonPhase {

    /**
     * An invalid phase. This value is (normally) only present when calling {@link MoonPhase#from(String)} with an
     * invalid identifier string.
     *
     * @since 1.0.0
     */
    NONE(-1),
    /**
     * A full moon.
     *
     * @since 1.0.0
     */
    FULL_MOON(0),
    /**
     * A waning gibbous.
     *
     * @since 1.0.0
     */
    WANING_GIBBOUS(1),
    /**
     * A third quarter.
     *
     * @since 1.0.0
     */
    THIRD_QUARTER(2),
    /**
     * A waning crescent.
     *
     * @since 1.0.0
     */
    WANING_CRESCENT(3),
    /**
     * A new moon.
     *
     * @since 1.0.0
     */
    NEW_MOON(4),
    /**
     * A waxing crescent.
     *
     * @since 1.0.0
     */
    WAXING_CRESCENT(5),
    /**
     * A first quarter.
     *
     * @since 1.0.0
     */
    FIRST_QUARTER(6),
    /**
     * A waxing gibbous.
     *
     * @since 1.0.0
     */
    WAXING_GIBBOUS(7);

    /**
     * The numeric phase identifier.
     *
     * @since 2.0.0
     */
    private final int phase;

    MoonPhase(int phase) {
        this.phase = phase;
    }

    /**
     * Returns the moon phase with the given name, if it exists.
     *
     * @param name The name of the phase.
     *
     * @return The moon phase represented by the given string.
     *
     * @since 1.0.0
     */
    public static MoonPhase from(String name) {
        final String lower = name.toLowerCase();

        for (final MoonPhase phase : values()) {
            if (phase.getName().equals(lower)) return phase;
        }

        return NONE;
    }

    /**
     * Returns the phase's name as a lowercase, underscore-separated string.
     *
     * @return The phase name.
     *
     * @since 1.0.0
     */
    public String getName() {
        return this.toString().toLowerCase();
    }

    /**
     * Returns the numeric phase identifier of this phase.
     *
     * @return The numeric identifier.
     *
     * @since 2.0.0
     */
    public int getNumeric() {
        return this.phase;
    }

    /**
     * Returns whether this phase is the current moon phase.
     *
     * @param world The current world.
     *
     * @return Whether this is the current phase.
     *
     * @since 1.0.0
     */
    public boolean isCurrent(LunarWorldView world) {
        return world.getMoonPhase() == this.getNumeric();
    }

}
