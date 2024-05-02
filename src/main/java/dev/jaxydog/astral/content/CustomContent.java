package dev.jaxydog.astral.content;

import dev.jaxydog.astral.content.block.AstralBlocks;
import dev.jaxydog.astral.content.data.AstralData;
import dev.jaxydog.astral.content.effect.AstralPotions;
import dev.jaxydog.astral.content.effect.AstralStatusEffects;
import dev.jaxydog.astral.content.item.AstralItems;
import dev.jaxydog.astral.content.item.group.AstralItemGroups;
import dev.jaxydog.astral.content.power.AstralActions;
import dev.jaxydog.astral.content.power.AstralConditions;
import dev.jaxydog.astral.content.power.AstralPowers;
import dev.jaxydog.astral.content.sound.AstralSoundEvents;
import dev.jaxydog.astral.content.trinket.AstralTrinketPredicates;
import dev.jaxydog.astral.register.ContentRegistrar;
import dev.jaxydog.astral.register.IgnoreRegistration;
import dev.jaxydog.astral.register.RegistrationPriority;

/** Contains all instances of defined content container classes */
@SuppressWarnings("unused")
public final class CustomContent extends ContentRegistrar {

    /** Stores the custom content container instance */
    @IgnoreRegistration
    public static final CustomContent INSTANCE = new CustomContent();

    // Origins extensions
    public static final AstralActions ACTIONS = new AstralActions();
    public static final AstralConditions CONDITIONS = new AstralConditions();
    @RegistrationPriority(1)
    public static final AstralData DATA = new AstralData();
    public static final AstralPowers POWERS = new AstralPowers();

    // Vanilla extensions
    @RegistrationPriority(2)
    public static final AstralBlocks BLOCKS = new AstralBlocks();
    public static final AstralGamerules GAMERULES = new AstralGamerules();
    @RegistrationPriority(1)
    public static final AstralItemGroups ITEM_GROUPS = new AstralItemGroups();
    @RegistrationPriority(1)
    public static final AstralItems ITEMS = new AstralItems();
    public static final AstralPotions POTIONS = new AstralPotions();
    public static final AstralSoundEvents SOUND_EVENTS = new AstralSoundEvents();
    @RegistrationPriority(1)
    public static final AstralStatusEffects STATUS_EFFECTS = new AstralStatusEffects();

    // Miscellaneous
    public static final AstralTrinketPredicates TRINKET_SLOTS = new AstralTrinketPredicates();

}
