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

import dev.jaxydog.astral.content.power.AstralPowerFactory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A custom ticking cooldown power, that allows for extra features compared to {@link CooldownPower}.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
public class TickingCooldownPower extends CooldownPower {

    /**
     * A predicate that determines whether the cooldown may update.
     *
     * @since 1.7.0
     */
    private final @Nullable Predicate<Entity> tickCondition;

    /**
     * An action run on the holding entity when the cooldown is emptied.
     *
     * @since 1.7.0
     */
    private final @Nullable Consumer<Entity> minAction;
    /**
     * An action run on the holding entity when the cooldown is changed.
     *
     * @since 1.7.0
     */
    private final @Nullable Consumer<Entity> setAction;
    /**
     * An action run on the holding entity when the cooldown is recharged.
     *
     * @since 1.7.0
     */
    private final @Nullable Consumer<Entity> maxAction;

    /**
     * The current cooldown progress.
     *
     * @since 1.7.0
     */
    protected int progress;

    /**
     * Creates a new ticking cooldown power.
     *
     * @param type The power's type.
     * @param entity The holding entity.
     * @param duration The duration of the cooldown.
     * @param hudRender The power's HUD render.
     * @param tickCondition The condition that allows this cooldown to update.
     * @param minAction An action run when the cooldown is set.
     * @param setAction An action run when the cooldown is changed.
     * @param maxAction An action run when the cooldown is recharged.
     *
     * @since 1.7.0
     */
    public TickingCooldownPower(
        PowerType<?> type,
        LivingEntity entity,
        int duration,
        HudRender hudRender,
        @Nullable Predicate<Entity> tickCondition,
        @Nullable Consumer<Entity> minAction,
        @Nullable Consumer<Entity> setAction,
        @Nullable Consumer<Entity> maxAction
    ) {
        super(type, entity, duration, hudRender);

        this.tickCondition = tickCondition;
        this.minAction = minAction;
        this.setAction = setAction;
        this.maxAction = maxAction;
        this.progress = duration;

        this.setTicking(true);
    }

    /**
     * Returns this power's default factory.
     *
     * @return This power's default factory.
     *
     * @since 1.7.0
     */
    public static AstralPowerFactory<TickingCooldownPower> getFactory() {
        return new AstralPowerFactory<>(
            "ticking_cooldown",
            new SerializableData().add("cooldown", SerializableDataTypes.INT)
                .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
                .add("tick_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("min_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("set_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("max_action", ApoliDataTypes.ENTITY_ACTION, null),
            data -> (type, entity) -> new TickingCooldownPower(
                type,
                entity,
                data.getInt("cooldown"),
                data.get("hud_render"),
                data.get("tick_condition"),
                data.get("min_action"),
                data.get("set_action"),
                data.get("max_action")
            )
        );
    }

    @Override
    public boolean canUse() {
        return this.progress >= this.cooldownDuration && this.isActive();
    }

    @Override
    public void use() {
        this.setCooldown(this.cooldownDuration);

        PowerHolderComponent.syncPower(this.entity, this.type);
    }

    @Override
    public float getProgress() {
        return MathHelper.clamp((float) this.progress / (float) this.cooldownDuration, 0F, 1F);
    }

    @Override
    public int getRemainingTicks() {
        return MathHelper.clamp(this.cooldownDuration - this.progress, 0, this.cooldownDuration);
    }

    @Override
    public void modify(int changeInTicks) {
        this.progress = MathHelper.clamp(this.progress - changeInTicks, 0, this.cooldownDuration);

        if (this.setAction != null) this.setAction.accept(this.entity);
    }

    @Override
    public void setCooldown(int cooldownInTicks) {
        this.progress = MathHelper.clamp(this.cooldownDuration - cooldownInTicks, 0, this.cooldownDuration);

        if (this.setAction != null) this.setAction.accept(this.entity);
    }

    @Override
    public void tick() {
        if (this.progress >= this.cooldownDuration) return;

        if (this.progress <= 0 && this.minAction != null) {
            this.minAction.accept(this.entity);
        }

        if (this.tickCondition == null || this.tickCondition.test(this.entity)) {
            this.progress += 1;

            PowerHolderComponent.syncPower(this.entity, this.type);
        }

        if (this.progress >= this.cooldownDuration && this.maxAction != null) {
            this.maxAction.accept(this.entity);
        }
    }

    @Override
    public NbtElement toTag() {
        return NbtInt.of(this.progress);
    }

    @Override
    public void fromTag(NbtElement tag) {
        this.progress = ((NbtInt) tag).intValue();
    }

    @Override
    public boolean shouldRender() {
        return this.progress < this.cooldownDuration;
    }

}
