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

import dev.jaxydog.astral.content.data.AstralData;
import dev.jaxydog.astral.content.data.MoonPhase;
import dev.jaxydog.astral.content.power.AstralCondition;
import dev.jaxydog.astral.content.power.AstralConditionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableData.Instance;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The moon phase condition.
 *
 * @author Jaxydog
 * @since 1.1.0
 */
public class MoonPhaseCondition extends AstralCondition<Entity> {

    /**
     * Creates a new moon phase condition.
     *
     * @param path The condition's identifier path.
     *
     * @since 2.0.0
     */
    public MoonPhaseCondition(String path) {
        super(path);
    }

    @Override
    public boolean test(Instance data, Entity value) {
        final MoonPhase phase = data.get("phase");
        final World world = value.getWorld();

        if (phase != MoonPhase.NONE) {
            return phase.isCurrent(world);
        } else {
            return data.<List<MoonPhase>>get("phases").stream().anyMatch(e -> e.isCurrent(world));
        }
    }

    @Override
    public AstralConditionFactory<Entity> factory() {
        final SerializableData data = new SerializableData().add("phase", AstralData.MOON_PHASE, MoonPhase.NONE)
            .add("phases", AstralData.MOON_PHASES, new ArrayList<>());

        return new AstralConditionFactory<>(this.getRegistryPath(), data, this::test);
    }

    @Override
    public Registry<ConditionFactory<Entity>> registry() {
        return ApoliRegistries.ENTITY_CONDITION;
    }

}
