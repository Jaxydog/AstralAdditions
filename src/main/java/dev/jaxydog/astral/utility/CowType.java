/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2024 Icepenguin
 *
 * This file is part of Astral.
 *
 * Astral is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Astral is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with Astral. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jaxydog.astral.utility;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.StringIdentifiable;

/**
 * Determines a cow's texture variant.
 *
 * @author Icepenguin
 * @since 1.7.0
 */
public enum CowType implements StringIdentifiable {

    /**
     * A normal, brown cow.
     *
     * @since 1.7.0
     */
    BROWN("brown"),

    /**
     * A pink cow.
     *
     * @since 1.7.0
     */
    PINK("pink");

    /**
     * A codec that allows for easy string lookups.
     *
     * @since 1.7.0
     */
    @SuppressWarnings("deprecation")
    public static final Codec<CowType> CODEC = StringIdentifiable.createCodec(CowType::values);
    /**
     * A data tracker entry to be used within {@link dev.jaxydog.astral.mixin.CowEntityMixin}.
     *
     * @since 1.7.0
     */
    public static final TrackedData<String> COW_TYPE = DataTracker.registerData(CowEntity.class,
        TrackedDataHandlerRegistry.STRING
    );

    /**
     * The name of the variant.
     *
     * @since 1.7.0
     */
    private final String name;

    /**
     * Creates a new cow type.
     *
     * @param name The name of the variant.
     *
     * @since 1.7.0
     */
    CowType(String name) {
        this.name = name;
    }

    /**
     * Returns the variant associated with the given name, or {@link #BROWN} if the given name is invalid.
     *
     * @param name The name of the variant.
     *
     * @return The variant associated with the given name.
     *
     * @since 1.7.0
     */
    public static CowType fromName(String name) {
        return CODEC.byId(name, BROWN);
    }

    @Override
    public String asString() {
        return this.name;
    }

}
