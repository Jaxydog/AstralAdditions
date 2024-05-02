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

package dev.jaxydog.astral.content.power.condition;

import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.power.AstralCondition;
import dev.jaxydog.astral.content.power.AstralConditionFactory;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * The distance condition.
 * <p>
 * This exists because the vanilla Origins implementation causes a crash on our server.
 *
 * @author Jaxydog
 * @since 1.1.0
 */
public class DistanceCondition extends AstralCondition<Entity> {

    /**
     * Creates a new distance condition.
     *
     * @param path The condition's identifier path.
     *
     * @since 1.1.0
     */
    public DistanceCondition(String path) {
        super(path);
    }

    @Override
    public boolean test(Instance data, Entity entity) {
        final List<Double> targetList = data.get("position");

        if (targetList.size() < 3) {
            Astral.LOGGER.warn("Expected three position coordinates!");

            return false;
        }

        final Vec3d targetPos = new Vec3d(targetList.get(0), targetList.get(1), targetList.get(2));
        final double distance = Math.sqrt(entity.getPos().squaredDistanceTo(targetPos));

        return data.<Comparison>get("comparison").compare(distance, data.getDouble("compare_to"));
    }

    @Override
    public AstralConditionFactory<Entity> factory() {
        final SerializableData data = new SerializableData().add("position", SerializableDataTypes.DOUBLES)
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.DOUBLE);

        return new AstralConditionFactory<>(this.getRegistryPath(), data, this::test);
    }

    @Override
    public Registry<ConditionFactory<Entity>> registry() {
        return ApoliRegistries.ENTITY_CONDITION;
    }

}
