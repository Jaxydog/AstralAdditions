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

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

/**
 * An extension of a {@link Power} that, one day, may provide additional functionality.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public abstract class AstralPower extends Power {

    /**
     * Creates a new power.
     *
     * @param type The power's type.
     * @param entity The holding entity.
     *
     * @since 2.0.0
     */
    public AstralPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

}
