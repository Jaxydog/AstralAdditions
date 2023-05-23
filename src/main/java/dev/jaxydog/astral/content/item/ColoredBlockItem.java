package dev.jaxydog.astral.content.item;

import dev.jaxydog.astral.utility.DyeableHelper;
import dev.jaxydog.astral.utility.Registerable;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

/** An extension of a regular custom block item that provides color support */
public abstract class ColoredBlockItem extends CustomBlockItem implements Registerable.Client {

	public <B extends Block> ColoredBlockItem(String rawId, B block, Settings settings) {
		super(rawId, block, settings);
	}

	public <B extends Block & Registerable> ColoredBlockItem(
		String rawId,
		DyeColor color,
		DyeableHelper<B> blocks,
		Settings settings
	) {
		super(rawId, blocks.get(color), settings);
	}

	/** Returns the color that the item stack should render at the given index */
	protected abstract int getColor(ItemStack stack, int index);

	@Override
	public void registerClient() {
		Client.super.registerClient();
		// This just ensures that registered block items are actually colored
		// Calling this on the server will crash it, as the `ColorProviderRegistry` doesn't exist on that environment
		ColorProviderRegistry.ITEM.register(this::getColor, this);
	}
}