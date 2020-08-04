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
package space.arim.api.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A simple implementation of <code>java.util.logging.Formatter</code> based on BungeeCord's <code>ConciseFormatter</code>.
 * 
 * @author A248
 *
 * @deprecated This class provides little value. Using JUL is not recommended. Also, until recently,
 * this class contained a thread safety issue with {@code SimpleDateFormat} (as does currently BungeeCord's);
 * to solve this, potentially inefficient synchronisation is used.
 */
@Deprecated
public class ConciseLogFormatter extends Formatter {
	
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
	
	@Override
	public String format(LogRecord record) {
		String format;
		synchronized (dateFormatter) {
			format = dateFormatter.format(record.getMillis());
		}
		StringBuilder builder = new StringBuilder(format);
		builder.append(" [").append(record.getLevel().getLocalizedName()).append("] ").append(formatMessage(record)).append('\n');
		if (record.getThrown() != null) {
			StringWriter writer = new StringWriter();
			record.getThrown().printStackTrace(new PrintWriter(writer));
			builder.append(writer);
		}
		return builder.toString();
	}
	
}
