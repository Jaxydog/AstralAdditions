package dev.jaxydog.astral.content.trinket;

import com.mojang.datafixers.util.Function3;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import dev.jaxydog.astral.register.Registered;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Wraps a trinket predicate for automatic registration.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public class AstralTrinketPredicate implements Registered.Common {

    /**
     * The predicate's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;
    /**
     * The inner predicate function.
     *
     * @since 2.0.0
     */
    private final Function3<ItemStack, SlotReference, LivingEntity, TriState> predicate;

    /**
     * Creates a new predicate wrapper.
     *
     * @param rawId The predicate's identifier path.
     * @param predicate The predicate.
     *
     * @since 2.0.0
     */
    public AstralTrinketPredicate(String rawId, Function3<ItemStack, SlotReference, LivingEntity, TriState> predicate) {
        this.path = rawId;
        this.predicate = predicate;
    }

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        TrinketsApi.registerTrinketPredicate(this.getRegistryId(), this.predicate);
    }

}
