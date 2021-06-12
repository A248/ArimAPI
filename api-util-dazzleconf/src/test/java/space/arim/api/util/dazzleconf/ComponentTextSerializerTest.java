/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
 *
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */

package space.arim.api.util.dazzleconf;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import space.arim.api.jsonchat.adventure.util.ComponentText;
import space.arim.api.jsonchat.adventure.util.TextGoal;
import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.ext.gson.GsonConfigurationFactory;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentTextSerializerTest {

    private ConfigurationFactory<Config> factory(Set<TextGoal> goals) {
        ConfigurationOptions options = new ConfigurationOptions.Builder()
                .addSerialiser(new ComponentSerializer())
                .addSerialiser(new ComponentTextSerializer(goals)).build();
        return GsonConfigurationFactory.create(Config.class, options);
    }

    @ParameterizedTest
    @ArgumentsSource(TextGoalsArgumentProvider.class)
    public void deserialize(Set<TextGoal> goals) {
        Config config = factory(goals).loadDefaults();
        assertEquals(ComponentText.create(Component.text("default text"), goals), config.componentText());
    }

    @ParameterizedTest
    @ArgumentsSource(TextGoalsArgumentProvider.class)
    public void serialize(Set<TextGoal> goals) throws IOException {
        Config config = () -> ComponentText.create(Component.text("text"), goals);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        factory(goals).write(config, output);
        assertEquals("""
                {
                  "componentText": "text"
                }""", output.toString(StandardCharsets.UTF_8));
    }

    public interface Config {

        @ConfDefault.DefaultString("default text")
        ComponentText componentText();
    }

    private static final class ComponentSerializer implements ValueSerialiser<Component> {

        @Override
        public Class<Component> getTargetClass() {
            return Component.class;
        }

        @Override
        public Component deserialise(FlexibleType flexibleType) throws BadValueException {
            return Component.text(flexibleType.getString());
        }

        @Override
        public Object serialise(Component value, Decomposer decomposer) {
            return ((TextComponent) value).content();
        }
    }
}
