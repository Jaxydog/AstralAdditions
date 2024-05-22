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

package dev.jaxydog.astral.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.astral.content.trinket.AstralTrinketPredicates;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Properly override the head renderer when using the cosmetic helmet slot.
 *
 * @param <T> The entity type.
 * @param <M> The model type.
 *
 * @author Jaxydog
 * @since 1.6.0
 */
@Mixin(HeadFeatureRenderer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead> extends FeatureRenderer<T, M> {

    /**
     * Creates a new instance of this mixin.
     *
     * @param context The rendering context.
     *
     * @since 1.6.0
     */
    public HeadFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    /**
     * Replaces the item stack if applicable.
     *
     * @param equippedStack The equipped item stack.
     * @param entity The source entity.
     *
     * @return The replaced item stack.
     *
     * @since 1.6.0
     */
    @ModifyVariable(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
        at = @At("STORE")
    )
    private ItemStack cosmeticReplacer(ItemStack equippedStack, @Local(argsOnly = true) T entity) {
        final ItemStack cosmeticStack = AstralTrinketPredicates.getCosmeticHelmet(entity);

        if (cosmeticStack.isEmpty() || equippedStack.isIn(AstralTrinketPredicates.COSMETIC_HELMET_UNHIDEABLE)) {
            return equippedStack;
        } else {
            return cosmeticStack;
        }
    }

}
