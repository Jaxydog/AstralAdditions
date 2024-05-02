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

package dev.jaxydog.astral.content.power;

import dev.jaxydog.astral.content.power.condition.DistanceCondition;
import dev.jaxydog.astral.content.power.condition.MoonPhaseCondition;
import dev.jaxydog.astral.content.power.condition.UnobstructedBlockInRadiusCondition;
import dev.jaxydog.astral.register.ContentRegistrar;

/**
 * Contains definitions for all modded-in conditions.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public final class AstralConditions extends ContentRegistrar {

    /**
     * The distance condition.
     *
     * @since 2.0.0
     */
    public static final DistanceCondition DISTANCE = new DistanceCondition("distance");

    /**
     * The moon phase condition.
     *
     * @since 2.0.0
     */
    public static final MoonPhaseCondition MOON_PHASE = new MoonPhaseCondition("moon_phase");

    /**
     * The unobstructed block in radius condition.
     *
     * @since 2.0.0
     */
    public static final UnobstructedBlockInRadiusCondition UNOBSTRUCTED_BLOCK_IN_RADIUS = new UnobstructedBlockInRadiusCondition(
        "unobstructed_block_in_radius");

}
