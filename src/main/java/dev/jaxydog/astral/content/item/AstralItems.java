/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2023–2024 Jaxydog
 * Copyright © 2024 IcePenguin
 * Copyright © 2024 FunsulYT
 *
 * This file is part of Astral.
 *
 * Astral is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Astral is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with Astral. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jaxydog.astral.content.item;

import dev.jaxydog.astral.content.block.AstralBlocks;
import dev.jaxydog.astral.content.block.custom.DyedAmethystBlock;
import dev.jaxydog.astral.content.block.custom.DyedAmethystClusterBlock;
import dev.jaxydog.astral.content.block.custom.DyedBuddingAmethystBlock;
import dev.jaxydog.astral.content.effect.AstralStatusEffects;
import dev.jaxydog.astral.content.item.AstralArmorItem.Material;
import dev.jaxydog.astral.content.item.custom.*;
import dev.jaxydog.astral.content.item.group.AstralItemGroups;
import dev.jaxydog.astral.datagen.TagGenerator;
import dev.jaxydog.astral.register.ArmorMap;
import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.register.DyeMap;
import dev.jaxydog.astral.register.RegistrationPriority;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;

/**
 * Contains definitions of all modded-in items.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public final class AstralItems extends ContentRegistrar {

    /**
     * The acacia log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem ACACIA_LOG_SLAB = new AstralBlockItem("acacia_log_slab",
        AstralBlocks.ACACIA_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The bamboo block slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem BAMBOO_BLOCK_SLAB = new AstralBlockItem("bamboo_block_slab",
        AstralBlocks.BAMBOO_BLOCK_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The birch log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem BIRCH_LOG_SLAB = new AstralBlockItem("birch_log_slab",
        AstralBlocks.BIRCH_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The blob of malintent effect item.
     * <p>
     * Randomly provides the holder with the sinister effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem BLOB_OF_MALINTENT = new RandomEffectItem("blob_of_malintent",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        AstralStatusEffects.SINISTER
    );

    /**
     * The bulb of rejection effect item.
     * <p>
     * Randomly provides the holder with the invisibility effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem BULB_OF_REJECTION = new RandomEffectItem("bulb_of_rejection",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.INVISIBILITY
    );

    /**
     * The cherry log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem CHERRY_LOG_SLAB = new AstralBlockItem("cherry_log_slab",
        AstralBlocks.CHERRY_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The chocolate milk food item.
     *
     * @since 2.0.0
     */
    public static final ChocolateMilkItem CHOCOLATE_MILK = new ChocolateMilkItem("chocolate_milk",
        new Settings().food(FoodComponents.CHOCOLATE_MILK).maxCount(16)
    );

    /**
     * The clock of regret effect item.
     * <p>
     * Randomly provides the holder with the nausea effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem CLOCK_OF_REGRET = new RandomEffectItem("clock_of_regret",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.NAUSEA
    );

    /**
     * The cloudy armor set.
     *
     * @since 2.0.0
     */
    public static final ArmorMap<DyeableCloudyArmorItem> CLOUDY_ARMOR = new ArmorMap<>("cloudy",
        (id, type) -> new DyeableCloudyArmorItem(id, ArmorMaterials.CLOUDY, type, new Settings())
    );

    /**
     * The cloudy candy food item.
     *
     * @since 2.0.0
     */
    public static final CloudyItem CLOUDY_CANDY = new CloudyItem("cloudy_candy",
        new Settings().food(FoodComponents.CLOUDY_CANDY).rarity(Rarity.UNCOMMON)
    );

    /**
     * The cloudy cotton item.
     *
     * @since 2.0.0
     */
    public static final CloudyItem CLOUDY_COTTON = new CloudyItem("cloudy_cotton",
        new Settings().rarity(Rarity.UNCOMMON)
    );

    /**
     * The cloudy mane item.
     *
     * @since 2.0.0
     */
    public static final CloudyItem CLOUDY_MANE = new CloudyItem("cloudy_mane", new Settings().rarity(Rarity.UNCOMMON));

    /**
     * The cobbled sandstone block item.
     *
     * @since 2.0.0
     */
    @RegistrationPriority(1)
    public static final AstralBlockItem COBBLED_SANDSTONE_BLOCK = new AstralBlockItem("cobbled_sandstone",
        AstralBlocks.COBBLED_SANDSTONE,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The cookie of resentment effect item.
     * <p>
     * Randomly provides the holder with the strength effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem COOKIE_OF_RESENTMENT = new RandomEffectItem("cookie_of_resentment",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.STRENGTH
    );

    /**
     * The crimson stem slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem CRIMSON_STEM_SLAB = new AstralBlockItem("crimson_stem_slab",
        AstralBlocks.CRIMSON_STEM_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The crown of dread effect item.
     * <p>
     * Randomly provides the holder with the darkness effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem CROWN_OF_DREAD = new RandomEffectItem("crown_of_dread",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.DARKNESS
    );

    /**
     * The cup of grief effect item.
     * <p>
     * Randomly provides the holder with the slowness effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem CUP_OF_GRIEF = new RandomEffectItem("cup_of_grief",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.SLOWNESS
    );

    /**
     * The dark oak log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem DARK_OAK_LOG_SLAB = new AstralBlockItem("dark_oak_log_slab",
        AstralBlocks.DARK_OAK_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The Ditty Dime item.
     *
     * @since 2.1.0
     */
    public static final AstralItem DITTY_DIME = new AstralItem("ditty_dime",
        new Settings().rarity(Rarity.RARE),
        AstralItemGroups.DEFAULT::getRegistryKey
    );

    /**
     * The dragon scale item.
     *
     * @since 2.0.0
     */
    public static final AstralItem DRAGON_SCALE = new AstralItem("dragon_scale",
        new Settings().rarity(Rarity.EPIC),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey
    );

    /**
     * The set of dyed amethyst block items.
     *
     * @since 2.0.0
     */
    public static final DyeMap<AstralBlockItem> DYED_AMETHYST_BLOCKS = new DyeMap<>("amethyst_block",
        (rawId, color) -> new AstralBlockItem(rawId,
            AstralBlocks.DYED_AMETHYST_BLOCKS.getComputed(color),
            new Settings(),
            AstralItemGroups.DYED_AMETHYST::getRegistryKey
        )
    );

    /**
     * The set of dyed amethyst cluster block items.
     *
     * @since 2.0.0
     */
    public static final DyeMap<AstralBlockItem> DYED_AMETHYST_CLUSTERS = new DyeMap<>("amethyst_cluster",
        (rawId, color) -> new AstralBlockItem(rawId,
            AstralBlocks.DYED_AMETHYST_CLUSTERS.getComputed(color),
            new Settings(),
            AstralItemGroups.DYED_AMETHYST::getRegistryKey
        )
    );

    /**
     * The set of dyed amethyst shard items.
     *
     * @since 2.1.0
     */
    public static final DyeMap<DyedAmethystShardItem> DYED_AMETHYST_SHARDS = new DyeMap<>("amethyst_shard",
        (path, color) -> new DyedAmethystShardItem(path,
            new Settings(),
            color,
            AstralItemGroups.DYED_AMETHYST::getRegistryKey
        )
    );

    /**
     * The set of dyed budding amethyst block items.
     *
     * @since 2.0.0
     */
    public static final DyeMap<AstralBlockItem> DYED_BUDDING_AMETHYST_BLOCKS = new DyeMap<>("budding_amethyst",
        (rawId, color) -> new AstralBlockItem(rawId,
            AstralBlocks.DYED_BUDDING_AMETHYST_BLOCKS.getComputed(color),
            new Settings(),
            AstralItemGroups.DYED_AMETHYST::getRegistryKey
        )
    );

    /**
     * The set of dyed large amethyst bud block items.
     *
     * @since 2.0.0
     */
    public static final DyeMap<AstralBlockItem> DYED_LARGE_AMETHYST_BUDS = new DyeMap<>("large_amethyst_bud",
        (rawId, color) -> new AstralBlockItem(rawId,
            AstralBlocks.DYED_LARGE_AMETHYST_BUDS.getComputed(color),
            new Settings(),
            AstralItemGroups.DYED_AMETHYST::getRegistryKey
        )
    );

    /**
     * The set of dyed medium amethyst bud block items.
     *
     * @since 2.0.0
     */
    public static final DyeMap<AstralBlockItem> DYED_MEDIUM_AMETHYST_BUDS = new DyeMap<>("medium_amethyst_bud",
        (rawId, color) -> new AstralBlockItem(rawId,
            AstralBlocks.DYED_MEDIUM_AMETHYST_BUDS.getComputed(color),
            new Settings(),
            AstralItemGroups.DYED_AMETHYST::getRegistryKey
        )
    );

    /**
     * The set of dyed small amethyst bud block items.
     *
     * @since 2.0.0
     */
    public static final DyeMap<AstralBlockItem> DYED_SMALL_AMETHYST_BUDS = new DyeMap<>("small_amethyst_bud",
        (rawId, color) -> new AstralBlockItem(rawId,
            AstralBlocks.DYED_SMALL_AMETHYST_BUDS.getComputed(color),
            new Settings(),
            AstralItemGroups.DYED_AMETHYST::getRegistryKey
        )
    );

    /**
     * The egg of greed effect item.
     * <p>
     * Randomly provides the holder with the hero of the village effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem EGG_OF_GREED = new RandomEffectItem("egg_of_greed",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.HERO_OF_THE_VILLAGE
    );

    /**
     * The eye of surprise effect item.
     * <p>
     * Randomly provides the holder with the levitation effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem EYE_OF_SURPRISE = new RandomEffectItem("eye_of_surprise",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.LEVITATION
    );

    /**
     * The flower of suffering effect item.
     * <p>
     * Randomly provides the holder with the wither effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem FLOWER_OF_SUFFERING = new RandomEffectItem("flower_of_suffering",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.WITHER
    );

    /**
     * The jungle log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem JUNGLE_LOG_SLAB = new AstralBlockItem("jungle_log_slab",
        AstralBlocks.JUNGLE_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The living sculk item.
     *
     * @since 2.0.0
     */
    public static final AstralItem LIVING_SCULK = new AstralItem("living_sculk",
        new Settings().rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey
    );

    /**
     * The mangrove log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem MANGROVE_LOG_SLAB = new AstralBlockItem("mangrove_log_slab",
        AstralBlocks.MANGROVE_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The Fluxling's mirror item.
     *
     * @since 2.0.0
     */
    public static final MirrorItem MIRROR = new MirrorItem("mirror", new Settings().maxCount(1).rarity(Rarity.RARE));

    /**
     * The oak log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem OAK_LOG_SLAB = new AstralBlockItem("oak_log_slab",
        AstralBlocks.OAK_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The photo of hope effect item.
     * <p>
     * Randomly provides the holder with the luck effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem PHOTO_OF_HOPE = new RandomEffectItem("photo_of_hope",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.LUCK
    );

    /**
     * The pig card item.
     *
     * @since 2.0.0
     */
    public static final AstralItem PIG_CARD = new AstralItem("pig_card",
        new Settings(),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey
    );

    /**
     * The placeholder item.
     *
     * @since 2.0.0
     */
    public static final PlaceholderItem PLACEHOLDER = new PlaceholderItem("placeholder",
        new Settings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON),
        null
    );

    /**
     * The randomizer block item.
     *
     * @since 2.0.0
     */
    public static final AstralBlockItem RANDOMIZER = new AstralBlockItem("randomizer",
        AstralBlocks.RANDOMIZER,
        new Settings().rarity(Rarity.UNCOMMON),
        () -> ItemGroups.REDSTONE
    );

    /**
     * The rotten chorus fruit food item.
     *
     * @since 2.0.0
     */
    public static final AstralItem ROTTEN_CHORUS_FRUIT = new AstralItem("rotten_chorus_fruit",
        new Settings().food(FoodComponents.ROTTEN_CHORUS_FRUIT),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey
    );

    /**
     * The skull of joy effect item.
     * <p>
     * Randomly provides the holder with the regeneration effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem SKULL_OF_JOY = new RandomEffectItem("skull_of_joy",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.REGENERATION
    );

    /**
     * The slime card item.
     *
     * @since 2.0.0
     */
    public static final AstralItem SLIME_CARD = new AstralItem("slime_card",
        new Settings(),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey
    );

    /**
     * The smooth stone stairs block item.
     *
     * @since 2.2.0
     */
    @RegistrationPriority(1)
    public static final AstralBlockItem SMOOTH_STONE_STAIRS = new AstralBlockItem("smooth_stone_stairs",
        AstralBlocks.SMOOTH_STONE_STAIRS,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The spray bottle item.
     *
     * @since 2.0.0
     */
    public static final SprayBottleItem SPRAY_BOTTLE = new SprayBottleItem("spray_bottle",
        new Settings().maxDamage(SprayBottleItem.MAX_USES)
    );

    /**
     * The spray potion item.
     *
     * @since 2.0.0
     */
    public static final SprayPotionItem SPRAY_POTION = new SprayPotionItem("spray_potion",
        new Settings().maxDamage(SprayPotionItem.MAX_USES)
    );

    /**
     * The spruce log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem SPRUCE_LOG_SLAB = new AstralBlockItem("spruce_log_slab",
        AstralBlocks.SPRUCE_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The strawberry milk food item.
     *
     * @since 2.0.0
     */
    public static final BottleItem STRAWBERRY_MILK = new BottleItem("strawberry_milk",
        new Settings().food(FoodComponents.STRAWBERRY_MILK).maxCount(16)
    );

    /**
     * The stripped acacia log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_ACACIA_LOG_SLAB = new AstralBlockItem("stripped_acacia_log_slab",
        AstralBlocks.STRIPPED_ACACIA_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped bamboo block slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_BAMBOO_BLOCK_SLAB = new AstralBlockItem("stripped_bamboo_block_slab",
        AstralBlocks.STRIPPED_BAMBOO_BLOCK_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped birch log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_BIRCH_LOG_SLAB = new AstralBlockItem("stripped_birch_log_slab",
        AstralBlocks.STRIPPED_BIRCH_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped cherry log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_CHERRY_LOG_SLAB = new AstralBlockItem("stripped_cherry_log_slab",
        AstralBlocks.STRIPPED_CHERRY_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped crimson stem slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_CRIMSON_STEM_SLAB = new AstralBlockItem("stripped_crimson_stem_slab",
        AstralBlocks.STRIPPED_CRIMSON_STEM_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped dark oak log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_DARK_OAK_LOG_SLAB = new AstralBlockItem("stripped_dark_oak_log_slab",
        AstralBlocks.STRIPPED_DARK_OAK_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped jungle log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_JUNGLE_LOG_SLAB = new AstralBlockItem("stripped_jungle_log_slab",
        AstralBlocks.STRIPPED_JUNGLE_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped mangrove log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_MANGROVE_LOG_SLAB = new AstralBlockItem("stripped_mangrove_log_slab",
        AstralBlocks.STRIPPED_MANGROVE_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped oak log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_OAK_LOG_SLAB = new AstralBlockItem("stripped_oak_log_slab",
        AstralBlocks.STRIPPED_OAK_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped spruce log slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_SPRUCE_LOG_SLAB = new AstralBlockItem("stripped_spruce_log_slab",
        AstralBlocks.STRIPPED_SPRUCE_LOG_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The stripped warped stem slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem STRIPPED_WARPED_STEM_SLAB = new AstralBlockItem("stripped_warped_stem_slab",
        AstralBlocks.STRIPPED_WARPED_STEM_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    /**
     * The target of panic effect item.
     * <p>
     * Randomly provides the holder with the speed effect.
     *
     * @since 2.0.0
     */
    public static final RandomEffectItem TARGET_OF_PANIC = new RandomEffectItem("target_of_panic",
        new Settings().maxCount(1).rarity(Rarity.RARE),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey,
        0.0005F,
        StatusEffects.SPEED
    );

    /**
     * The void essence item.
     *
     * @since 2.0.0
     */
    public static final AstralItem VOID_ESSENCE = new AstralItem("void_essence",
        new Settings().rarity(Rarity.EPIC),
        AstralItemGroups.STARMONEY_PLAZA::getRegistryKey
    );

    /**
     * The warped stem slab block item.
     *
     * @since 2.2.0
     */
    public static final AstralBlockItem WARPED_STEM_SLAB = new AstralBlockItem("warped_stem_slab",
        AstralBlocks.WARPED_STEM_SLAB,
        new Settings(),
        () -> ItemGroups.BUILDING_BLOCKS
    );

    @Override
    public void generate() {
        super.generate();

        final TagGenerator tag = TagGenerator.getInstance();

        tag.generate(DyedAmethystBlock.AMETHYST_BLOCK_ITEMS, b -> b.add(Items.AMETHYST_BLOCK));
        tag.generate(DyedBuddingAmethystBlock.BUDDING_AMETHYST_ITEMS, b -> b.add(Items.BUDDING_AMETHYST));
        tag.generate(DyedAmethystClusterBlock.AMETHYST_CLUSTER_ITEMS, b -> b.add(Items.AMETHYST_CLUSTER));
        tag.generate(DyedAmethystClusterBlock.LARGE_AMETHYST_BUD_ITEMS, b -> b.add(Items.LARGE_AMETHYST_BUD));
        tag.generate(DyedAmethystClusterBlock.MEDIUM_AMETHYST_BUD_ITEMS, b -> b.add(Items.MEDIUM_AMETHYST_BUD));
        tag.generate(DyedAmethystClusterBlock.SMALL_AMETHYST_BUD_ITEMS, b -> b.add(Items.SMALL_AMETHYST_BUD));
        tag.generate(DyedAmethystShardItem.AMETHYST_SHARDS, b -> b.add(Items.AMETHYST_SHARD));
    }

    /**
     * Contains shared food components for custom items.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    private static final class FoodComponents {

        /**
         * The chocolate milk food component.
         *
         * @since 2.0.0
         */
        public static final FoodComponent CHOCOLATE_MILK = new FoodComponent.Builder().alwaysEdible()
            .hunger(6)
            .saturationModifier(0.25F)
            .build();

        /**
         * The cloudy candy food component.
         *
         * @since 2.0.0
         */
        public static final FoodComponent CLOUDY_CANDY = new FoodComponent.Builder().alwaysEdible()
            .hunger(2)
            .saturationModifier(0.45F)
            .snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 0, false, true, true), 0.25F)
            .build();

        /**
         * The rotten chorus fruit food component.
         *
         * @since 2.0.0
         */
        public static final FoodComponent ROTTEN_CHORUS_FRUIT = new FoodComponent.Builder().alwaysEdible()
            .hunger(1)
            .saturationModifier(0.5F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 0), 1F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 400, 0), 1F)
            .build();

        /**
         * The strawberry milk food component.
         *
         * @since 2.0.0
         */
        public static final FoodComponent STRAWBERRY_MILK = new FoodComponent.Builder().alwaysEdible()
            .hunger(7)
            .saturationModifier(0.25F)
            .build();

        private FoodComponents() { }

    }

    /**
     * Contains shared armor materials for custom items.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    private static final class ArmorMaterials {

        /**
         * The material used by cloudy armor.
         *
         * @since 2.0.0
         */
        public static final Material CLOUDY = Material.builder("cloudy")
            .setDurability(52, 64, 60, 44)
            .setEnchantability(15)
            .setEquipSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER)
            .setProtection(2, 5, 3, 2)
            .setRepairIngredient(Ingredient.ofItems(CLOUDY_COTTON))
            .build();

        private ArmorMaterials() { }

    }

}
