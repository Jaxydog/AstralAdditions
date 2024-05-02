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

package dev.jaxydog.astral.content.power.custom;

import dev.jaxydog.astral.content.power.AstralPower;
import dev.jaxydog.astral.content.power.AstralPowerFactory;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A power that applies modifiers to various scale types.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
public class ModifyScalePower extends AstralPower {

    /**
     * The list of modified scale types.
     *
     * @since 1.7.0
     */
    private final Set<ScaleType> scaleTypes = new ObjectArraySet<>();
    /**
     * The list of scale modifiers.
     * <p>
     * This is created with a capacity of 1, assuming there will be at least one modifier.
     *
     * @since 1.7.0
     */
    private final List<Modifier> modifiers = new ObjectArrayList<>(1);

    /**
     * The duration of the scale transition period in ticks.
     *
     * @since 1.7.0
     */
    private final int transition;
    /**
     * The interval at which to update the power in ticks.
     *
     * @since 1.7.0
     */
    private final int tickRate;

    /**
     * Whether the entity's scale should be reset when the power is lost.
     *
     * @since 1.7.0
     */
    private final boolean resetOnLoss;

    /**
     * Creates a new modify scale power.
     *
     * @param type The power's type.
     * @param entity The holding entity.
     * @param scaleTypes A collection of scale types.
     * @param transition The tick duration of the transition period.
     * @param tickRate The tick interval at which to update.
     * @param resetOnLoss Whether scales are reset when the power is lost.
     *
     * @since 1.7.0
     */
    public ModifyScalePower(
        PowerType<?> type,
        LivingEntity entity,
        Collection<ScaleType> scaleTypes,
        int transition,
        int tickRate,
        boolean resetOnLoss
    ) {
        super(type, entity);

        this.scaleTypes.addAll(scaleTypes);
        this.transition = transition;
        this.tickRate = tickRate;
        this.resetOnLoss = resetOnLoss;

        this.setTicking(true);
    }

    /**
     * Returns the power factory associated with this power type.
     *
     * @return The power factory associated with this power type.
     *
     * @since 1.7.0
     */
    public static AstralPowerFactory<ModifyScalePower> getFactory() {
        return new AstralPowerFactory<ModifyScalePower>(
            "modify_scale",
            new SerializableData().add("scale_types", SerializableDataTypes.IDENTIFIERS)
                .add("transition", SerializableDataTypes.INT, 0)
                .add("tick_rate", SerializableDataTypes.INT, 1)
                .add("reset_on_loss", SerializableDataTypes.BOOLEAN, true)
                .add("modifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null),
            data -> (type, entity) -> {
                final List<Identifier> scaleTypeIds = data.get("scale_types");
                final int transition = data.getInt("transition");
                final int rate = data.getInt("tick_rate");
                final boolean resetOnLoss = data.getBoolean("reset_on_loss");
                final @Nullable Modifier modifier = data.get("modifier");
                final @Nullable List<Modifier> modifiers = data.get("modifiers");

                final List<ScaleType> types = scaleTypeIds.stream()
                    .map(i -> ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, i))
                    .toList();

                final ModifyScalePower power = new ModifyScalePower(type, entity, types, transition, rate, resetOnLoss);

                if (modifier != null) power.addModifier(modifier);

                if (modifiers != null && !modifiers.isEmpty()) {
                    modifiers.forEach(power::addModifier);
                }

                return power;
            }
        ).allowCondition();
    }

    /**
     * Adds a scale modifier to the power.
     *
     * @param modifier A scale modifier.
     *
     * @since 1.7.0
     */
    public void addModifier(Modifier modifier) {
        this.modifiers.add(modifier);

    }

    /**
     * Applies all modifiers to the entity.
     *
     * @since 1.7.0
     */
    private void applyModifiers() {
        this.scaleTypes.forEach(this::applyModifiers);
    }

    /**
     * Applies all modifiers to the entity for the provided scale type.
     *
     * @param type The target scale type.
     *
     * @since 1.7.0
     */
    private void applyModifiers(ScaleType type) {
        final ScaleData scale = ScaleData.Builder.create().entity(this.entity).type(type).build();

        double target = 1D;

        for (final Modifier modifier : this.modifiers) {
            target = modifier.apply(this.entity, target);
        }

        if (target != scale.getTargetScale()) {
            scale.setScaleTickDelay(this.transition);
            scale.setTargetScale((float) target);
        }
    }

    /**
     * Removes all modifiers from the entity.
     *
     * @since 1.7.0
     */
    private void removeModifiers() {
        this.scaleTypes.forEach(this::removeModifiers);
    }

    /**
     * Removes all modifiers from the entity for the provided scale type.
     *
     * @param type The target scale type.
     *
     * @since 1.7.0
     */
    private void removeModifiers(ScaleType type) {
        final ScaleData scale = ScaleData.Builder.create().entity(this.entity).type(type).build();

        if (scale.getScale() != 1F) {
            scale.setScaleTickDelay(this.transition);
            scale.setTargetScale(1F);
        }
    }

    @Override
    public void tick() {
        if (this.entity.age % this.tickRate != 0) return;

        if (this.isActive()) {
            this.applyModifiers();
        } else if (this.resetOnLoss) {
            this.removeModifiers();
        }
    }

    @Override
    public void onRemoved() {
        if (this.resetOnLoss) this.removeModifiers();
    }

}
