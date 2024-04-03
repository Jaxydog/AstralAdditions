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

package dev.jaxydog.astral.content.block.custom;

import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.block.AstralBlock;
import dev.jaxydog.astral.content.item.AstralBlockItem;
import dev.jaxydog.astral.content.sound.SoundContext;
import dev.jaxydog.astral.datagen.*;
import dev.jaxydog.astral.register.Registered.Generated;
import dev.jaxydog.astral.utility.color.ImageBiMapper;
import dev.jaxydog.astral.utility.color.Rgba;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Arrays;
import java.util.Optional;

/**
 * Defines dyed amethyst blocks.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class DyedAmethystBlock extends AstralBlock implements Generated {

    /**
     * A block tag containing all amethyst blocks.
     *
     * @since 2.0.0
     */
    public static final TagKey<Block> AMETHYST_BLOCKS = TagKey.of(Registries.BLOCK.getKey(),
        Astral.getId("amethyst_blocks")
    );
    /**
     * An item tag containing all amethyst blocks.
     *
     * @since 2.0.0
     */
    public static final TagKey<Item> AMETHYST_BLOCK_ITEMS = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("amethyst_blocks")
    );

    /**
     * The amethyst block's chime sound.
     *
     * @since 2.0.0
     */
    public static final SoundContext BLOCK_CHIME_SOUND = new SoundContext(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
        SoundCategory.BLOCKS,
        1F,
        1.1F,
        0.6F
    );
    /**
     * The amethyst block's hit sound.
     *
     * @since 2.0.0
     */
    public static final SoundContext BLOCK_HIT_SOUND = new SoundContext(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT,
        SoundCategory.BLOCKS,
        1F,
        1.1F,
        0.6F
    );

    /**
     * The color mapper used within the mod's data generator.
     *
     * @since 2.0.0
     */
    public static final ImageBiMapper<DyeColor> DYE_MAPPER = new ImageBiMapper<>(
        (color, dye) -> {
            final float saturation = color.saturation();
            final float brightness = color.brightness();

            return (switch (dye) {
                // Grayscale colors get special treatment.
                case WHITE -> color.withSaturation(0F);
                case LIGHT_GRAY -> color.withSaturation(0F).withBrightness(brightness - 0.225F);
                case GRAY -> color.withSaturation(0F).withBrightness(brightness - 0.45F);
                case BLACK -> color.withSaturation(0F).withBrightness(brightness - 0.6F);
                // All other colors just hue rotate.
                default -> {
                    final float[] components = dye.getColorComponents();
                    final Rgba target = new Rgba(components[0], components[1], components[2], color.alphaScaled());
                    final Rgba rotated = color.withHue(target.hue());

                    // Fine-tune some of the more problematic colors.
                    yield switch (dye) {
                        // Small tweaks to make the colors more distinct and visually appealing.
                        case BROWN -> rotated.withBrightness(brightness - 0.25F);
                        case GREEN -> rotated.withSaturation(saturation + 0.375F).withBrightness(brightness - 0.325F);
                        case PINK -> rotated.withSaturation(saturation - 0.1F).withBrightness(brightness + 0.1F);
                        case CYAN -> rotated.withSaturation(saturation + 0.25F).withBrightness(brightness - 0.25F);
                        // All other colors get a slight saturation bump.
                        default -> rotated.withSaturation(saturation + 0.25F);
                    };
                }
            });
        },
        // Some colors look wrong without this filter.
        (image, dye) -> {
            final float percentage = switch (dye) {
                case GRAY -> 0.625F;
                case BLACK -> 0.5F;
                case GREEN -> 0.875F;
                default -> 1F;
            };

            if (percentage != 1F) {
                new RescaleOp(percentage, 0xF, null).filter(image, image);
            }
        }
    );

    /**
     * This block's dye color.
     *
     * @since 2.0.0
     */
    private final DyeColor color;

    /**
     * Creates a new block using the given settings.
     *
     * @param path The block's identifier path.
     * @param settings The block's settings.
     * @param color The block's color.
     *
     * @since 2.0.0
     */
    public DyedAmethystBlock(String path, Settings settings, DyeColor color) {
        super(path, settings);

        this.color = color;
    }

    /**
     * Returns this block's dye color.
     *
     * @return The associated color.
     *
     * @since 2.0.0
     */
    public DyeColor getColor() {
        return this.color;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (world.isClient()) return;

        final BlockPos pos = hit.getBlockPos();

        BLOCK_HIT_SOUND.play(world, pos);
        BLOCK_CHIME_SOUND.play(world, pos);
    }

    @Override
    public void generate() {
        ModelGenerator.getInstance().generateBlock(g -> g.registerSimpleCubeAll(this));

        TagGenerator.getInstance().generate(AMETHYST_BLOCKS, g -> g.add(this));
        TagGenerator.getInstance().generate(AMETHYST_BLOCK_ITEMS, g -> g.add(this.asItem()));
        TagGenerator.getInstance().generate(BlockTags.PICKAXE_MINEABLE, g -> g.add(this));

        TextureGenerator.getInstance().generate(Registries.BLOCK.getKey(), instance -> {
            final Optional<BufferedImage> maybeSource = instance.getImage("amethyst_block");

            if (maybeSource.isPresent()) {
                final BufferedImage image = DYE_MAPPER.convert(maybeSource.get(), this.getColor());

                instance.generate(this.getRegistryId(), image);
            }
        });

        LootTableGenerator.getInstance().generate(LootContextTypes.BLOCK,
            this.getLootTableId(),
            new Builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1F))
                .with(ItemEntry.builder(this))
                .conditionally(SurvivesExplosionLootCondition.builder().build())
                .build())
        );

        RecipeGenerator.getInstance().generate(((AstralBlockItem) this.asItem()).getRegistryId(),
            ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this)
                .group("dyeable_amethyst_blocks")
                .input(AMETHYST_BLOCK_ITEMS)
                .input(DyeItem.byColor(this.getColor()))
                .criterion("block", FabricRecipeProvider.conditionsFromTag(AMETHYST_BLOCK_ITEMS))
        );

        LanguageGenerator.getInstance().generate(builder -> {
            final String[] parts = this.getColor().getName().split("_");
            final String value = Arrays.stream(parts)
                .map(StringUtils::capitalize)
                .reduce(String::concat)
                .orElse("Dyed");

            builder.add(this, value + " Amethyst Block");
        });
    }

}
