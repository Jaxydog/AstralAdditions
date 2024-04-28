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

package dev.jaxydog.astral.utility;

import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.CustomGamerules;
import dev.jaxydog.astral.utility.injected.AstralLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

/**
 * Provides commonly used fields and methods for mob challenge scaling.
 *
 * @author Jaxydog
 * @since 1.1.0
 */
@NonExtendable
public interface ChallengeHelper {

    /**
     * The block position used as the world origin.
     * <p>
     * The Y value does not matter, as it is overwritten, but just for sanity's sake it's set to sea level.
     *
     * @since 1.1.1
     */
    BlockPos ORIGIN = new BlockPos(0, 63, 0);

    /**
     * An NBT key that tells an entity to ignore challenge scaling.
     * <p>
     * If this is set, the entity will never be scaled.
     *
     * @since 1.1.0
     */
    String IGNORE_KEY = "IgnoreChallengeScaling";
    /**
     * An NBT key that tells an entity to force-enable challenge scaling.
     * <p>
     * If this is set, the entity will *always* be scaled (if supported).
     *
     * @since 1.1.0
     */
    String FORCE_KEY = "ForceChallengeScaling";

    /**
     * A tag that determines which entities are scaled.
     * <p>
     * If an entity is not present within this tag, it will never be scaled (unless set through NBT).
     *
     * @since 1.1.0
     */
    TagKey<EntityType<?>> SCALED_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, Astral.getId("challenge"));

    /**
     * Determines whether a given entity should have challenge scaling applied to them.
     *
     * @param entity The target entity.
     *
     * @return Whether to enable scaling for the entity.
     *
     * @since 2.0.0
     */
    @SuppressWarnings({ "BooleanMethodIsAlwaysInverted", "RedundantCast" })
    static boolean shouldApplyScaling(Entity entity) {
        // Ensure the entity is living firstly. If not, then return early.
        return entity instanceof final LivingEntity living
            // Check is scaling is being forced, and return early if so.
            && (((AstralLivingEntity) living).astral$forcesChallengeScaling()
            // Check if scaling is enabled.
            || (isEnabled(living.getWorld())
            // Check if the entity is also in the scaling tag.
            && living.getType().isIn(SCALED_ENTITIES)
            // Check that the entity doesn't ignore scaling.
            && !((AstralLivingEntity) living).astral$ignoresChallengeScaling()
            // Check that the entity isn't tamed.
            && (!(living instanceof final TameableEntity tamable) || !tamable.isTamed())));
    }

    /**
     * Returns whether challenge scaling is enabled in the current world.
     *
     * @param world The current world.
     *
     * @return Whether scaling is enabled in this world.
     *
     * @since 1.1.0
     */
    static boolean isEnabled(World world) {
        return world.getGameRules().getBoolean(CustomGamerules.CHALLENGE_ENABLED);
    }

    /**
     * Returns whether challenge scaling should use the world's spawnpoint, rather than the world origin.
     *
     * @param world The current world.
     *
     * @return Whether the world's spawnpoint should be used.
     *
     * @since 2.0.0
     */
    static boolean useWorldSpawn(World world) {
        return world.getGameRules().getBoolean(CustomGamerules.CHALLENGE_USE_WORLDSPAWN);
    }

    /**
     * Returns the world's configured attack additive value.
     * <p>
     * This value is used within scaling calculations to determine an entity's added attack damage.
     *
     * @param world The current world.
     *
     * @return The world's configured attack additive.
     *
     * @since 1.1.0
     */
    static double getAttackAdditive(World world) {
        return world.getGameRules().get(CustomGamerules.CHALLENGE_ATTACK_ADDITIVE).get();
    }

    /**
     * Returns the world's configured health additive value.
     * <p>
     * This value is used within scaling calculations to determine an entity's added health value.
     *
     * @param world The current world.
     *
     * @return The world's configured health additive.
     *
     * @since 1.1.0
     */
    static double getHealthAdditive(World world) {
        return world.getGameRules().get(CustomGamerules.CHALLENGE_HEALTH_ADDITIVE).get();
    }

    /**
     * Returns a statistic additive that has been scaled using the world's challenge configuration.
     *
     * @param entity The target entity.
     * @param additive The statistical additive.
     *
     * @return The scaled additive.
     *
     * @since 1.1.1
     */
    static double getScaledAdditive(Entity entity, double additive) {
        if (entity == null || entity.getWorld() == null) return additive;

        final int step = getChunkStep(entity.getWorld());
        final double distance = getSpawnDistance(entity);
        // Scale by chunks, not blocks.
        final double modifier = Math.max(0D, additive) * ((distance / 16D) / step);
        final boolean overworld = entity.getWorld().getRegistryKey().equals(World.OVERWORLD);

        return overworld ? modifier : modifier / 2D;
    }

    /**
     * Returns the world's configured chunk step size.
     * <p>
     * This is the main factor that determines how distance affects additives.
     *
     * @param world The current world.
     *
     * @return The world's configured chunk step.
     *
     * @since 1.1.0
     */
    static int getChunkStep(World world) {
        return Math.max(world.getGameRules().getInt(CustomGamerules.CHALLENGE_CHUNK_STEP), 1);
    }

    /**
     * Returns the given entity's distance from the world's configured origin.
     * <p>
     * The origin will either be (0, 0), or the world's spawnpoint, depending on the configured gamerule.
     * <p>
     * This method ignores height differences, only considering horizontal distance.
     *
     * @param entity The target entity.
     *
     * @return The entity's distance from the origin.
     *
     * @since 1.1.0
     */
    static double getSpawnDistance(Entity entity) {
        if (entity == null || entity.getWorld() == null) return 1D;

        final World world = entity.getWorld();
        final BlockPos center = useWorldSpawn(world) ? world.getSpawnPos() : ORIGIN;
        final BlockPos adjusted = center.withY(entity.getBlockY());

        return Math.sqrt(entity.getBlockPos().getSquaredDistance(adjusted));
    }

}
