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

package dev.jaxydog.astral.content.data.custom;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.data.AstralJsonDataLoader;
import dev.jaxydog.astral.utility.CurrencyHelper.ItemRepresentable;
import dev.jaxydog.astral.utility.CurrencyHelper.Reward;
import dev.jaxydog.astral.utility.CurrencyHelper.Skeleton;
import dev.jaxydog.astral.utility.CurrencyHelper.Unit;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The currency data loader.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class CurrencyLoader extends AstralJsonDataLoader {

    /**
     * Creates a new JSON data loader.
     *
     * @param gson The GSON instance.
     * @param folder The folder name.
     *
     * @since 2.0.0
     */
    public CurrencyLoader(Gson gson, String folder) {
        super(gson, folder);
    }

    /**
     * Creates a new JSON data loader.
     *
     * @param folder The folder name.
     *
     * @since 2.0.0
     */
    public CurrencyLoader(String folder) {
        super(folder);
    }

    /**
     * Parses and loads currency data.
     *
     * @param object The JSON source object.
     * @param parse The function used to parse and construct an instance of type {@code T}.
     * @param loader Loads a completed list of constructed types, returning the number of values added.
     * @param descriptor A string that describes the data being loaded for logging purposes.
     * @param <T> The type being loaded.
     *
     * @since 2.0.0
     */
    private <T extends ItemRepresentable> void load(
        JsonObject object,
        BiFunction<Identifier, JsonObject, T> parse,
        Function<Map<Identifier, T>, Integer> loader,
        String descriptor
    ) {
        final Map<Identifier, T> output = new Object2ObjectOpenHashMap<>(object.size());

        object.asMap().forEach((key, value) -> {
            final Identifier valueId = Identifier.tryParse(key);

            if (Objects.isNull(valueId)) {
                Astral.LOGGER.warn("Invalid identifier key '{}'", key);

                return;
            } else if (output.containsKey(valueId)) {
                Astral.LOGGER.warn("Duplicate identifier key '{}'", key);

                return;
            }

            try {
                final JsonObject data = JsonHelper.asObject(value, "data");
                final String itemIdString = JsonHelper.getString(data, "item");
                final Identifier itemId = Identifier.tryParse(itemIdString);

                if (Objects.isNull(itemId)) {
                    throw new JsonSyntaxException("Invalid identifier '%s'".formatted(itemIdString));
                }

                output.put(valueId, parse.apply(itemId, data));
            } catch (JsonSyntaxException exception) {
                Astral.LOGGER.warn(exception.getLocalizedMessage());
            }
        });

        Astral.LOGGER.info("Loaded {} currency {}", loader.apply(output), descriptor);
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        prepared.entrySet().stream().filter(entry -> entry.getValue() instanceof JsonObject).forEach(entry -> {
            final JsonObject object = JsonHelper.asObject(entry.getValue(), "data");
            final String namespace = entry.getKey().getNamespace();
            final String path = entry.getKey().getPath();
            final String type = path.replaceFirst("\\.json$", "");

            switch (type) {
                case "units" -> this.load(object, (i, o) -> Unit.parse(namespace, i, o), Unit.UNITS::load, type);
                case "rewards" -> this.load(object, Reward::parse, Reward.REWARDS::load, type);
                case "skeletons" -> this.load(object, Skeleton::parse, Skeleton.SKELETONS::load, type);
            }
        });
    }

}
