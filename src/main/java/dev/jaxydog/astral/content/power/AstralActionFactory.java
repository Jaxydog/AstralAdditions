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

import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.register.Registered;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

/**
 * An extension of an {@link ActionFactory} that provides additional functionality.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralActionFactory<T> extends ActionFactory<T> implements Registered {

    /**
     * Creates a new action factory.
     *
     * @param path The factory's identifier path.
     * @param data The factory's serialization data.
     * @param effect The factory's executed action.
     *
     * @since 2.0.0
     */
    public AstralActionFactory(String path, SerializableData data, BiConsumer<SerializableData.Instance, T> effect) {
        super(Astral.getId(path), data, effect);
    }

    /**
     * Registers this factory within the provided registry.
     *
     * @param registry The target registry.
     *
     * @since 2.0.0
     */
    public void register(Registry<ActionFactory<T>> registry) {
        Registry.register(registry, this.getRegistryId(), this);
    }

    @Override
    public String getRegistryPath() {
        return this.getSerializerId().getPath();
    }

    @Override
    public Identifier getRegistryId() {
        return this.getSerializerId();
    }

}
