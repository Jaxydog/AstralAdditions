package dev.jaxydog.astral.content.power;

import dev.jaxydog.astral.content.power.custom.*;
import dev.jaxydog.astral.register.ContentRegistrar;

/** Contains definitions for all custom powers */
@SuppressWarnings("unused")
public final class AstralPowers extends ContentRegistrar {

    public static final AstralPowerFactory<TickingCooldownPower> TICKING_COOLDOWN = TickingCooldownPower.getCooldownFactory();

    public static final AstralPowerFactory<ActionOnKeyPower> ACTION_ON_KEY = ActionOnKeyPower.getActionFactory();

    public static final AstralPowerFactory<ActionOnSprayPower> ACTION_ON_SPRAY = ActionOnSprayPower.getFactory();

    public static final AstralPowerFactory<ActionWhenSprayedPower> ACTION_WHEN_SPRAYED = ActionWhenSprayedPower.getFactory();

    public static final AstralPowerFactory<ModifyScalePower> MODIFY_SCALE = ModifyScalePower.getFactory();

}
