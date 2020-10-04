/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.realexecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

final class ExecutorReflector {

	private ExecutorReflector() {}
	
	static Executor handle(Consumer<Exception> exceptionHandler, Exception ex) {
		if (exceptionHandler != null) {
			exceptionHandler.accept(ex);
		}
		return null;
	}
	
	/*
	 * It's a wonder everyone names their field "executor".
	 */
	static Executor getFieldNamedExecutor0(Object scheduler)
			throws NoSuchFieldException, SecurityException, IllegalAccessException, ClassCastException {
		Field executorField = scheduler.getClass().getDeclaredField("executor"); // NoSuchFieldException, SecurityException
		executorField.setAccessible(true);
		Object result = executorField.get(scheduler); // IllegalAccessException
		return (Executor) result; // ClassCastException
	}
	
	private static Executor getFieldNamedExecutor(Object scheduler, Consumer<Exception> exceptionHandler) {
		try {
			return getFieldNamedExecutor0(scheduler);

		} catch (NoSuchFieldException | SecurityException | IllegalAccessException | InaccessibleObjectException | ClassCastException ex) {
			return handle(exceptionHandler, ex);
		}
	}
	
	/*
	 * It's a further wonder how 2 fields are both named "asyncScheduler"
	 */
	static Executor getFieldNamedAsyncSchedulerThenGetFieldNamedExecutorFromThat(Object scheduler, Consumer<Exception> exceptionHandler) {
		Object asyncScheduler;
		try {
			Field asyncSchedulerField = scheduler.getClass().getDeclaredField("asyncScheduler");
			asyncSchedulerField.setAccessible(true);
			asyncScheduler = asyncSchedulerField.get(scheduler);

		} catch (NoSuchFieldException | SecurityException | IllegalAccessException | InaccessibleObjectException ex) {
			return handle(exceptionHandler, ex);
		}
		return getFieldNamedExecutor(asyncScheduler, exceptionHandler);
	}
	
}
