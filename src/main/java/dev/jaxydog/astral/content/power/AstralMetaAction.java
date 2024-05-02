package dev.jaxydog.astral.content.power;

import dev.jaxydog.astral.register.Registered;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.calio.data.SerializableDataType;

/**
 * An abstract class for easily implementing custom Origins actions with multiple expected data types.
 * <p>
 * This type is automatically registered.
 *
 * @author Jaxydog
 * @since 2.0.0
 */
public abstract class AstralMetaAction implements Registered.Common {

    /**
     * The action's identifier path used within the registration system.
     *
     * @since 2.0.0
     */
    private final String path;

    /**
     * Creates a new action.
     *
     * @param path The action's identifier path.
     *
     * @since 2.0.0
     */
    public AstralMetaAction(String path) {
        this.path = path;
    }

    /**
     * Executes the action.
     *
     * @param data The action's associated data.
     * @param value The value to execute the action on.
     *
     * @since 2.0.0
     */
    public abstract <T> void execute(Instance data, T value);

    /**
     * Returns the action's associated factory.
     *
     * @return The action's associated factory.
     *
     * @since 2.0.0
     */
    public abstract <T> AstralActionFactory<T> factory(SerializableDataType<ActionFactory<T>.Instance> type);

    @Override
    public String getRegistryPath() {
        return this.path;
    }

    @Override
    public void registerCommon() {
        this.factory(ApoliDataTypes.BIENTITY_ACTION).register(ApoliRegistries.BIENTITY_ACTION);
        this.factory(ApoliDataTypes.BLOCK_ACTION).register(ApoliRegistries.BLOCK_ACTION);
        this.factory(ApoliDataTypes.ENTITY_ACTION).register(ApoliRegistries.ENTITY_ACTION);
        this.factory(ApoliDataTypes.ITEM_ACTION).register(ApoliRegistries.ITEM_ACTION);
    }

}
