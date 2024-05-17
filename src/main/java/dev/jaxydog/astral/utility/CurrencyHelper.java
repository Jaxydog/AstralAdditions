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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.content.AstralGamerules;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * Provides types, fields, and methods for the mod's currency system.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
@NonExtendable
public interface CurrencyHelper {

    /**
     * The NBT key used to determine whether a given item stack may be exchanged.
     *
     * @since 2.0.0
     */
    String EXCHANGE_KEY = "Exchange";

    /**
     * Returns whether the given item stack may be automatically exchanged.
     * <p>
     * This is checked using the {@value EXCHANGE_KEY} NBT tag. If this tag is not present, this method will always
     * return true.
     *
     * @param stack The item stack.
     *
     * @return Whether the given item stack may be automatically exchanged.
     *
     * @since 2.0.0
     */
    static boolean canExchange(ItemStack stack) {
        final NbtCompound compound;

        if ((compound = stack.getNbt()) == null) return false;

        return !compound.contains(EXCHANGE_KEY) || compound.getBoolean(EXCHANGE_KEY);
    }

    /**
     * Drops rewards to the provided player entity.
     *
     * @param player The target player entity.
     * @param rolls The attempts at rolling a reward.
     *
     * @since 2.0.0
     */
    static void dropRewards(PlayerEntity player, int rolls) {
        if (Reward.REWARDS.isEmpty()) return;

        final Random random = player.getRandom();
        final double chance = player.getWorld().getGameRules().get(AstralGamerules.CURRENCY_REWARD_CHANCE).get();

        int count = 0;

        for (int iteration = 0; iteration < rolls; iteration += 1) {
            if (random.nextDouble() <= chance) count += 1;
        }

        final List<Reward> rewards = Reward.getRandom(count);
        final PlayerInventory inventory = player.getInventory();

        for (final Reward reward : rewards) {
            inventory.offerOrDrop(reward.getItem().getDefaultStack());
        }
    }

    /**
     * Automatically exchanges currency items within a player's inventory.
     *
     * @param player The target player entity.
     *
     * @since 2.0.0
     */
    static void tryExchange(PlayerEntity player) {
        if (player.getWorld().isClient()) return;

        // Collect a list of stacks within the inventory.
        final PlayerInventory inventory = player.getInventory();
        final List<ItemStack> stacks = new ObjectArrayList<>();

        for (int slot = 0; slot < inventory.size(); slot += 1) {
            final ItemStack stack = inventory.getStack(slot);

            if (!stack.isEmpty()) stacks.add(stack);
        }

        // Find all stacks associated with currency units.
        final List<Pair<ItemStack, Unit>> units = stacks.stream()
            .flatMap(s -> Unit.UNITS.find(s).stream().map(u -> new Pair<>(s, u)))
            .toList();

        // Re-compute the stack array if the inventory was changed.
        if (tryExchangeUnits(player, units)) {
            stacks.clear();

            for (int slot = 0; slot < inventory.size(); slot += 1) {
                final ItemStack stack = inventory.getStack(slot);

                if (!stack.isEmpty()) stacks.add(stack);
            }
        }

        // Find all stacks associated with currency units.
        final List<Pair<ItemStack, Reward>> rewards = stacks.stream()
            .flatMap(s -> Reward.REWARDS.find(s).stream().map(r -> new Pair<>(s, r)))
            .toList();

        tryExchangeRewards(player, rewards);
    }

    /**
     * Automatically exchanges currency units within a player's inventory.
     *
     * @param player The target player entity.
     * @param pairs Pairs of item stacks, and their associated units.
     *
     * @return Whether the player's inventory was modified.
     *
     * @since 2.0.0
     */
    static boolean tryExchangeUnits(PlayerEntity player, List<Pair<ItemStack, Unit>> pairs) {
        final Map<Unit, Integer> counts = new Object2IntOpenHashMap<>(pairs.size());

        // Count all units, ignoring stacks that cannot be exchanged.
        for (final Pair<ItemStack, Unit> pair : pairs) {
            final ItemStack stack = pair.getLeft();
            final Unit unit = pair.getRight();

            if (canExchange(stack)) counts.merge(unit, stack.getCount(), Integer::sum);
        }

        final PlayerInventory inventory = player.getInventory();
        boolean changed = false;

        for (final Entry<Unit, Integer> entry : counts.entrySet()) {
            final Unit unit = entry.getKey();
            final int count = entry.getValue();

            if (count == 0) continue;

            final Optional<Entry<Identifier, Unit>> maybeNext = unit.next(true);

            if (maybeNext.isEmpty()) continue;

            // Calculate the price and total units crafted.
            final String thisNamespace = Unit.UNITS.getId(unit).map(Identifier::getNamespace).orElse(Astral.MOD_ID);
            final String nextNamespace = maybeNext.get().getKey().getNamespace();
            final Unit next = maybeNext.get().getValue();
            final int price;

            if (thisNamespace.equals(nextNamespace)) {
                price = next.value() / unit.value();
            } else if (unit.exchangeMap().containsKey(nextNamespace)) {
                price = next.value() / unit.exchangeMap().get(nextNamespace);
            } else {
                continue;
            }

            if (price == 0) continue;

            final int total = count / price;

            if (total == 0) continue;

            final ItemStack stack = next.getItem().getDefaultStack();

            stack.setCount(total);

            // Remove consumed items.
            inventory.remove(
                s -> s.getItem().equals(unit.getItem()) && canExchange(s),
                total * price,
                player.playerScreenHandler.getCraftingInput()
            );

            inventory.offerOrDrop(stack);

            if (next.dropRewards()) dropRewards(player, total);

            changed = true;
        }

        return changed;
    }

    /**
     * Automatically exchanges currency rewards within a player's inventory.
     *
     * @param player The target player entity.
     * @param pairs Pairs of item stacks, and their associated rewards.
     *
     * @return Whether the player's inventory was modified.
     *
     * @since 2.0.0
     */
    static boolean tryExchangeRewards(PlayerEntity player, List<Pair<ItemStack, Reward>> pairs) {
        final Map<Reward, Integer> counts = new Object2IntOpenHashMap<>(pairs.size());
        final List<Skeleton> craftableSkeletons = new ObjectArrayList<>();

        // Count all rewards, ignoring stacks that cannot be exchanged.
        for (final Pair<ItemStack, Reward> pair : pairs) {
            final ItemStack stack = pair.getLeft();
            final Reward reward = pair.getRight();

            if (canExchange(stack)) counts.merge(reward, stack.getCount(), Integer::sum);
        }

        // Grab a list of all craftable skeletons.
        for (final Skeleton skeleton : Skeleton.SKELETONS.values()) {
            if (!skeleton.hasRequirements(counts)) continue;

            craftableSkeletons.add(skeleton);
        }

        final Map<Reward, Integer> removed = new Object2IntOpenHashMap<>(counts.size());
        final Map<Skeleton, Integer> skeletons = new Object2IntOpenHashMap<>(craftableSkeletons.size());

        while (!craftableSkeletons.isEmpty()) {
            final List<Skeleton> uncraftable = new ObjectArrayList<>();

            for (final Skeleton skeleton : craftableSkeletons) {
                if (skeleton.hasRequirements(counts)) {
                    for (final Reward reward : skeleton.getRequirements()) {
                        removed.merge(reward, 1, Integer::sum);
                        counts.computeIfPresent(reward, (r, n) -> n - 1);
                    }

                    skeletons.merge(skeleton, 1, Integer::sum);
                } else {
                    uncraftable.add(skeleton);
                }
            }

            craftableSkeletons.removeAll(uncraftable);
        }

        final PlayerInventory inventory = player.getInventory();
        boolean modified = false;

        // Remove all consumed reward items.
        for (final Entry<Reward, Integer> entry : removed.entrySet()) {
            final Reward reward = entry.getKey();
            final int count = entry.getValue();
            final int n = inventory.remove(
                s -> s.getItem().equals(reward.getItem()) && canExchange(s),
                count,
                player.playerScreenHandler.getCraftingInput()
            );

            if (n > 0) modified = true;
        }

        // Give all produced skeleton items.
        for (final Entry<Skeleton, Integer> entry : skeletons.entrySet()) {
            final Skeleton skeleton = entry.getKey();
            final int count = entry.getValue();
            final ItemStack stack = skeleton.getItem().getDefaultStack();

            stack.setCount(count);
            inventory.offerOrDrop(stack);

            modified = true;
        }

        return modified;
    }

    /**
     * A type that has an associated item representation.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    sealed interface ItemRepresentable {

        /**
         * Returns the associated item identifier, or {@code minecraft:air} if the referenced identifier is invalid.
         *
         * @return The associated item identifier, or {@code minecraft:air} if the referenced identifier is invalid.
         *
         * @since 2.0.0
         */
        Identifier getItemId();

        /**
         * Returns the associated item, or {@link net.minecraft.item.Items#AIR} if the referenced item is invalid.
         *
         * @return The associated item, or {@link net.minecraft.item.Items#AIR} if the referenced item is invalid.
         *
         * @since 2.0.0
         */
        default Item getItem() {
            return Registries.ITEM.get(this.getItemId());
        }

    }

    /**
     * A map containing values of a specified currency type.
     *
     * @param <T> The currency type.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    final class CurrencyMap<T extends ItemRepresentable> {

        /**
         * The inner identifier-to-object map.
         *
         * @since 2.0.0
         */
        private final Map<Identifier, T> inner = new Object2ObjectOpenHashMap<>();

        /**
         * Sets the value at the given identifier.
         *
         * @param identifier The identifier.
         * @param value The currency value.
         *
         * @return The new size of the map.
         *
         * @since 2.0.0
         */
        public int set(Identifier identifier, T value) {
            this.inner.put(identifier, value);

            return this.inner.size();
        }

        /**
         * Sets the values at the given identifiers.
         *
         * @param values The value map.
         *
         * @return The new size of the map.
         *
         * @since 2.0.0
         */
        public int set(Map<Identifier, T> values) {
            this.inner.putAll(values);

            return this.inner.size();
        }

        /**
         * Clears the map.
         *
         * @since 2.0.0
         */
        public void clear() {
            this.inner.clear();
        }

        /**
         * Loads the given values into this map.
         *
         * @param values The value map.
         *
         * @return The new size of the map.
         *
         * @since 2.0.0
         */
        public int load(Map<Identifier, T> values) {
            this.clear();

            return this.set(values);
        }

        /**
         * Finds and returns the value associated with the provided item identifier.
         *
         * @param identifier The item's identifier.
         *
         * @return The value associated with the provided item identifier.
         *
         * @since 2.0.0
         */
        public Optional<T> find(Identifier identifier) {
            return this.inner.values().stream().filter(t -> t.getItemId().equals(identifier)).findFirst();
        }

        /**
         * Finds and returns the value associated with the provided item.
         *
         * @param item The item.
         *
         * @return The value associated with the provided item.
         *
         * @since 2.0.0
         */
        public Optional<T> find(Item item) {
            return this.find(Registries.ITEM.getId(item));
        }

        /**
         * Finds and returns the value associated with the provided item stack.
         *
         * @param stack The item stack.
         *
         * @return The value associated with the provided item stack.
         *
         * @since 2.0.0
         */
        public Optional<T> find(ItemStack stack) {
            return this.find(stack.getItem());
        }

        /**
         * Finds and returns the identifier of the provided value.
         *
         * @param value The value.
         *
         * @return The identifier of the provided value.
         *
         * @since 2.0.0
         */
        public Optional<Identifier> getId(T value) {
            return this.inner.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .map(Entry::getKey);
        }

        /**
         * Returns the total number of entries within the map.
         *
         * @return The total number of entries within the map.
         *
         * @since 2.0.0
         */
        public int size() {
            return this.inner.size();
        }

        /**
         * Returns whether the map is empty.
         *
         * @return Whether the map is empty.
         *
         * @since 2.0.0
         */
        public boolean isEmpty() {
            return this.inner.isEmpty();
        }

        /**
         * Returns a set containing the registered values and their identifiers.
         *
         * @return A set containing the registered values and their identifiers.
         *
         * @since 2.0.0
         */
        public Set<Entry<Identifier, T>> entrySet() {
            return this.inner.entrySet();
        }

        /**
         * Returns a set containing the identifiers within this map.
         *
         * @return A set containing the identifiers within this map.
         *
         * @since 2.0.0
         */
        public Set<Identifier> keySet() {
            return this.inner.keySet();
        }

        /**
         * Returns a collection containing the values within this map.
         *
         * @return A collection containing the values within this map.
         *
         * @since 2.0.0
         */
        public Collection<T> values() {
            return this.inner.values();
        }

    }

    /**
     * A base unit of currency.
     *
     * @param itemId The representing item's identifier.
     * @param value The value of this currency.
     * @param dropRewards Whether to drop rewards when automatically exchanging.
     * @param exchangeMap A map containing exchange rates for other namespaces.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    record Unit(
        Identifier itemId, int value, boolean dropRewards, Map<String, Integer> exchangeMap
    ) implements ItemRepresentable, Comparable<Unit> {

        /**
         * A map containing all currently active currency units.
         *
         * @since 2.0.0
         */
        public static final CurrencyMap<Unit> UNITS = new CurrencyMap<>();

        /**
         * A comparator that compares by a unit's value.
         *
         * @since 2.0.0
         */
        private static final Comparator<Unit> COMPARATOR = Comparator.comparingInt(Unit::value);

        /**
         * Parses a JSON object and creates a new {@link Unit} from the resolved data.
         *
         * @param namespace The unit's namespace.
         * @param itemId The representing item's identifier.
         * @param object The JSON object.
         *
         * @return A new unit.
         *
         * @throws JsonSyntaxException If a field is invalid or missing from the given object.
         * @since 2.0.0
         */
        public static Unit parse(String namespace, Identifier itemId, JsonObject object) throws JsonSyntaxException {
            final int value = JsonHelper.getInt(object, "value");

            if (value <= 0) throw new JsonSyntaxException("Expected a positive non-zero value");

            final boolean dropRewards = JsonHelper.getBoolean(object, "drops", false);
            final Map<String, Integer> exchanges = new Object2IntOpenHashMap<>();

            if (object.has("exchanges")) {
                final JsonObject conversions = JsonHelper.getObject(object, "exchanges");

                for (final Entry<String, JsonElement> entry : conversions.entrySet()) {
                    if (entry.getKey().equals(namespace)) continue;
                    if (!JsonHelper.isNumber(entry.getValue())) continue;

                    final int effectiveValue = entry.getValue().getAsInt();

                    exchanges.put(entry.getKey(), effectiveValue);
                }
            }

            return new Unit(itemId, value, dropRewards, exchanges);
        }

        /**
         * Returns the next unit of currency by value.
         *
         * @param exactMultiple Whether the value of the unit returned should be an exact multiple of this unit's
         * value.
         *
         * @return The next unit by value.
         *
         * @since 2.0.0
         */
        public Optional<Entry<Identifier, Unit>> next(boolean exactMultiple) {
            final String thisNamespace = UNITS.getId(this).map(Identifier::getNamespace).orElse(Astral.MOD_ID);

            return UNITS.entrySet().stream().flatMap(entry -> {
                final Identifier identifier = entry.getKey();
                final Unit unit = entry.getValue();
                final String nextNamespace = identifier.getNamespace();

                // If these are equal, we don't need to do any conversions.
                if (thisNamespace.equals(nextNamespace)) {
                    // Ignore values that are not "next".
                    if (entry.getValue().value() <= this.value()) return Stream.empty();

                    // Skip this check if an exact multiple is not required.
                    if (!exactMultiple) return Stream.of(entry);

                    // Ensure the returned entry's value is perfectly divisible.
                    return Stream.of(entry).filter(e -> unit.value() % this.value() == 0);
                }
                // Make sure we use the converted value if this is from a different namespace.
                if (this.exchangeMap().containsKey(nextNamespace)) {
                    // Compute the actual value within this namespace.
                    final int value = this.exchangeMap().get(nextNamespace) * unit.value();

                    // Ignore values that are not "next".
                    if (value <= this.value()) return Stream.empty();

                    // Skip this check if an exact multiple is not required.
                    if (!exactMultiple) return Stream.of(entry);

                    // Ensure the returned entry's value is perfectly divisible.
                    return Stream.of(entry).filter(e -> value % this.value() == 0);
                }

                // Assume the currencies are incompatible.
                return Stream.empty();
            }).min(Comparator.comparingInt(entry -> {
                final Identifier identifier = entry.getKey();
                final Unit unit = entry.getValue();
                final String nextNamespace = identifier.getNamespace();

                // Make sure we use the converted value if this is from a different namespace.
                if (this.exchangeMap().containsKey(nextNamespace)) {
                    // Compute the actual value within this namespace.
                    return this.exchangeMap().get(nextNamespace) * unit.value();
                }

                // If these are equal, we don't need to do any conversions.
                if (thisNamespace.equals(nextNamespace)) return unit.value();

                // Otherwise, something went wrong and this should be ignored.
                return Integer.MAX_VALUE;
            }));
        }

        @Override
        public Identifier getItemId() {
            if (Registries.ITEM.containsId(this.itemId())) {
                return this.itemId();
            } else {
                return Registries.ITEM.getDefaultId();
            }
        }

        @Override
        public int compareTo(@NotNull Unit o) {
            return COMPARATOR.compare(this, o);
        }

    }

    /**
     * An item given as a reward for converting currency.
     *
     * @param itemId The representing item's identifier.
     * @param weight The randomness weight used for reward drops.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    record Reward(Identifier itemId, int weight) implements ItemRepresentable {

        /**
         * A map containing all currently active currency rewards.
         *
         * @since 2.0.0
         */
        public static final CurrencyMap<Reward> REWARDS = new CurrencyMap<>();

        /**
         * Parses a JSON object and creates a new {@link Reward} from the resolved data.
         *
         * @param itemId The representing item's identifier.
         * @param object The JSON object.
         *
         * @return A new reward.
         *
         * @throws JsonSyntaxException If a field is invalid or missing from the given object.
         * @since 2.0.0
         */
        public static Reward parse(Identifier itemId, JsonObject object) throws JsonSyntaxException {
            final int weight = JsonHelper.getInt(object, "weight");

            if (weight <= 0) throw new JsonSyntaxException("Expected a positive non-zero weight");

            return new Reward(itemId, weight);
        }

        /**
         * Returns a list of randomly generated rewards.
         *
         * @param count The number of rewards to generate.
         *
         * @return A list of randomly generated rewards.
         *
         * @since 2.0.0
         */
        public static List<Reward> getRandom(int count) {
            final int elements = Math.max(count, REWARDS.size());
            final List<Reward> output = new ObjectArrayList<>(elements);
            final WeightedList<Reward> rewards = new WeightedList<>();

            REWARDS.values().forEach(r -> rewards.add(r, r.weight()));

            // Allows for duplicate elements, unlike `.limit(elements).toList()`.
            for (int iteration = 0; iteration < elements; iteration += 1) {
                rewards.shuffle().stream().findFirst().ifPresent(output::add);
            }

            return output;
        }

        @Override
        public Identifier getItemId() {
            if (Registries.ITEM.containsId(this.itemId())) {
                return this.itemId();
            } else {
                return Registries.ITEM.getDefaultId();
            }
        }

    }

    /**
     * An item created using multiple reward items.
     *
     * @param itemId The representing item's identifier.
     * @param requires A list of required rewards to create this skeleton.
     *
     * @author Jaxydog
     * @since 2.0.0
     */
    record Skeleton(Identifier itemId, List<Identifier> requires) implements ItemRepresentable {

        /**
         * A map containing all currently active currency skeletons.
         *
         * @since 2.0.0
         */
        public static final CurrencyMap<Skeleton> SKELETONS = new CurrencyMap<>();

        /**
         * Parses a JSON object and creates a new {@link Skeleton} from the resolved data.
         *
         * @param itemId The representing item's identifier.
         * @param object The JSON object.
         *
         * @return A new reward.
         *
         * @throws JsonSyntaxException If a field is invalid or missing from the given object.
         * @since 2.0.0
         */
        public static Skeleton parse(Identifier itemId, JsonObject object) throws JsonSyntaxException {
            // Initialize with space for at least two items.
            final List<Identifier> requires = new ObjectArrayList<>(2);
            final JsonArray array = JsonHelper.getArray(object, "cost");

            for (final JsonElement element : array) {
                final String string = JsonHelper.asString(element, "cost");
                final Identifier identifier = Identifier.tryParse(string);

                if (Objects.isNull(identifier)) {
                    throw new JsonSyntaxException("Invalid identifier '%s'".formatted(string));
                }

                requires.add(identifier);
            }

            // Fail to generate if the requirements list is empty. This prevents infinite loops.
            if (requires.isEmpty()) throw new JsonSyntaxException("Expected a non-empty cost array");

            return new Skeleton(itemId, requires);
        }

        /**
         * Returns a list of required reward entries.
         * <p>
         * If a requirement's identifier is not found it will be ignored.
         *
         * @return A list of required rewards.
         *
         * @since 2.0.0
         */
        public List<Reward> getRequirements() {
            return this.requires().stream().flatMap(i -> Reward.REWARDS.find(i).stream()).toList();
        }

        /**
         * Returns whether the provided count map contains the required number of rewards to craft this skeleton.
         *
         * @param rewardCounts A map containing the number of each held reward.
         *
         * @return Whether the provided count map contains the required number of rewards to craft this skeleton.
         *
         * @since 2.0.0
         */
        public boolean hasRequirements(Map<Reward, Integer> rewardCounts) {
            // Calculate the total number of requirements.
            final Map<Reward, Integer> requirementCounts = new Object2IntOpenHashMap<>(rewardCounts.size());

            for (final Reward reward : this.getRequirements()) {
                requirementCounts.merge(reward, 1, Integer::sum);
            }

            // Ensure there's enough of each reward.
            return requirementCounts.entrySet().stream().allMatch(entry -> {
                if (!rewardCounts.containsKey(entry.getKey())) return false;

                return rewardCounts.get(entry.getKey()) >= entry.getValue();
            });
        }

        @Override
        public Identifier getItemId() {
            if (Registries.ITEM.containsId(this.itemId())) {
                return this.itemId();
            } else {
                return Registries.ITEM.getDefaultId();
            }
        }

    }

}
