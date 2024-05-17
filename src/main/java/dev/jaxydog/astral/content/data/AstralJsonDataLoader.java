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

package dev.jaxydog.astral.content.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.jaxydog.astral.register.Registered.Common;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

/**
 * An extension of a {@link JsonDataLoader} that provides additional functionality.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public abstract class AstralJsonDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener, Common {

    /**
     * The default GSON instance.
     *
     * @since 2.0.0
     */
    private static final Gson DEFAULT_GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    /**
     * The data loader's folder and identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;

    /**
     * Creates a new JSON data loader.
     *
     * @param gson The GSON instance.
     * @param folder The folder name.
     *
     * @since 2.0.0
     */
    public AstralJsonDataLoader(Gson gson, String folder) {
        super(gson, folder);

        this.path = folder;
    }

    /**
     * Creates a new JSON data loader.
     *
     * @param folder The folder name.
     *
     * @since 2.0.0
     */
    public AstralJsonDataLoader(String folder) {
        this(DEFAULT_GSON, folder);
    }

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public Identifier getFabricId() {
        return this.getRegistryId();
    }

    @Override
    public void registerCommon() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this);
    }

}
