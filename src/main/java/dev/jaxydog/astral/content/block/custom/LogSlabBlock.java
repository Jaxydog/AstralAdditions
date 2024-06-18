/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2024 IcePenguin
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
import dev.jaxydog.astral.content.block.AstralSlabBlock;
import dev.jaxydog.astral.datagen.*;
import dev.jaxydog.astral.register.Registered.Generated;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.client.*;
import net.minecraft.data.client.VariantSettings.Rotation;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

/**
 * The log slab block.
 *
 * @author IcePenguin
 * @since 2.2.0
 */
@SuppressWarnings("deprecation")
public class LogSlabBlock extends AstralSlabBlock implements Generated {

    /**
     * The block's axis property.
     *
     * @since 2.2.0
     */
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;

    /**
     * The slab block's base block.
     *
     * @since 2.2.0
     */
    private final Block base;

    /**
     * Creates a new log slab block using given settings.
     *
     * @param path The slab block's identifier path.
     * @param base The slab block's base block.
     * @param settings the slab block's settings.
     *
     * @since 2.2.0
     */
    public LogSlabBlock(String path, Block base, Settings settings) {
        super(path, settings);

        this.base = base;

        this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y));
    }

    /**
     * Returns this slab block's base block.
     *
     * @return This slab block's base block.
     *
     * @since 2.2.0
     */
    public Block getBaseBlock() {
        return this.base;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);

        builder.add(AXIS);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = super.getPlacementState(ctx);

        return state == null ? null : state.with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.get(AXIS)) {
                case X -> state.with(AXIS, Direction.Axis.Z);
                case Z -> state.with(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    public void generate() {
        ModelGenerator.getInstance().generateItem(g -> {
            final Identifier identifier = ModelIds.getItemModelId(this.asItem());
            final Identifier modelIdentifier = ModelIds.getBlockModelId(this).withSuffixedPath("_horizontal");

            new Model(Optional.of(modelIdentifier), Optional.empty()).upload(identifier, new TextureMap(), g.writer);
        });
        ModelGenerator.getInstance().generateBlock(g -> {
            final Identifier side = ModelIds.getBlockModelId(this.getBaseBlock());
            final Identifier top = side.withSuffixedPath("_top");
            final Identifier inner = Astral.getId(side.getPath() + "_inner");
            final Identifier identifier = ModelIds.getBlockModelId(this);

            VERTICAL_MODEL.upload(
                identifier.withSuffixedPath("_vertical"),
                new TextureMap().put(TextureKey.TOP, top)
                    .put(TextureKey.BOTTOM, top)
                    .put(SIDE_0, side)
                    .put(SIDE_1, side),
                g.modelCollector
            );
            VERTICAL_MODEL_TOP.upload(
                identifier.withSuffixedPath("_vertical_top"),
                new TextureMap().put(TextureKey.TOP, top)
                    .put(TextureKey.BOTTOM, top)
                    .put(SIDE_0, side)
                    .put(SIDE_1, side),
                g.modelCollector
            );
            HORIZONTAL_MODEL.upload(
                identifier.withSuffixedPath("_horizontal"),
                new TextureMap().put(TextureKey.TOP, inner)
                    .put(TextureKey.BOTTOM, side)
                    .put(SIDE_0, top)
                    .put(SIDE_1, side),
                g.modelCollector
            );
            HORIZONTAL_MODEL_TOP.upload(
                identifier.withSuffixedPath("_horizontal_top"),
                new TextureMap().put(TextureKey.TOP, side)
                    .put(TextureKey.BOTTOM, inner)
                    .put(SIDE_0, top)
                    .put(SIDE_1, side),
                g.modelCollector
            );

            g.blockStateCollector.accept(VariantsBlockStateSupplier.create(this, BlockStateVariant.create())
                .coordinate(BlockStateVariantMap.create(Properties.AXIS, Properties.SLAB_TYPE)
                    .register((axis, type) -> {
                        final BlockStateVariant variant = BlockStateVariant.create();

                        if (type == SlabType.DOUBLE) {
                            return switch (axis) {
                                case Y ->
                                    variant.put(VariantSettings.MODEL, ModelIds.getBlockModelId(this.getBaseBlock()));
                                case Z ->
                                    variant.put(VariantSettings.MODEL, ModelIds.getBlockModelId(this.getBaseBlock()))
                                        .put(VariantSettings.X, Rotation.R90);
                                case X ->
                                    variant.put(VariantSettings.MODEL, ModelIds.getBlockModelId(this.getBaseBlock()))
                                        .put(VariantSettings.X, Rotation.R90)
                                        .put(VariantSettings.Y, Rotation.R90);
                            };
                        }

                        final Identifier topId, sideId;

                        switch (type) {
                            case BOTTOM -> {
                                topId = ModelIds.getBlockModelId(this).withSuffixedPath("_vertical");
                                sideId = ModelIds.getBlockModelId(this).withSuffixedPath("_horizontal");
                            }
                            case TOP -> {
                                topId = ModelIds.getBlockModelId(this).withSuffixedPath("_vertical_top");
                                sideId = ModelIds.getBlockModelId(this).withSuffixedPath("_horizontal_top");
                            }
                            default -> throw new AssertionError();
                        }

                        return switch (axis) {
                            case Y -> variant.put(VariantSettings.MODEL, topId);
                            case X -> variant.put(VariantSettings.MODEL, sideId).put(VariantSettings.Y, Rotation.R90);
                            case Z -> variant.put(VariantSettings.MODEL, sideId);
                        };
                    })));
        });

        TagGenerator.getInstance().generate(BlockTags.AXE_MINEABLE, g -> g.add(this));
        TagGenerator.getInstance().generate(BlockTags.SLABS, g -> g.add(this));

        LootTableGenerator.getInstance().generate(
            LootContextTypes.BLOCK,
            this.getLootTableId(),
            new Builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1F))
                .with(ItemEntry.builder(this))
                .conditionally(SurvivesExplosionLootCondition.builder().build())
                .build())
        );

        RecipeGenerator.getInstance().generate(
            this.getRegistryId(),
            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this, 6)
                .pattern("XXX")
                .input('X', this.getBaseBlock())
                .criterion("block", FabricRecipeProvider.conditionsFromItem(this.getBaseBlock()))
        );

        LanguageGenerator.getInstance().generate(builder -> {
            final String[] parts = this.getRegistryPath().split("_");
            final String value = Arrays.stream(parts)
                .map(s -> StringUtils.capitalize(s) + " ")
                .reduce(String::concat)
                .orElse("Log Slab");

            builder.add(this, value.trim());
        });
    }

}
