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
package space.arim.api.configure.configs;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import space.arim.api.configure.ConfigAccessor;
import space.arim.api.configure.ConfigTestingHelper;
import space.arim.api.configure.Configuration;
import space.arim.api.configure.DefaultResourceProvider;
import space.arim.api.configure.JarResources;
import space.arim.api.configure.yaml.YamlConfigSerialiser;

public abstract class ConfigImplTest {

	@TempDir
	public Path dir;
	
	private Path configPath;
	
	private Configuration config;
	
	@BeforeEach
	public void setup() {
		configPath = dir.resolve("config.yml");
		DefaultResourceProvider defaultResourcePath = JarResources.forCallerClass("config.yml");
		config = createInstance(new ConfigurationBuilder().executor(Runnable::run).defaultResource(defaultResourcePath)
				.serialiser(new YamlConfigSerialiser())).join();
	}
	
	protected abstract CompletableFuture<? extends Configuration> createInstance(ConfigurationBuilder builder);
	
	@Test
	public void testCopy() {
		ConfigTestingHelper.assertSuccess(config.saveDefaultConfig(configPath).join());
	}
	
	@Test
	public void testBasicValues() {
		ConfigTestingHelper.assertSuccess(config.saveDefaultConfig(configPath).join());
		ConfigTestingHelper.assertSuccess(config.readConfig(configPath).join());
		ConfigAccessor accessor = config.getAccessor();

		Map<String, Object> veryNestedMap = Map.of("message-also", "another one", "variable", 5);
		Map<String, Object> nestedMap = Map.of("firstvalue", true, "more", veryNestedMap);
		assertEquals(true, accessor.getBoolean("enable"));
		assertEquals(nestedMap, accessor.getObject("nesting", Map.class));
		assertEquals(veryNestedMap, accessor.getObject("nesting.more", Map.class));
	}
	
	@Test
	public void testConfigureValues() {
		ConfigTestingHelper.copyOrFail(JarResources.forCallerClass("configured.yml"), configPath);
		ConfigTestingHelper.assertSuccess(config.readConfig(configPath).join());
		ConfigAccessor accessor = config.getAccessor();

		int configVersion = accessor.getInteger("config-ver");
		assertEquals(3, configVersion, "Should use configured config version");
		List<Integer> list = List.of(1, 5, 4, 9);
		assertEquals(list, accessor.getIntegerList("integer-list"), "Unconfigured list should fallback to default");
	}
	
	@Test
	public void testTypeMismatch() {
		ConfigTestingHelper.copyOrFail(JarResources.forCallerClass("configured.yml"), configPath);
		ConfigTestingHelper.assertSuccess(config.readConfig(configPath).join());
		int variable = config.getAccessor().getInteger("nesting.more.variable");
		assertEquals(5, variable, "Should fall back to default value in case of a type mismatch");
	}
	
}
