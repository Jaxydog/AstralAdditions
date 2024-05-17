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

import dev.jaxydog.astral.content.data.custom.MoonPhase;
import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.register.IgnoreRegistration;
import io.github.apace100.calio.SerializationHelper;
import io.github.apace100.calio.data.SerializableDataType;

import java.util.List;

/**
 * Contains definitions of all modded-in data types.
 *
 * @author Jaxydog
 * @since 1.0.0
 */
public final class AstralData extends ContentRegistrar {

    /**
     * The moon phase data type.
     *
     * @since 1.0.0
     */
    @IgnoreRegistration
    public static final SerializableDataType<MoonPhase> MOON_PHASE = SerializableDataType.enumValue(MoonPhase.class,
        SerializationHelper.buildEnumMap(MoonPhase.class, MoonPhase::getName)
    );

    /**
     * The moon phase array data type.
     *
     * @since 1.0.0
     */
    @IgnoreRegistration
    public static final SerializableDataType<List<MoonPhase>> MOON_PHASES = SerializableDataType.list(MOON_PHASE);

}
