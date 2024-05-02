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
import dev.jaxydog.astral.register.Registered.Common;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An extension of a {@link PowerFactory} that provides additional functionality.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralPowerFactory<P extends Power> extends PowerFactory<P> implements Common {

    /**
     * Creates a new power factory.
     *
     * @param path The factory's identifier path.
     * @param data The factory's serialization data.
     * @param constructor The factory's power constructor.
     *
     * @since 2.0.0
     */
    public AstralPowerFactory(
        String path,
        SerializableData data,
        Function<SerializableData.Instance, BiFunction<PowerType<P>, LivingEntity, P>> constructor
    ) {
        super(Astral.getId(path), data, constructor);
    }

    @Override
    public void registerCommon() {
        Registry.register(ApoliRegistries.POWER_FACTORY, this.getRegistryId(), this);
    }

    @Override
    public String getRegistryPath() {
        return this.getSerializerId().getPath();
    }

    @Override
    public Identifier getRegistryId() {
        return this.getSerializerId();
    }

    @Override
    public AstralPowerFactory<P> allowCondition() {
        return (AstralPowerFactory<P>) super.allowCondition();
    }

}
