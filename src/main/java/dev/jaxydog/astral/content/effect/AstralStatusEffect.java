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

package dev.jaxydog.astral.content.effect;

import dev.jaxydog.astral.register.Registered.Common;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * An extension of a {@link StatusEffect} that provides commonly used functionality.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralStatusEffect extends StatusEffect implements Common {

    /**
     * The status effect's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;

    /**
     * Creates a new status effect using the given properties.
     *
     * @param path The effect's identifier path.
     * @param category The effect's category.
     * @param color The effect's potion and particle color.
     *
     * @since 2.0.0
     */
    public AstralStatusEffect(String path, StatusEffectCategory category, int color) {
        super(category, color);

        this.path = path;
    }

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        Registry.register(Registries.STATUS_EFFECT, this.getRegistryId(), this);
    }

}
