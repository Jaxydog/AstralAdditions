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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.item.AstralBlockItem;
import dev.jaxydog.astral.datagen.*;
import dev.jaxydog.astral.register.Registered.Client;
import dev.jaxydog.astral.utility.injected.AstralModel;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Defines dyed amethyst cluster blocks.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
@SuppressWarnings("deprecation")
public class DyedAmethystClusterBlock extends DyedAmethystBlock implements Client {

    /**
     * A block tag containing all amethyst clusters.
     *
     * @since 2.0.0
     */
    public static final TagKey<Block> AMETHYST_CLUSTERS = TagKey.of(Registries.BLOCK.getKey(),
        Astral.getId("amethyst_clusters")
    );
    /**
     * A block tag containing all large amethyst buds.
     *
     * @since 2.0.0
     */
    public static final TagKey<Block> LARGE_AMETHYST_BUDS = TagKey.of(Registries.BLOCK.getKey(),
        Astral.getId("large_amethyst_buds")
    );
    /**
     * A block tag containing all medium amethyst buds.
     *
     * @since 2.0.0
     */
    public static final TagKey<Block> MEDIUM_AMETHYST_BUDS = TagKey.of(Registries.BLOCK.getKey(),
        Astral.getId("medium_amethyst_buds")
    );
    /**
     * A block tag containing all small amethyst buds.
     *
     * @since 2.0.0
     */
    public static final TagKey<Block> SMALL_AMETHYST_BUDS = TagKey.of(Registries.BLOCK.getKey(),
        Astral.getId("small_amethyst_buds")
    );

    /**
     * An item tag containing all amethyst clusters.
     *
     * @since 2.0.0
     */
    public static final TagKey<Item> AMETHYST_CLUSTER_ITEMS = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("amethyst_clusters")
    );
    /**
     * An item tag containing all large amethyst buds.
     *
     * @since 2.0.0
     */
    public static final TagKey<Item> LARGE_AMETHYST_BUD_ITEMS = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("large_amethyst_buds")
    );
    /**
     * An item tag containing all medium amethyst buds.
     *
     * @since 2.0.0
     */
    public static final TagKey<Item> MEDIUM_AMETHYST_BUD_ITEMS = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("medium_amethyst_buds")
    );
    /**
     * An item tag containing all small amethyst buds.
     *
     * @since 2.0.0
     */
    public static final TagKey<Item> SMALL_AMETHYST_BUD_ITEMS = TagKey.of(Registries.ITEM.getKey(),
        Astral.getId("small_amethyst_buds")
    );

    /**
     * Controls whether the block is waterlogged.
     *
     * @since 2.0.0
     */
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    /**
     * Controls the direction the block is facing.
     *
     * @since 2.0.0
     */
    public static final DirectionProperty FACING = Properties.FACING;

    /**
     * The amethyst cluster type assigned to this instance.
     *
     * @since 2.0.0
     */
    private final Type type;

    /**
     * Creates a new block using the given settings.
     *
     * @param path The block's identifier path.
     * @param settings The block's settings.
     * @param color The block's color.
     *
     * @since 2.0.0
     */
    public DyedAmethystClusterBlock(String path, Settings settings, DyeColor color, Type type) {
        super(path, settings, color);

        this.type = type;
    }

    /**
     * Returns the item's display model.
     *
     * @return The item model.
     *
     * @since 2.0.0
     */
    @SuppressWarnings("RedundantCast")
    private static Model getModel() {
        final Model model = ((AstralModel) Models.GENERATED).astral$copy();
        final JsonObject display = new JsonObject();
        final JsonObject head = new JsonObject();
        final JsonArray translation = new JsonArray(3);

        translation.add(0);
        translation.add(14);
        translation.add(-5);
        head.add("translation", translation);
        display.add("head", head);

        ((AstralModel) model).astral$addCustomJson("display", display);

        return model;
    }

    /**
     * Returns the amethyst cluster type of this instance.
     *
     * @return The cluster type.
     *
     * @since 2.0.0
     */
    public Type getType() {
        return type;
    }

    @Override
    public BlockState getStateForNeighborUpdate(
        BlockState state,
        Direction direction,
        BlockState neighborState,
        WorldAccess world,
        BlockPos pos,
        BlockPos neighborPos
    ) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction == state.get(FACING).getOpposite() && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return state;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        } else {
            return Fluids.EMPTY.getDefaultState();
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        final Direction direction = state.get(FACING);
        final BlockPos blockPos = pos.offset(direction.getOpposite());

        return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getType().getShape(state.get(FACING));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final World world = ctx.getWorld();
        final BlockPos blockPos = ctx.getBlockPos();
        final boolean waterlogged = world.getFluidState(blockPos).getFluid() == Fluids.WATER;

        return this.getDefaultState().with(WATERLOGGED, waterlogged).with(FACING, ctx.getSide());
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    @Override
    public void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(this, RenderLayer.getCutout());
    }

    @Override
    public void generate() {
        ModelGenerator.getInstance().generateBlock(g -> g.registerAmethyst(this));
        ModelGenerator.getInstance().generateItem(g -> {
            final Identifier identifier = ModelIds.getItemModelId(this.asItem());

            getModel().upload(identifier, TextureMap.layer0(this), g.writer);
        });

        TagGenerator.getInstance().generate(this.getType().getBlockTagKey(), g -> g.add(this));
        TagGenerator.getInstance().generate(this.getType().getItemTagKey(), g -> g.add(this.asItem()));
        TagGenerator.getInstance().generate(BlockTags.PICKAXE_MINEABLE, g -> g.add(this));

        TextureGenerator.getInstance().generate(Registries.BLOCK.getKey(), instance -> {
            final Optional<BufferedImage> maybeSource = instance.getImage(this.getType().getBasePath());

            if (maybeSource.isPresent()) {
                final BufferedImage image = DYE_MAPPER.convert(maybeSource.get(), this.getColor());

                instance.generate(this.getRegistryId(), image);
            }
        });

        LootTableGenerator.getInstance().generate(LootContextTypes.BLOCK,
            this.getLootTableId(),
            new LootTable.Builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1F))
                .with(ItemEntry.builder(this))
                .conditionally(SurvivesExplosionLootCondition.builder().build())
                .build())
        );

        RecipeGenerator.getInstance().generate(((AstralBlockItem) this.asItem()).getRegistryId(),
            ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this)
                .group("dyeable_%ss".formatted(this.getType().getBasePath()))
                .input(this.getType().getItemTagKey())
                .input(DyeItem.byColor(this.getColor()))
                .criterion("block", FabricRecipeProvider.conditionsFromTag(this.getType().getItemTagKey()))
        );

        LanguageGenerator.getInstance().generate(builder -> {
            final String[] parts = this.getColor().getName().split("_");
            final String value = Arrays.stream(parts)
                .map(StringUtils::capitalize)
                .reduce(String::concat)
                .orElse("Dyed");

            builder.add(this, "%s %s".formatted(value, switch (this.getType()) {
                case CLUSTER -> "Amethyst Cluster";
                case LARGE_BUD -> "Large Amethyst Bud";
                case MEDIUM_BUD -> "Medium Amethyst Bud";
                case SMALL_BUD -> "Small Amethyst Bud";
            }));
        });
    }

    /**
     * A variant of an amethyst cluster block.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    public enum Type {

        /**
         * Amethyst clusters.
         *
         * @since 2.0.0
         */
        CLUSTER("amethyst_cluster", 3, 7, AMETHYST_CLUSTERS, AMETHYST_CLUSTER_ITEMS),
        /**
         * Large amethyst buds.
         *
         * @since 2.0.0
         */
        LARGE_BUD("amethyst_cluster", 3, 7, LARGE_AMETHYST_BUDS, LARGE_AMETHYST_BUD_ITEMS),
        /**
         * Medium amethyst buds.
         *
         * @since 2.0.0
         */
        MEDIUM_BUD("amethyst_cluster", 3, 7, MEDIUM_AMETHYST_BUDS, MEDIUM_AMETHYST_BUD_ITEMS),
        /**
         * Small amethyst buds.
         *
         * @since 2.0.0
         */
        SMALL_BUD("amethyst_cluster", 3, 7, SMALL_AMETHYST_BUDS, SMALL_AMETHYST_BUD_ITEMS);

        /**
         * The base identifier path.
         *
         * @since 2.0.0
         */
        private final String basePath;
        /**
         * The "size" of the amethyst cluster.
         *
         * @since 2.0.0
         */
        private final int size;
        /**
         * The height of the amethyst cluster.
         *
         * @since 2.0.0
         */
        private final int height;
        /**
         * The block tag containing this category of cluster.
         *
         * @since 2.0.0
         */
        private final TagKey<Block> blockTagKey;
        /**
         * The item tag containing this category of cluster.
         *
         * @since 2.0.0
         */
        private final TagKey<Item> itemTagKey;

        /**
         * Maps different directions to their shapes.
         *
         * @since 2.0.0
         */
        private final Map<Direction, VoxelShape> shapes = new Object2ObjectArrayMap<>(Direction.values().length);

        /**
         * Creates a new amethyst cluster variant.
         *
         * @param basePath The base identifier path.
         * @param size The size of the cluster.
         * @param height The height of the cluster.
         * @param blockTagKey The block tag containing blocks of this type.
         * @param itemTagKey The item tag containing items of this type.
         */
        @SuppressWarnings("SuspiciousNameCombination")
        Type(String basePath, int size, int height, TagKey<Block> blockTagKey, TagKey<Item> itemTagKey) {
            this.basePath = basePath;
            this.size = size;
            this.height = height;
            this.blockTagKey = blockTagKey;
            this.itemTagKey = itemTagKey;

            final double sizeI = 16D - this.getSize();
            final double heightI = 16D - this.getHeight();

            // Determines the hit-boxes of a block of this type. This is ~~mostly~~ copied from the base cluster class.
            this.shapes.put(Direction.UP, Block.createCuboidShape(size, 0D, size, sizeI, height, sizeI));
            this.shapes.put(Direction.DOWN, Block.createCuboidShape(size, size, heightI, sizeI, sizeI, 16D));
            this.shapes.put(Direction.NORTH, Block.createCuboidShape(size, size, heightI, sizeI, sizeI, 16D));
            this.shapes.put(Direction.SOUTH, Block.createCuboidShape(size, size, 0D, sizeI, sizeI, height));
            this.shapes.put(Direction.EAST, Block.createCuboidShape(0D, size, size, height, sizeI, sizeI));
            this.shapes.put(Direction.WEST, Block.createCuboidShape(heightI, size, size, 16D, sizeI, sizeI));
        }

        /**
         * Returns this type's base identifier path.
         *
         * @return The identifier path.
         *
         * @since 2.0.0
         */
        public String getBasePath() {
            return this.basePath;
        }

        /**
         * Returns this type's cluster size.
         *
         * @return The cluster size.
         *
         * @since 2.0.0
         */
        public int getSize() {
            return this.size;
        }

        /**
         * Returns this type's cluster height.
         *
         * @return The cluster height.
         *
         * @since 2.0.0
         */
        public int getHeight() {
            return this.height;
        }

        /**
         * Returns this type's block tag key.
         *
         * @return The block tag key.
         *
         * @since 2.0.0
         */
        public TagKey<Block> getBlockTagKey() {
            return this.blockTagKey;
        }

        /**
         * Returns this type's item tag key.
         *
         * @return The item tag key.
         *
         * @since 2.0.0
         */
        public TagKey<Item> getItemTagKey() {
            return this.itemTagKey;
        }

        /**
         * Returns the block's shape in the current direction.
         *
         * @param direction The direction that the block is facing.
         *
         * @return The shape of the block.
         *
         * @since 2.0.0
         */
        public VoxelShape getShape(Direction direction) {
            return this.shapes.get(direction);
        }
    }

}
