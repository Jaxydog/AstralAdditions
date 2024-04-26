package dev.jaxydog.astral.content.block;

import dev.jaxydog.astral.content.block.custom.DyedAmethystBlock;
import dev.jaxydog.astral.content.block.custom.DyedAmethystClusterBlock;
import dev.jaxydog.astral.content.block.custom.DyedAmethystClusterBlock.Type;
import dev.jaxydog.astral.content.block.custom.DyedBuddingAmethystBlock;
import dev.jaxydog.astral.content.block.custom.RandomizerBlock;
import dev.jaxydog.astral.datagen.TagGenerator;
import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.register.DyeMap;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Blocks;

/** Contains definitions for all custom blocks */
public final class AstralBlocks extends ContentRegistrar {

    public static final DyeMap<DyedAmethystBlock> DYED_AMETHYST_BLOCKS = new DyeMap<>("amethyst_block",
        (rawId, color) -> new DyedAmethystBlock(rawId, Settings.copy(Blocks.AMETHYST_BLOCK), color)
    );
    public static final DyeMap<DyedBuddingAmethystBlock> DYED_BUDDING_AMETHYST_BLOCKS = new DyeMap<>("budding_amethyst",
        (rawId, color) -> new DyedBuddingAmethystBlock(rawId, Settings.copy(Blocks.BUDDING_AMETHYST), color)
    );
    public static final DyeMap<DyedAmethystClusterBlock> DYED_AMETHYST_CLUSTERS = new DyeMap<>("amethyst_cluster",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.AMETHYST_CLUSTER).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.CLUSTER);
        }
    );
    public static final DyeMap<DyedAmethystClusterBlock> DYED_LARGE_AMETHYST_BUDS = new DyeMap<>("large_amethyst_bud",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.LARGE_AMETHYST_BUD).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.LARGE_BUD);
        }
    );
    public static final DyeMap<DyedAmethystClusterBlock> DYED_MEDIUM_AMETHYST_BUDS = new DyeMap<>("medium_amethyst_bud",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.MEDIUM_AMETHYST_BUD).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.MEDIUM_BUD);
        }
    );
    public static final DyeMap<DyedAmethystClusterBlock> DYED_SMALL_AMETHYST_BUDS = new DyeMap<>("small_amethyst_bud",
        (rawId, color) -> {
            final Settings settings = Settings.copy(Blocks.SMALL_AMETHYST_BUD).mapColor(color);

            return new DyedAmethystClusterBlock(rawId, settings, color, Type.SMALL_BUD);
        }
    );

    public static final RandomizerBlock RANDOMIZER = new RandomizerBlock("randomizer",
        Settings.copy(Blocks.IRON_BLOCK)
    );

    @Override
    public void generate() {
        super.generate();

        TagGenerator.getInstance().generate(DyedAmethystBlock.AMETHYST_BLOCKS, b -> b.add(Blocks.AMETHYST_BLOCK));
        TagGenerator.getInstance()
            .generate(DyedBuddingAmethystBlock.BUDDING_AMETHYSTS, b -> b.add(Blocks.BUDDING_AMETHYST));
        TagGenerator.getInstance()
            .generate(DyedAmethystClusterBlock.AMETHYST_CLUSTERS, b -> b.add(Blocks.AMETHYST_CLUSTER));
        TagGenerator.getInstance()
            .generate(DyedAmethystClusterBlock.LARGE_AMETHYST_BUDS, b -> b.add(Blocks.LARGE_AMETHYST_BUD));
        TagGenerator.getInstance()
            .generate(DyedAmethystClusterBlock.MEDIUM_AMETHYST_BUDS, b -> b.add(Blocks.MEDIUM_AMETHYST_BUD));
        TagGenerator.getInstance()
            .generate(DyedAmethystClusterBlock.SMALL_AMETHYST_BUDS, b -> b.add(Blocks.SMALL_AMETHYST_BUD));
    }

}
