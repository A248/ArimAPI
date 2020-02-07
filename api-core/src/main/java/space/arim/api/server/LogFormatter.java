/* 
 * UniversalUtil, simple utilities for Spigot and BungeeCord
 * Copyright © 2019 Anand Beh <https://www.arim.space>
 * 
 * UniversalUtil is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UniversalUtil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UniversalUtil. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A simple default implementation for {@link Formatter}.
 * See {@link #format(LogRecord)} for details.
 * 
 * @author A248
 *
 */
public class LogFormatter extends Formatter {
	
	private final DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	/**
	 * Formats a log record. <br>
	 * <br>
	 * Uses the following pseudo format: <br>
	 * <code>dd/MM/yyyy HH:mm:ss [record.getLevel().getLocalizedName()] formatMessage(record)</code>
	 * 
	 * @return the formatted record
	 */
	@Override
	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder();
		builder.append(date.format(record.getMillis()));
		builder.append(" [");
		builder.append(record.getLevel().getLocalizedName());
		builder.append("] ");
		builder.append(formatMessage(record));
		builder.append('\n');
		if (record.getThrown() != null) {
			StringWriter writer = new StringWriter();
			record.getThrown().printStackTrace(new PrintWriter(writer));
			builder.append(writer);
		}
		return builder.toString();
	}
	
}
