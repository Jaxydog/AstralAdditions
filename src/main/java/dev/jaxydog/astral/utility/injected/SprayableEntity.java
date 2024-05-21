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

package dev.jaxydog.astral.utility.injected;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * An entity that can be sprayed.
 *
 * @author Jaxydog
 * @since 1.6.0
 */
public interface SprayableEntity {

    /**
     * Sprays this entity.
     *
     * @param source The source entity, if applicable.
     * @param ticks The amount of ticks to be sprayed for.
     * @param initialSpray Whether this is the first spray on this entity.
     *
     * @since 1.6.0
     */
    void astral$setSprayed(@Nullable LivingEntity source, int ticks, boolean initialSpray);

    /**
     * Sprays this entity.
     *
     * @param source The source entity, if applicable.
     * @param ticks The amount of ticks to be sprayed for.
     *
     * @since 1.6.0
     */
    default void astral$setSprayed(@Nullable LivingEntity source, int ticks) {
        this.astral$setSprayed(source, ticks, !this.astral$isSprayed());
    }

    /**
     * Un-sprays this entity.
     *
     * @since 1.6.0
     */
    default void astral$setUnsprayed() {
        this.astral$setSprayed(null, 0, false);
    }

    /**
     * Returns this entity's spray source.
     *
     * @return This entity's spray source.
     *
     * @since 1.6.0
     */
    @Nullable
    LivingEntity astral$getSpraySource();

    /**
     * Returns the amount of ticks remaining in the current spray.
     *
     * @return The amount of ticks remaining in the current spray.
     *
     * @since 1.6.0
     */
    int astral$getSprayTicks();

    /**
     * Returns the number of charges that it takes to spray this entity.
     *
     * @return The number of charges that it takes to spray this entity.
     *
     * @since 1.6.0
     */
    default int astral$getSprayCharges() {
        return 1;
    }

    /**
     * Returns whether this entity is currently sprayed.
     *
     * @return Whether this entity is currently sprayed.
     *
     * @since 1.6.0
     */
    default boolean astral$isSprayed() {
        return this.astral$getSprayTicks() > 0;
    }

    /**
     * Returns whether this entity can be sprayed.
     *
     * @return Whether this entity can be sprayed.
     *
     * @since 1.6.0
     */
    default boolean astral$canSpray() {
        return !this.astral$isSprayed();
    }

    /**
     * Ticks this entity's spray status.
     *
     * @since 1.6.0
     */
    default void astral$sprayTick() {
        if (this.astral$isSprayed()) {
            final int ticks = this.astral$getSprayTicks() - 1;

            this.astral$setSprayed(this.astral$getSpraySource(), ticks, false);
        } else {
            this.astral$setUnsprayed();
        }
    }

    /**
     * A goal that causes a sprayed entity to run away from its sprayer.
     *
     * @param <T> The entity type.
     *
     * @since 1.6.0
     */
    class EscapeSprayGoal<T extends PathAwareEntity & SprayableEntity> extends Goal {

        /**
         * The entity instance.
         *
         * @since 1.6.0
         */
        protected final T entity;
        /**
         * The speed at which to run.
         *
         * @since 1.6.0
         */
        protected final double speed;

        /**
         * The movement path.
         *
         * @since 1.6.0
         */
        protected @Nullable Path path;

        /**
         * Creates a new {@link EscapeSprayGoal}.
         *
         * @param entity The entity instance.
         * @param speed The speed at which to run.
         *
         * @since 1.6.0
         */
        public EscapeSprayGoal(T entity, double speed) {
            this.entity = entity;
            this.speed = speed;

            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        /**
         * Attempts to find a location to run towards.
         *
         * @return Whether a target path was found.
         *
         * @since 1.6.0
         */
        protected boolean findTarget() {
            final LivingEntity source = this.entity.astral$getSpraySource();

            if (source == null) return false;

            final Vec3d target = NoPenaltyTargeting.findFrom(this.entity, 16, 7, source.getPos());

            if (target == null) return false;

            if ((source).squaredDistanceTo(target.x, target.y, target.z) < source.squaredDistanceTo(this.entity)) {
                return false;
            }

            this.path = this.entity.getNavigation().findPathTo(target.getX(), target.getY(), target.getZ(), 0);

            return this.path != null;
        }

        @Override
        public boolean canStart() {
            return this.entity.astral$isSprayed() && this.findTarget();
        }

        @Override
        public void start() {
            this.entity.getNavigation().startMovingAlong(this.path, this.speed);
        }

        @Override
        public boolean shouldContinue() {
            return this.entity.astral$isSprayed() && !this.entity.getNavigation().isIdle();
        }

        @Override
        public void stop() {
            this.path = null;
        }

    }

}
