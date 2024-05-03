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
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Runs an action when a block or entity is sprayed.
 * <p>
 * This is the inverse of {@link ActionWhenSprayedPower}.
 *
 * @author Jaxydog
 * @since 1.6.0
 */
public class ActionOnSprayPower extends AstralPower {

    /**
     * The power's execution priority; higher means this power is run first.
     *
     * @since 1.6.0
     */
    private final int priority;
    /**
     * The number of charges consumed when executing the action.
     *
     * @since 1.6.0
     */
    private final int charges;

    /**
     * An action run on the held item when spraying.
     *
     * @since 1.6.0
     */
    private final @Nullable Consumer<Pair<World, ItemStack>> itemAction;
    /**
     * A predicate that only allows the action to be run if it resolves to {@code true}.
     *
     * @since 1.6.0
     */
    private final @Nullable Predicate<ItemStack> itemCondition;
    /**
     * An action run on both entities when spraying.
     *
     * @since 1.6.0
     */
    private final @Nullable Consumer<Pair<Entity, Entity>> bientityAction;
    /**
     * A predicate that only allows the action to be run if it resolves to {@code true}.
     *
     * @since 1.6.0
     */
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;
    /**
     * An action run on the targeted block when spraying.
     *
     * @since 1.6.0
     */
    private final @Nullable Consumer<Triple<World, BlockPos, Direction>> blockAction;
    /**
     * A predicate that only allows the action to be run if it resolves to {@code true}.
     *
     * @since 1.6.0
     */
    private final @Nullable Predicate<CachedBlockPosition> blockCondition;

    /**
     * Creates a new action on sprayed power.
     *
     * @param type The power type.
     * @param entity The holding entity.
     * @param priority The execution priority.
     * @param charges The charges consumed when the action is run.
     * @param bientityAction An action run on both entities when spraying.
     * @param bientityCondition A predicate that only allows the action to be run if it resolves to {@code true}.
     * @param itemAction An action run on the held item when spraying.
     * @param itemCondition A predicate that only allows the action to be run if it resolves to {@code true}.
     * @param blockAction An action run on the targeted block when spraying.
     * @param blockCondition A predicate that only allows the action to be run if it resolves to {@code true}.
     *
     * @since 1.6.0
     */
    public ActionOnSprayPower(
        PowerType<?> type,
        LivingEntity entity,
        int priority,
        int charges,
        @Nullable Consumer<Pair<World, ItemStack>> itemAction,
        @Nullable Predicate<ItemStack> itemCondition,
        @Nullable Consumer<Pair<Entity, Entity>> bientityAction,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition,
        @Nullable Consumer<Triple<World, BlockPos, Direction>> blockAction,
        @Nullable Predicate<CachedBlockPosition> blockCondition
    ) {
        super(type, entity);

        this.priority = priority;
        this.charges = charges;
        this.itemAction = itemAction;
        this.itemCondition = itemCondition;
        this.bientityAction = bientityAction;
        this.bientityCondition = bientityCondition;
        this.blockAction = blockAction;
        this.blockCondition = blockCondition;
    }

    /**
     * Returns this power's default factory.
     *
     * @return This power's default factory.
     *
     * @since 1.6.0
     */
    public static AstralPowerFactory<ActionOnSprayPower> getFactory() {
        return new AstralPowerFactory<ActionOnSprayPower>(
            "action_on_spray",
            new SerializableData().add("priority", SerializableDataTypes.INT, 0)
                .add("charges", SerializableDataTypes.INT, 1)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
            data -> (type, entity) -> new ActionOnSprayPower(
                type,
                entity,
                data.getInt("priority"),
                data.getInt("charges"),
                data.get("item_action"),
                data.get("item_condition"),
                data.get("bientity_action"),
                data.get("bientity_condition"),
                data.get("block_action"),
                data.get("block_condition")
            )
        ).allowCondition();
    }

    /**
     * Returns the power's execution priority, which is used to determine when this power is triggered compared to other
     * held powers.
     *
     * @return The execution priority.
     *
     * @since 1.6.0
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Returns the total number of charges to consume when a spray is successful.
     *
     * @return The expended charges.
     *
     * @since 1.6.0
     */
    public int getCharges() {
        return this.charges;
    }

    /**
     * Returns whether the given entity may be sprayed using the given stack.
     *
     * @param target The target entity.
     * @param stack The item stack.
     *
     * @return Whether the entity may be sprayed.
     *
     * @since 1.6.0
     */
    public boolean canSpray(Entity target, ItemStack stack) {
        // Then ensure that there is an action to run.
        return this.bientityAction != null
            // Test the item condition.
            && (this.itemCondition == null || this.itemCondition.test(stack))
            // Then test the bientity condition.
            && (this.bientityCondition == null || this.bientityCondition.test(new Pair<>(this.entity, target)));
    }

    /**
     * Returns whether the given entity may be sprayed using the given stack.
     *
     * @param world The current world.
     * @param pos The block's position.
     * @param stack The item stack.
     *
     * @return Whether the block may be sprayed.
     *
     * @since 1.6.0
     */
    public boolean canSpray(World world, BlockPos pos, ItemStack stack) {
        // Then ensure that there is an action to run.
        return this.blockAction != null
            // Test the item condition.
            && (this.itemCondition == null || this.itemCondition.test(stack))
            // Then test the bientity condition.
            && (this.blockCondition == null || this.blockCondition.test(new CachedBlockPosition(world, pos, true)));
    }

    /**
     * Attempts to spray the target entity.
     *
     * @param target The target entity.
     * @param stack The item stack.
     *
     * @since 1.6.0
     */
    public void onSpray(Entity target, ItemStack stack) {
        if (this.bientityAction == null) return;

        this.bientityAction.accept(new Pair<>(this.entity, target));

        if (this.itemAction != null) {
            this.itemAction.accept(new Pair<>(this.entity.getWorld(), stack));
        }
    }

    /**
     * Attempts to spray the target block.
     *
     * @param world The current world.
     * @param pos The block's position.
     * @param direction The side of the block that was sprayed.
     * @param stack The item stack.
     *
     * @since 1.6.0
     */
    public void onSpray(World world, BlockPos pos, Direction direction, ItemStack stack) {
        if (this.blockAction == null) return;

        this.blockAction.accept(new ImmutableTriple<>(world, pos, direction));

        if (this.itemAction != null) {
            this.itemAction.accept(new Pair<>(world, stack));
        }
    }

}
