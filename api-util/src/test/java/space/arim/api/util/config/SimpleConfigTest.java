/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SimpleConfigTest {
	
	@TempDir
	public File tempDir;
	private File configFile;
	private Config config;
	
	@BeforeEach
	public void setup() {
		configFile = new File(tempDir, "config.yml");
		config = new SimpleConfig(configFile) {};
	}
	
	@Test
	public void testNestedValues() {
		config.saveDefaultConfig();
		config.reloadConfig();
		Map<String, Object> veryNestedMap = Map.of("message-also", "another one", "variable", 5);
		Map<String, Object> nestedMap = Map.of("firstvalue", true, "more", veryNestedMap);
		Map<String, Object> bigMap = Map.of("enable", true, "integer-list", List.of(1, 2, 3, 4), "nesting",
				nestedMap);
		assertTrue(Map.class.isInstance(config.getObject("nesting.more", Object.class)), "Nested maps should always be maps!");

		assertEquals(true, SimpleConfig.getFromNestedMap(bigMap, "enable", Object.class));
		assertEquals(true, SimpleConfig.getFromNestedMap(bigMap, "enable", Boolean.class));
		assertEquals(nestedMap, SimpleConfig.getFromNestedMap(bigMap, "nesting", Object.class));
		assertEquals(nestedMap, SimpleConfig.getFromNestedMap(bigMap, "nesting", Map.class));
		assertEquals(veryNestedMap, SimpleConfig.getFromNestedMap(bigMap, "nesting.more", Object.class));
		assertEquals(veryNestedMap, SimpleConfig.getFromNestedMap(bigMap, "nesting.more", Map.class));
	}
	
	@Test
	public void testGetKeys() {
		Set<String> keys = Set.of("enable", "integer-list", "nesting", "message", "config-version");
		Set<String> subKeys = Set.of("firstvalue", "more");
		assertEquals(keys, config.getDefaultKeys());
		assertEquals(subKeys, config.getDefaultKeys("nesting"));
		config.saveDefaultConfig();
		try {
			Set<String> configuredKeys = config.getConfiguredKeys();
			fail("Configured keys should not be possible to get: " + configuredKeys);
		} catch (IllegalStateException expected) {
			
		}
		config.reloadConfig();
		assertEquals(keys, config.getConfiguredKeys());
		assertEquals(subKeys, config.getConfiguredKeys("nesting"));
	}
	
	@Test
	public void testConfiguredValues() {
		config.saveDefaultConfig();
		assertTrue(configFile.exists(), "Config file should exist after Config#saveDefaultConfig");
		config.reloadConfig();

		assertTrue(configFile.delete(), "Should be able to delete the config file since it already exists");
		copyFromJar("configured.yml", configFile);
		config.reloadConfig();
		int configVersion = config.getInteger("config-version");
		int defaultConfigVersion = config.getDefaultObject("config-version", Integer.class);
		int configuredConfigVersion = config.getConfiguredObject("config-version", Integer.class);
		assertEquals(3, configVersion, "Should use configured config version");
		assertEquals(3, configuredConfigVersion, "Configured config version is 3, see configured.yml");
		assertEquals(1, defaultConfigVersion, "Default config version is 1, see config.yml");
		List<Integer> list = List.of(1, 2, 3, 4);
		assertEquals(list, config.getIntegerList("integer-list"));
		assertNull(config.getConfiguredObject("integer-list", List.class), "Unconfigured list should be null");
		assertEquals(list, config.getDefaultObject("integer-list", List.class));
	}
	
	void copyFromJar(String resource, File configFile) {
		try (InputStream is = getClass().getResourceAsStream(File.separatorChar + resource);
				OutputStream os = new FileOutputStream(configFile)) {
			is.transferTo(os);
		} catch (IOException ex) {
			fail(ex);
		}
	}
	
	@Test
	public void testTypeMismatch() {
		copyFromJar("configured.yml", configFile);
		config.reloadConfig();
		assertTrue(configFile.exists(), "Config file must have existed after successful reload");
		int variable = config.getInteger("nesting.more.variable");
		assertEquals(5, variable, "Should fall back to default value in case of a type mismatch");
		assertEquals(5, config.getDefaultObject("nesting.more.variable", Integer.class));
		Object variableAsObj = config.getObject("nesting.more.variable", Object.class);
		Object defVariableAsObj = config.getDefaultObject("nesting.more.variable", Object.class);
		assertNotEquals(variableAsObj, defVariableAsObj, "Configured option should be different from non-configured");
	}
	
}
