package dev.jaxydog.astral.content.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public abstract class AstralPower extends Power {

    public AstralPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

}
