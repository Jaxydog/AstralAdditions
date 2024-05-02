package dev.jaxydog.astral.content;

import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.register.IgnoreRegistration;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.IntRule;
import net.minecraft.world.GameRules.Key;

/**
 * Contains definitions of all modded-in gamerules.
 *
 * @author Jaxydog
 * @since 1.1.0
 */
public final class AstralGamerules extends ContentRegistrar {

    /**
     * The gamerule that toggles challenge scaling on or off.
     *
     * @since 1.1.0
     */
    @IgnoreRegistration
    public static final Key<BooleanRule> CHALLENGE_ENABLED = GameRuleRegistry.register("challengeEnabled",
        Category.MOBS,
        GameRuleFactory.createBooleanRule(true)
    );

    /**
     * The gamerule that configures the challenge scaling chunk step size.
     *
     * @since 1.1.0
     */
    @IgnoreRegistration
    public static final Key<IntRule> CHALLENGE_CHUNK_STEP = GameRuleRegistry.register("challengeChunkStep",
        Category.MOBS,
        GameRuleFactory.createIntRule(16)
    );

    /**
     * The gamerule that configures the amount of attack points that are added for each chunk step.
     *
     * @since 1.1.0
     */
    @IgnoreRegistration
    public static final Key<DoubleRule> CHALLENGE_ATTACK_ADDITIVE = GameRuleRegistry.register("challengeAttackAdditive",
        Category.MOBS,
        GameRuleFactory.createDoubleRule(1D)
    );

    /**
     * The gamerule that configures the amount of health points that are added for each chunk step.
     *
     * @since 1.1.0
     */
    @IgnoreRegistration
    public static final Key<DoubleRule> CHALLENGE_HEALTH_ADDITIVE = GameRuleRegistry.register("challengeHealthAdditive",
        Category.MOBS,
        GameRuleFactory.createDoubleRule(1D)
    );

    /**
     * The gamerule that determines whether challenge scaling uses the world's spawn or 0, 0.
     *
     * @since 1.1.0
     */
    @IgnoreRegistration
    public static final Key<BooleanRule> CHALLENGE_USE_WORLDSPAWN = GameRuleRegistry.register("challengeUseWorldspawn",
        Category.MOBS,
        GameRuleFactory.createBooleanRule(true)
    );

    /**
     * The gamerule that configures the chance of receiving a reward from currency conversions.
     *
     * @since 1.1.0
     */
    @IgnoreRegistration
    public static final Key<DoubleRule> CURRENCY_REWARD_CHANCE = GameRuleRegistry.register("currencyRewardChance",
        Category.DROPS,
        GameRuleFactory.createDoubleRule(0.1D)
    );

}
