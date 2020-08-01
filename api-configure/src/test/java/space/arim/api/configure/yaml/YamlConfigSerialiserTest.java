/* 
 * ArimAPI-configure
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-configure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-configure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-configure. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure.yaml;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigReadResult;
import space.arim.api.configure.ConfigTestingHelper;
import space.arim.api.configure.ConfigWriteResult;
import space.arim.api.configure.JarResources;
import space.arim.api.configure.impl.SimpleConfigData;

public class YamlConfigSerialiserTest {

	@TempDir
	public Path tempDir;
	
	private Path configDest;
	private YamlConfigSerialiser serialiser;
	
	private static final Map<String, Object> VALUES = Map.of(
			"enable", true,
			"integer-list", List.of(1, 5, 4, 9),
			"nesting", Map.of("firstvalue", true, "more", Map.of("message-also", "another one", "variable", 5)),
			"message", "Hi",
			"config-ver", 2
			);
	private static final List<ConfigComment> COMMENTS = List.of(
			new ConfigComment(1, " Config comment headers"),
			new ConfigComment(2, "second line"),
			new ConfigComment(0, " and a third line"));
	
	private static final ConfigData CONFIG_DATA = new SimpleConfigData(VALUES, COMMENTS);
	
	@BeforeEach
	public void setup() {
		configDest = tempDir.resolve("config.yml");
		
		serialiser = new YamlConfigSerialiser(Runnable::run);
	}
	
	private ConfigReadResult readOrFail() {
		ConfigReadResult readResult = serialiser.readConfig(configDest, List.of()).join();
		ConfigTestingHelper.assertSuccess(readResult);
		return readResult;
	}
	
	@Test
	public void testRead() {
		Path resource = JarResources.forCallerClass("config.yml");
		ConfigTestingHelper.copyOrFail(resource, configDest);
		assertEquals(CONFIG_DATA, readOrFail().getReadData());
	}
	
	@Test
	public void testWrite() {
		ConfigWriteResult writeResult = serialiser.writeConfig(configDest, CONFIG_DATA).join();
		ConfigTestingHelper.assertSuccess(writeResult);
		assertEquals(CONFIG_DATA, readOrFail().getReadData());
	}
	
}
