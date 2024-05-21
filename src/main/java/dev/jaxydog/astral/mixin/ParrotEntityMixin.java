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

package dev.jaxydog.astral.mixin;

import dev.jaxydog.astral.Astral;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allows the player to heal parrots with seed items.
 *
 * @author Jaxydog
 * @since 1.0.0
 */
@Mixin(ParrotEntity.class)
public abstract class ParrotEntityMixin {

    /**
     * An item tag that determines whether an item may be fed to a parrot.
     *
     * @since 1.4.2
     */
    @Unique
    private static final TagKey<Item> PARROT_FEED = TagKey.of(RegistryKeys.ITEM, Astral.getId("parrot_feed"));

    /**
     * Allows a player to feed and heal a tamed parrot with items defined by the {@link #PARROT_FEED} item tag.
     *
     * @param player The player.
     * @param hand The hand being used to interact with this mob.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.0.0
     */
    @SuppressWarnings("UnreachableCode")
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void interactMobInject(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        final ItemStack stack = player.getStackInHand(hand);
        final ParrotEntity self = (ParrotEntity) (Object) this;
        final World world = player.getWorld();
        final boolean missingHealth = self.getHealth() < self.getMaxHealth();
        final boolean canFeed = stack.isIn(PARROT_FEED);

        if (world.isClient || !self.isTamed() || !canFeed || !missingHealth) return;

        if (!self.isSilent()) {
            final float pitchModifier = self.getRandom().nextFloat() - self.getRandom().nextFloat();

            self.playSound(SoundEvents.ENTITY_PARROT_EAT, 1F, 1F + (pitchModifier * 0.2F));
        }

        if (!player.getAbilities().creativeMode) stack.decrement(1);

        self.getWorld().sendEntityStatus(self, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
        self.heal(1);

        callbackInfo.setReturnValue(ActionResult.SUCCESS);
    }

}
