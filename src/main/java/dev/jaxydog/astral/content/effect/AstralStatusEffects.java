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

import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.utility.injected.AstralLightningEntity;
import io.github.apace100.origins.util.Scheduler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.util.math.random.Random;

/**
 * Contains definitions of all modded-in status effects.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public final class AstralStatusEffects extends ContentRegistrar {

    /**
     * The sinister effect.
     *
     * @since 2.0.0
     */
    public static final AstralStatusEffect SINISTER = new AstralStatusEffect("sinister",
        StatusEffectCategory.HARMFUL,
        0xE43727
    ) {

        /**
         * A scheduler instance used to delay effects for a tick.
         *
         * @since 2.0.0
         */
        private final Scheduler scheduler = new Scheduler();

        @SuppressWarnings("RedundantCast")
        @Override
        public synchronized void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
            switch (Math.min(entity.getRandom().nextBetween(0, amplifier + 2), 5)) {
                case 0 -> entity.damage(entity.getWorld().getDamageSources().magic(), 2 * (amplifier + 2));
                case 1 -> {
                    entity.setFrozenTicks(0);
                    entity.setOnFireFor(amplifier + 2);
                }
                case 2 -> {
                    entity.extinguishWithSound();
                    entity.setFrozenTicks(60 * (amplifier + 2));
                }
                case 3 -> {
                    final Random random = entity.getWorld().getRandom();
                    final double x = (random.nextDouble() - 0.5) * 2;
                    final double y = amplifier * 1.5;
                    final double z = (random.nextDouble() - 0.5) * 2;

                    entity.setVelocity(x, y, z);
                    entity.velocityModified = true;
                }
                case 4 -> {
                    if (entity.isSubmergedInWater()) {
                        final Random random = entity.getWorld().getRandom();
                        final double x = (random.nextDouble() - 0.5) * 2;
                        final double y = -amplifier * 0.75;
                        final double z = (random.nextDouble() - 0.5) * 2;

                        // Attempt to drown the player if they're swimming.
                        entity.setAir(0);
                        entity.setVelocity(x, y, z);
                        entity.velocityModified = true;
                    } else if (!entity.getWorld().isClient()) {
                        // Otherwise spawn a pufferfish on them.
                        final PufferfishEntity pufferfish = new PufferfishEntity(EntityType.PUFFERFISH,
                            entity.getWorld()
                        );

                        pufferfish.setPosition(entity.getSyncedPos());

                        entity.getWorld().spawnEntity(pufferfish);
                    }
                }
                case 5 -> {
                    if (entity.getWorld().isClient()) return;

                    // Strike them with lightning.
                    final LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, entity.getWorld());

                    lightning.setPosition(entity.getSyncedPos());
                    ((AstralLightningEntity) lightning).astral$setPreservesItems(true);

                    entity.getWorld().spawnEntity(lightning);
                }
            }

            // Continue re-applying until the amplifier is zero.
            if (amplifier > 0) {
                this.scheduler.queue(server -> entity.addStatusEffect(new StatusEffectInstance(SINISTER,
                    Math.min(20 * (amplifier), 200),
                    amplifier - 1
                )), 1);
            }
        }
    };

}
