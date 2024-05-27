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

package dev.jaxydog.astral.content.item.custom;

import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.block.AstralBlocks;
import dev.jaxydog.astral.content.block.custom.DyedAmethystBlock;
import dev.jaxydog.astral.content.item.AstralItem;
import dev.jaxydog.astral.datagen.*;
import dev.jaxydog.astral.register.Registered.Generated;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Defines dyed amethyst clusters.
 *
 * @author Jaxydog
 * @since 2.1.0
 */
public class DyedAmethystShardItem extends AstralItem implements Generated {

    /**
     * An item tag containing all amethyst shards.
     *
     * @since 2.1.0
     */
    public static final TagKey<Item> AMETHYST_SHARDS = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("amethyst_shards")
    );

    /**
     * This item's dye color.
     *
     * @since 2.1.0
     */
    private final DyeColor color;

    /**
     * Creates a new item using the given settings.
     * <p>
     * If the {@code preferredGroup} supplier is {@code null}, this item will not be added to any item groups.
     *
     * @param path The item's identifier path.
     * @param settings The item's settings.
     * @param color The item's dye color.
     * @param preferredGroup The item's preferred item group.
     *
     * @since 2.1.0
     */
    public DyedAmethystShardItem(
        String path, Settings settings, DyeColor color, @Nullable Supplier<RegistryKey<ItemGroup>> preferredGroup
    ) {
        super(path, settings, preferredGroup);

        this.color = color;
    }

    /**
     * Creates a new item using the given settings.
     * <p>
     * This item will be added to the default item group.
     *
     * @param path The item's identifier path.
     * @param settings The item's settings.
     * @param color The item's dye color.
     *
     * @since 2.1.0
     */
    public DyedAmethystShardItem(String path, Settings settings, DyeColor color) {
        super(path, settings);

        this.color = color;
    }

    /**
     * Returns this item's dye color.
     *
     * @return The associated color.
     *
     * @since 2.1.0
     */
    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public void generate() {
        ModelGenerator.getInstance().generateItem(g -> g.register(this, Models.GENERATED));

        TagGenerator.getInstance().generate(AMETHYST_SHARDS, g -> g.add(this));

        TextureGenerator.getInstance().generate(Registries.ITEM.getKey(), instance -> {
            final Optional<BufferedImage> maybeSource = instance.getImage("amethyst_shard");

            if (maybeSource.isPresent()) {
                final BufferedImage image = DyedAmethystBlock.DYE_MAPPER.convert(maybeSource.get(), this.getColor());

                instance.generate(this.getRegistryId(), image);
            }
        });

        final Block block = AstralBlocks.DYED_AMETHYST_BLOCKS.getComputed(this.getColor());

        RecipeGenerator.getInstance().generate(Astral.getId(this.getRegistryPath() + "s_to_block"),
            ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, block)
                .group("dyed_amethyst_shards_to_blocks")
                .input(this, 4)
                .criterion("item", FabricRecipeProvider.conditionsFromTag(AMETHYST_SHARDS))
        );

        RecipeGenerator.getInstance().generate(this.getRegistryId(),
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, this)
                .group("dyed_amethyst_shards")
                .input(AMETHYST_SHARDS)
                .input(DyeItem.byColor(this.getColor()))
                .criterion("item", FabricRecipeProvider.conditionsFromTag(AMETHYST_SHARDS))
        );

        LanguageGenerator.getInstance().generate(builder -> {
            final String[] parts = this.getColor().getName().split("_");
            final String value = Arrays.stream(parts)
                .map(s -> StringUtils.capitalize(s) + " ")
                .reduce(String::concat)
                .orElse("Dyed ");

            builder.add(this, value + "Amethyst Shard");
        });
    }

}
