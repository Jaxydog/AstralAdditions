package dev.jaxydog.astral.mixin.challenge;

import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.astral.utility.ChallengeHelper;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.RamImpactTask;
import net.minecraft.entity.passive.GoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(RamImpactTask.class)
public abstract class RamImpactTaskMixin extends MultiTickTask<GoatEntity> {

    public RamImpactTaskMixin(
        Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState, int minRunTime, int maxRunTime
    ) {
        super(requiredMemoryState, minRunTime, maxRunTime);
    }

    @ModifyArg(
        method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/GoatEntity;J)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
        )
    )
    private float keepRunningArgsInject(float damage, @Local(argsOnly = true) GoatEntity entity) {
        if (!ChallengeHelper.shouldScale(entity)) return damage;

        final double additive = ChallengeHelper.getAttackAdditive(entity.getWorld());

        return damage + (float) ChallengeHelper.getScaledAdditive(entity, additive);
    }

}
