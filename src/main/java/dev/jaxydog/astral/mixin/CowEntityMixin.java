/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2024 Icepenguin
 * Copyright © 2024 RemasteredArch
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

package dev.jaxydog.astral.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.astral.content.item.AstralItems;
import dev.jaxydog.astral.utility.CowType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Implements pink cows and strawberry milking.
 *
 * @author Icepenguin
 * @since 1.7.0
 */
@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends PassiveEntityMixin {

    // spiders 🕷 🕸

    /**
     * Creates a new instance of this mixin.
     *
     * @param entityType The entity type.
     * @param world The current world.
     *
     * @since 1.7.0
     */
    protected CowEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Returns whether a cow is of the given type.
     *
     * @param entity The entity.
     * @param type The cow type.
     *
     * @return Whether a cow is of the given type.
     *
     * @since 1.7.0
     */
    @Unique
    private boolean isCowType(LivingEntity entity, CowType type) {
        return entity.getDataTracker().get(CowType.COW_TYPE).equals(type.asString());
    }

    /**
     * Returns whether this cow is of the given type.
     *
     * @param type The cow type.
     *
     * @return Whether this cow is of the given type.
     *
     * @since 1.7.0
     */
    @Unique
    private boolean isCowType(CowType type) {
        return this.isCowType(this, type);
    }

    /**
     * Returns the entity's cow type.
     *
     * @param entity The entity.
     *
     * @return The entity's cow type.
     *
     * @since 1.7.0
     */
    @Unique
    private CowType getCowType(LivingEntity entity) {
        return CowType.fromName(entity.getDataTracker().get(CowType.COW_TYPE));
    }

    /**
     * Returns this entity's cow type.
     *
     * @return This entity's cow type.
     *
     * @since 1.7.0
     */
    @Unique
    private CowType getCowType() {
        return this.getCowType(this);
    }

    /**
     * Sets an entity's cow type.
     *
     * @param entity The entity.
     * @param type The cow type.
     *
     * @since 1.7.0
     */
    @Unique
    private void setCowType(LivingEntity entity, CowType type) {
        entity.getDataTracker().set(CowType.COW_TYPE, type.asString());
    }

    /**
     * Sets this entity's cow type.
     *
     * @param type The cow type.
     *
     * @since 1.7.0
     */
    @Unique
    private void setCowType(CowType type) {
        this.setCowType(this, type);
    }

    /**
     * Randomly determines whether this entity should be a pink cow.
     *
     * @param cow The cow entity.
     *
     * @since 1.7.0
     */
    @Override
    protected void pinkCowRng(CowEntity cow) {
        if (this.getRandom().nextInt(124) == 0) {
            this.setCowType(CowType.PINK);
        }
    }

    /**
     * Allows a pink cow to be milked for pink milk.
     *
     * @param player The player entity.
     * @param hand The hand interacting with this entity.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.7.0
     */
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void pinkMilk(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> callbackInfo) {
        final ItemStack stack = player.getStackInHand(hand);

        if (!stack.isOf(Items.GLASS_BOTTLE) || this.isBaby() || !this.isCowType(CowType.PINK)) return;

        final ItemStack result = AstralItems.STRAWBERRY_MILK.getDefaultStack();
        final ItemStack exchanged = ItemUsage.exchangeStack(stack, player, result, false);

        player.setStackInHand(hand, exchanged);
        player.playSound(SoundEvents.ENTITY_COW_MILK, 1F, 1F);

        callbackInfo.setReturnValue((ActionResult.success(player.getWorld().isClient())));
    }

    /**
     * Creates a possibly pink child.
     *
     * @param baby The baby entity.
     * @param world The world.
     * @param entity The entity.
     *
     * @return The baby entity.
     *
     * @since 1.7.0
     */
    @ModifyReturnValue(
        method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/CowEntity;",
        at = @At("RETURN")
    )
    private @Nullable CowEntity createVariedChild(
        CowEntity baby, @Local(argsOnly = true) ServerWorld world, @Local(argsOnly = true) PassiveEntity entity
    ) {
        if (baby == null) return null;

        final CowType typeA = this.getCowType();
        final CowType typeB = this.getCowType(entity);
        final boolean usePink;

        if (typeA.equals(typeB)) {
            usePink = typeA.equals(CowType.PINK) ? random.nextBoolean() : random.nextInt(124) == 0;
        } else {
            usePink = random.nextInt(2) == 0;
        }

        this.setCowType(baby, usePink ? CowType.PINK : CowType.BROWN);

        return baby;
    }

}
