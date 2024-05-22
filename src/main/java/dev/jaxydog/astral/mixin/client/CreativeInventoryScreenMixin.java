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

import dev.jaxydog.astral.utility.injected.AstralItemGroup;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows for animated item group icons.
 *
 * @author Jaxydog
 * @since 1.7.0
 */
@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeScreenHandler> {

    /**
     * The cached animation delta value.
     *
     * @since 1.7.0
     */
    @Unique
    private float cachedDelta = 0F;

    /**
     * Creates a new instance of this mixin.
     *
     * @param screenHandler The screen handler instance.
     * @param playerInventory The player's inventory.
     * @param text The screen text.
     *
     * @since 1.7.0
     */
    public CreativeInventoryScreenMixin(
        CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text
    ) {
        super(screenHandler, playerInventory, text);
    }

    /**
     * Captures the animation delta.
     *
     * @param context The rendering context.
     * @param delta The current delta.
     * @param mouseX The mouse's X position.
     * @param mouseY The mouse's Y position.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.7.0
     */
    @Inject(method = "drawBackground", at = @At("HEAD"))
    private void captureDelta(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        this.cachedDelta = delta;
    }

    /**
     * Reset the animation delta back to 0.
     *
     * @param context The rendering context.
     * @param delta The current delta.
     * @param mouseX The mouse's X position.
     * @param mouseY The mouse's Y position.
     * @param callbackInfo The injection callback information.
     *
     * @since 1.7.0
     */
    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void releaseDelta(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        this.cachedDelta = 0F;
    }

    /**
     * Prefer the injected {@link AstralItemGroup#astral$getIcon(float)} method over the original.
     *
     * @param instance The item group instance.
     *
     * @return The current icon.
     *
     * @since 1.7.0
     */
    @SuppressWarnings("RedundantCast")
    @Redirect(
        method = "renderTabIcon", at = @At(
        value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;getIcon()Lnet/minecraft/item/ItemStack;"
    )
    )
    private ItemStack redirectGetIcon(ItemGroup instance) {
        return ((AstralItemGroup) instance).astral$getIcon(this.cachedDelta);
    }

}
