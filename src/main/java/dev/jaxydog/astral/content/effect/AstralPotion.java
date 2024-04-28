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

package dev.jaxydog.astral.content.effect;

import dev.jaxydog.astral.register.Registered.Common;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.Nullable;

/**
 * An extension of a {@link Potion} that provides commonly used functionality.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralPotion extends Potion implements Common {

    /**
     * The potion's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;
    /**
     * The potion's recipe, if one exists.
     *
     * @since 2.0.0
     */
    private final @Nullable Recipe recipe;

    /**
     * Creates a new potion.
     *
     * @param path The potion's identifier path.
     * @param recipe The potion's recipe.
     * @param effects The potion's status effects.
     *
     * @since 2.0.0
     */
    public AstralPotion(String path, @Nullable Recipe recipe, StatusEffectInstance... effects) {
        super(effects);

        this.path = path;
        this.recipe = recipe;
    }

    /**
     * Creates a new potion.
     *
     * @param path The potion's identifier path.
     * @param effects The potion's status effects.
     *
     * @since 2.0.0
     */
    public AstralPotion(String path, StatusEffectInstance... effects) {
        this(path, null, effects);
    }

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        Registry.register(Registries.POTION, this.getRegistryId(), this);

        if (this.recipe != null) {
            FabricBrewingRecipeRegistry.registerPotionRecipe(this.recipe.base(), this.recipe.item(), this);
        }
    }

    /**
     * Defines the data for a potion recipe.
     *
     * @param base The potion that is added on to.
     * @param item The item combined with the base potion.
     *
     * @author Jaxydog
     * @see AstralPotion
     * @since 2.0.0
     */
    public record Recipe(Potion base, Ingredient item) { }

}
