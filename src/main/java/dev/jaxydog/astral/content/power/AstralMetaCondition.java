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
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.calio.data.SerializableDataType;

/**
 * An abstract class for easily implementing custom Origins conditions with multiple data types.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public abstract class AstralMetaCondition implements Registered.Common {

    /**
     * The condition's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;

    /**
     * Creates a new meta condition.
     *
     * @param path The condition's identifier path.
     *
     * @since 2.0.0
     */
    public AstralMetaCondition(String path) {
        this.path = path;
    }

    /**
     * Tests the condition.
     *
     * @param data The condition's associated data.
     * @param value The value to test.
     *
     * @since 2.0.0
     */
    public abstract <T> boolean test(Instance data, T value);

    /**
     * Returns the condition's associated factory.
     *
     * @param type The condition's type.
     * @param <T> The type of the value to test.
     *
     * @return The condition's associated factory.
     *
     * @since 2.0.0
     */
    public abstract <T> AstralConditionFactory<T> factory(SerializableDataType<ConditionFactory<T>.Instance> type);

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        this.factory(ApoliDataTypes.BIENTITY_CONDITION).register(ApoliRegistries.BIENTITY_CONDITION);
        this.factory(ApoliDataTypes.BLOCK_CONDITION).register(ApoliRegistries.BLOCK_CONDITION);
        this.factory(ApoliDataTypes.ENTITY_CONDITION).register(ApoliRegistries.ENTITY_CONDITION);
        this.factory(ApoliDataTypes.ITEM_CONDITION).register(ApoliRegistries.ITEM_CONDITION);
    }

}
