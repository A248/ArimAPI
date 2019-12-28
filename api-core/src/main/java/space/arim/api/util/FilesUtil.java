/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Files utility.
 * 
 * @author A248
 *
 */
public final class FilesUtil {

	private FilesUtil() {}
	
	/**
	 * Utilises class <code>com.google.common.io.ByteStreams</code> <br>
	 * <br>
	 * <b>If that class is not on the classpath do not call this method!</b>
	 * 
	 * @param target - the file to save to
	 * @param input - the source from which to save. Use <code>YourClass.class.getResourceAsStream(File.separator + "config.yml")</code>
	 * @return true if the saving was successful
	 * @throws IOException if an IO error occurred
	 */
	public static boolean saveFromStream(File target, InputStream input) throws IOException {
		if ((target.getParentFile().exists() || target.getParentFile().mkdirs()) && target.createNewFile()) {
			try (FileOutputStream output = new FileOutputStream(target)){
				com.google.common.io.ByteStreams.copy(input, output);
				return true;
			}
		}
		return false;
	}
	
	private static void ensureDir(File folder) {
		if (!folder.exists() && !folder.mkdirs()) {
			throw new IllegalStateException("Directory creation of " + folder.getPath() + " failed.");
		} else if (!folder.isDirectory()) {
			throw new IllegalArgumentException(folder.getPath() + " is not a directory!");
		}
	}
	
	public static File dateSuffixedFile(File folder, String filename) {
		ensureDir(folder);
		return new File(folder, filename + StringsUtil.basicTodaysDate());
	}
	
	public static File dateSuffixedFile(File folder, String filename, String subFolder) {
		ensureDir(folder);
		return new File(folder, (subFolder.endsWith(File.separator)) ? subFolder : (subFolder + File.separator) + filename + StringsUtil.basicTodaysDate());
	}
	
	public static File datePrefixedFile(File folder, String filename) {
		ensureDir(folder);
		return new File(folder, StringsUtil.basicTodaysDate() + filename);
	}
	
	public static File datePrefixedFile(File folder, String filename, String subFolder) {
		ensureDir(folder);
		return new File(folder, (subFolder.startsWith(File.separator) ? subFolder : File.separator + subFolder) + StringsUtil.basicTodaysDate() + filename);
	}
	
	public static boolean generateBlankFile(File file) {
		if (file.exists() && file.canRead() && file.canWrite()) {
			return true;
		} else if (file.exists()) {
			file.delete();
		}
		try {
			return file.createNewFile();
		} catch (IOException ex) {
			return false;
		}
	}
	
}
