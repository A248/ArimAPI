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
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.junit.jupiter.api.Test;
import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.ext.gson.GsonConfigurationFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatMessageSerializerTest {

    private ConfigurationFactory<Config> factory() {
        ChatMessageSerializer serializer = new ChatMessageSerializer(PlainComponentSerializer.plain());
        ConfigurationOptions options = new ConfigurationOptions.Builder()
                .addSerialiser(serializer).build();
        return GsonConfigurationFactory.create(Config.class, options);
    }

    @Test
    public void deserialize() {
        Config config = factory().loadDefaults();
        assertEquals(Component.text("default text"), config.component());
    }

    @Test
    public void serialize() throws IOException {
        Config config = () -> Component.text("text");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        factory().write(config, output);
        assertEquals("""
                {
                  "component": "text"
                }""", output.toString(StandardCharsets.UTF_8));
    }

    public interface Config {

        @ConfDefault.DefaultString("default text")
        Component component();
    }
}
