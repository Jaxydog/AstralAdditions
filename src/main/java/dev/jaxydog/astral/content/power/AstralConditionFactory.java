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
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiPredicate;

/**
 * An extension of a {@link ConditionFactory} that provides additional functionality.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralConditionFactory<T> extends ConditionFactory<T> implements Registered {

    /**
     * Creates a new condition factory.
     *
     * @param path The factory's identifier path.
     * @param data The factory's serialization data.
     * @param condition The factory's test condition.
     *
     * @since 2.0.0
     */
    public AstralConditionFactory(
        String path, SerializableData data, BiPredicate<SerializableData.Instance, T> condition
    ) {
        super(Astral.getId(path), data, condition::test);
    }

    /**
     * Registers this factory within the provided registry.
     *
     * @param registry The target registry.
     *
     * @since 2.0.0
     */
    public void register(Registry<ConditionFactory<T>> registry) {
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
