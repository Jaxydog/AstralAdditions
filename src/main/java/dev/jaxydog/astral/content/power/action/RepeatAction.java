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

package dev.jaxydog.astral.content.power.action;

import dev.jaxydog.astral.content.power.AstralActionFactory;
import dev.jaxydog.astral.content.power.AstralMetaAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;

/**
 * The repeat meta-action.
 *
 * @author Jaxydog
 * @since 1.1.0
 */
public class RepeatAction extends AstralMetaAction {

    /**
     * Creates a new repeat action.
     *
     * @param path The action's identifier path.
     *
     * @since 1.1.0
     */
    public RepeatAction(String path) {
        super(path);
    }

    @Override
    public <T> void execute(Instance data, T t) {
        final ActionFactory<T>.Instance action = data.get("action");
        final int repeat = data.getInt("repeat");

        for (int i = 0; i < repeat; i += 1) action.accept(t);
    }

    @Override
    public <T> AstralActionFactory<T> factory(SerializableDataType<ActionFactory<T>.Instance> type) {
        final SerializableData data = new SerializableData().add("repeat", SerializableDataTypes.INT)
            .add("action", type);

        return new AstralActionFactory<>(this.getRegistryPath(), data, this::execute);
    }

}
