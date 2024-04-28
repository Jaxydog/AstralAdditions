package dev.jaxydog.astral.content.power;

import dev.jaxydog.astral.content.power.custom.*;
import dev.jaxydog.astral.register.ContentRegistrar;

/** Contains definitions for all custom powers */
@SuppressWarnings("unused")
public final class CustomPowers extends ContentRegistrar {

    public static final CustomPowerFactory<TickingCooldownPower> TICKING_COOLDOWN = TickingCooldownPower.getCooldownFactory();

    public static final CustomPowerFactory<ActionOnKeyPower> ACTION_ON_KEY = ActionOnKeyPower.getActionFactory();

    public static final CustomPowerFactory<ActionOnSprayPower> ACTION_ON_SPRAY = ActionOnSprayPower.getFactory();

    public static final CustomPowerFactory<ActionWhenSprayedPower> ACTION_WHEN_SPRAYED = ActionWhenSprayedPower.getFactory();

    public static final CustomPowerFactory<ModifyScalePower> MODIFY_SCALE = ModifyScalePower.getFactory();

}
