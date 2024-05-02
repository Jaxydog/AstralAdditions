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

import dev.jaxydog.astral.register.Registered;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData.Instance;
import net.minecraft.registry.Registry;

/**
 * An abstract class for easily implementing custom Origins actions.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public abstract class AstralAction<T> implements Registered.Common {

    /**
     * The action's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;

    /**
     * Creates a new action.
     *
     * @param path The action's identifier path.
     *
     * @since 2.0.0
     */
    public AstralAction(String path) {
        this.path = path;
    }

    /**
     * Executes the action.
     *
     * @param data The action's associated data.
     * @param value The value to execute the action on.
     *
     * @since 2.0.0
     */
    public abstract void execute(Instance data, T value);

    /**
     * Returns the action's associated factory.
     *
     * @return The action's associated factory.
     *
     * @since 2.0.0
     */
    public abstract AstralActionFactory<T> factory();

    /**
     * Returns the action's registry instance.
     *
     * @return The action's registry instance.
     *
     * @since 2.0.0
     */
    public abstract Registry<ActionFactory<T>> registry();

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        this.factory().register(this.registry());
    }

}
