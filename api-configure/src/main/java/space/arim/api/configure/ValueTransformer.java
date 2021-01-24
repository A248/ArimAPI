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
package space.arim.api.configure;

/**
 * A transformer applied to all configuration values uploading loading such values. <br>
 * <br>
 * Transformers should be stateless and thread safe.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.configure} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface ValueTransformer {

	/**
	 * Rewrites the object at the specified key path to another object. <br>
	 * <br>
	 * If this value transformer need not apply to the specific object at this key path,
	 * it should simply return the same {@code value} parameter. <br>
	 * <br>
	 * If this transformer returns {@code null}, the entry is immediately removed and
	 * no other transformers are invoked.
	 * 
	 * @param key the full key path at which this object occurs, never {@code null}
	 * @param value the object to rewrite, never {@code null}
	 * @return the new object, or {@code value} to make no changes, or {@code null} to remove the entry
	 */
	Object transform(String key, Object value);
	
}
