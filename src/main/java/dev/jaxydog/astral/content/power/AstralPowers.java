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

import dev.jaxydog.astral.content.power.custom.*;
import dev.jaxydog.astral.register.ContentRegistrar;

/**
 * Contains definitions for all modded-in powers.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public final class AstralPowers extends ContentRegistrar {

    /**
     * The action-on-key power.
     *
     * @since 2.0.0
     */
    public static final AstralPowerFactory<ActionOnKeyPower> ACTION_ON_KEY = ActionOnKeyPower.getKeyFactory();

    /**
     * The action-on-spray power.
     *
     * @since 2.0.0
     */
    public static final AstralPowerFactory<ActionOnSprayPower> ACTION_ON_SPRAY = ActionOnSprayPower.getFactory();

    /**
     * The action-when-sprayed power.
     *
     * @since 2.0.0
     */
    public static final AstralPowerFactory<ActionWhenSprayedPower> ACTION_WHEN_SPRAYED = ActionWhenSprayedPower.getFactory();

    /**
     * The modify scale power.
     *
     * @since 2.0.0
     */
    public static final AstralPowerFactory<ModifyScalePower> MODIFY_SCALE = ModifyScalePower.getFactory();

    /**
     * The ticking cooldown power.
     *
     * @since 2.0.0
     */
    public static final AstralPowerFactory<TickingCooldownPower> TICKING_COOLDOWN = TickingCooldownPower.getFactory();

}
