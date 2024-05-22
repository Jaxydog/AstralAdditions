/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright © 2024 Icepenguin
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

import com.google.common.base.Suppliers;
import dev.jaxydog.astral.Astral;
import dev.jaxydog.astral.utility.CowType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.entity.CowEntityRenderer;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

/**
 * Overrides the cow's texture when it has other variants.
 *
 * @author Icepenguin
 * @since 1.7.0
 */
@Mixin(CowEntityRenderer.class)
public class CowEntityRendererMixin {

    /**
     * The pink cow texture.
     *
     * @since 1.7.0
     */
    @Unique
    private static final Identifier PINK_TEXTURE_DEFAULT = Astral.getId("textures/entity/cow/pink_cow.png");
    /**
     * The pink cow texture (with ETF installed).
     *
     * @since 1.7.2
     */
    @Unique
    private static final Identifier PINK_TEXTURE_ETF = Astral.getId("textures/entity/cow/pink_cow_etf.png");
    /**
     * Supplies the current pink texture.
     *
     * @since 1.7.0
     */
    @Unique
    private static final Supplier<Identifier> PINK_TEXTURE = Suppliers.memoize(() -> {
        if (FabricLoader.getInstance().getModContainer("entity_texture_features").isPresent()) {
            return PINK_TEXTURE_ETF;
        } else {
            return PINK_TEXTURE_DEFAULT;
        }
    });

    /**
     * Overrides the cow's texture if necessary.
     *
     * @param cowEntity The cow entity.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.7.0
     */
    @Inject(
        method = "getTexture(Lnet/minecraft/entity/passive/CowEntity;)Lnet/minecraft/util/Identifier;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void maybePinkTexture(CowEntity cowEntity, CallbackInfoReturnable<Identifier> callbackInfo) {
        if (cowEntity.getDataTracker().get(CowType.COW_TYPE).equals(CowType.PINK.asString())) {
            callbackInfo.setReturnValue(PINK_TEXTURE.get());
        }
    }

}
