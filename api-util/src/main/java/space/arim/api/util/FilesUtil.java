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
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Files utility.
 * 
 * @author A248
 *
 * @deprecated Not valuable enough to maintain. Utilities in this class are easily recreatable where needed.
 * It is also recommended to switch to NIO which is far more readable and capable.
 */
@Deprecated(forRemoval = true)
public final class FilesUtil {

	private FilesUtil() {}
	
	/**
	 * Copies the input stream to a file. <br>
	 * <br>
	 * <i>Note: Thise method used to require Guava. Not anymore.</i>
	 * 
	 * @param target the file to save to
	 * @param input the source from which to save
	 * @return true if the saving was successful
	 * @throws IOException if an IO error occurred
	 */
	public static boolean saveFromStream(File target, InputStream input) throws IOException {
		if (makeDir(target.getParentFile())) {
			try (FileOutputStream output = new FileOutputStream(target)){
				input.transferTo(output);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Reads lines from a file easily.
	 * 
	 * @param file the file
	 * @param processor handle the lines
	 * @param exceptionHandler handle a possible IOException, can be <code>null</code>
	 * @return true if everything went successfully, false otherwise
	 */
	public static boolean readLines(File file, Consumer<String> processor, Consumer<IOException> exceptionHandler) {
		if (makeDir(file.getParentFile()) && file.exists()) {
			try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
				while (scanner.hasNextLine()) {
					processor.accept(scanner.nextLine());
				}
				return true;
			} catch (IOException ex) {
				if (exceptionHandler != null) {
					exceptionHandler.accept(ex);
				}
				return false;
			}
		}
		return true;
	}
	
	private static boolean makeDir(File folder) {
		return folder.isDirectory() || folder.mkdirs();
	}
	
	/**
	 * Checks if the specified File is a folder. <br>
	 * If it does not exist, the File is attempted to be created as a directory. <br>
	 * If the File is still not a folder, an unchecked exception is thrown.
	 * 
	 * @param folder the File to check
	 * @return the same file
	 */
	public static File requireDirectory(File folder) {
		if (!makeDir(folder)) {
			throw new IllegalStateException("Directory creation of " + folder.getPath() + " failed.");
		}
		return folder;
	}
	
	/**
	 * Deletes the file or directory according to {@link File#delete()}. <br>
	 * <br>
	 * If a directory, and not empty, its contents will first be deleted, and then it will be deleted. <br>
	 * Otherwise, there is no difference between this and <code>file.delete()</code>. <br>
	 * <br>
	 * <b>Useful because <code>file.delete()</code> requires the target directory to be empty if the file is a directory.</b>
	 * 
	 * @param file the file
	 * @return true if successful, false otherwise
	 */
	public static boolean delete(File file) {
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				if (!delete(subFile)) {
					return false;
				}
			}
		}
		return file.delete();
	}
	
	public static File dateSuffixedFile(File folder, String filename) {
		return new File(requireDirectory(folder), filename + StringsUtil.basicTodaysDate());
	}
	
	public static File dateSuffixedFile(File folder, String filename, String subFolder) {
		return new File(requireDirectory(folder), (subFolder.endsWith(File.separator)) ? subFolder : (subFolder + File.separator) + filename + StringsUtil.basicTodaysDate());
	}
	
	public static File datePrefixedFile(File folder, String filename) {
		return new File(requireDirectory(folder), StringsUtil.basicTodaysDate() + filename);
	}
	
	public static File datePrefixedFile(File folder, String filename, String subFolder) {
		return new File(requireDirectory(folder), (subFolder.startsWith(File.separator) ? subFolder : File.separator + subFolder) + StringsUtil.basicTodaysDate() + filename);
	}
	
	public static boolean generateBlankFile(File file) {
		requireDirectory(file.getParentFile());
		if (file.exists()) {
			return true;
		}
		try {
			return file.createNewFile();
		} catch (IOException ex) {
			return file.exists();
		}
	}
	
}
