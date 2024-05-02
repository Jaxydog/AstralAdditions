package dev.jaxydog.astral.content.trinket;

import com.mojang.datafixers.util.Function3;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import dev.jaxydog.astral.register.Registered;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class AstralTrinketPredicate implements Registered.Common {

    private final String path;
    private final Function3<ItemStack, SlotReference, LivingEntity, TriState> predicate;

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
