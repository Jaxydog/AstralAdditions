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

import dev.jaxydog.astral.content.power.action.RepeatAction;
import dev.jaxydog.astral.register.ContentRegistrar;

/**
 * Contains definitions of all modded-in actions.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public final class AstralActions extends ContentRegistrar {

    /**
     * The repeat action.
     *
     * @since 2.0.0
     */
    public static final RepeatAction REPEAT = new RepeatAction("repeat");

}
