/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.concurrent;

import space.arim.universal.registry.Registrable;

/**
 * A complement to {@link AsyncExecutor} designed to provide: <br>
 * * Resynchronisation (execution on the main thread) via the specifications of {@link BasicExecutor}.
 * If the application has no main thread, concurrent execution is an acceptable implementation.<br>
 * * Task scheduling according to superinterface {@link Scheduler}.
 * 
 * @author A248
 *
 */
public interface SyncExecutor extends Registrable, BasicExecutor, Scheduler {

}
