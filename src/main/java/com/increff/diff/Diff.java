/*
 * Copyright (c) 2021. Increff
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.increff.diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

/**
 * Allows the comparison of directories and files within the directories.
 * Can be used to indicate differences in files with same file names in different directories
 */
public class Diff {
	private static final Logger logger = Logger.getLogger(Diff.class);

	/**
	 * Executes the diff utility. Requires two run time arguments, representing the directories to be processed
	 * @param args Requires 2 arguments. Each should represent a valid directory
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			logger.error("Please provide two arguments, both of with should be valid directories");
			System.exit(1);
		}
		File dir1 = new File(args[0]);
		File dir2 = new File(args[1]);
		compareDirectories(dir1, dir2);
	}

	/**
	 * Compares two directories. Logs warning if files in directories are different
	 * For common files, runs the process() function to determine differences individually
	 * @param d1 File object for first directory
	 * @param d2 File object for second directory
	 * @return true if common files have similar content, false otherwise
	 */
	public static boolean compareDirectories(File d1, File d2) throws IOException {
		logger.info("Comparing directories " + d1 + ", " + d2);
		if (!d1.isDirectory() || !d2.isDirectory()) {
			throw new IOException("Given files must be directories, d1: " + d1 + ", d2: " + d2);
		}

		List<File> d1Files = Arrays.asList(d1.listFiles());
		List<File> d2Files = Arrays.asList(d2.listFiles());
		Set<String> set1 = d1Files.stream().map(o -> o.getName()).collect(Collectors.toSet());
		Set<String> set2 = d2Files.stream().map(o -> o.getName()).collect(Collectors.toSet());

		boolean equals = true;
		if (!set1.equals(set2)) {
			logger.warn("Directories have different files (count or name)");
		}

		// Get Common files
		Set<String> commonFiles = new HashSet<>(set1);
		commonFiles.retainAll(set2);

		// Iterate over common files
		for (String s : commonFiles) {
			File f1 = new File(d1, s);
			File f2 = new File(d2, s);
			equals = compareFiles(f1, f2) && equals;
		}
		return equals;
	}

	/**
	 * Compares Files by creating and then comparing a Set of lines in the two files
	 * NOTE: Since Set of lines is being compared, repetitions are ignored
	 * @param f1 First File Object
	 * @param f2 Second File Object
	 * @return true if Set of lines in both files is similar
	 */
	public static boolean compareFiles(File f1, File f2) throws IOException {
		logger.info("Comparing " + f1 + ", " + f2);
		Set<String> set1 = toSet(f1);
		Set<String> set2 = toSet(f2);
		Set<String> s1s2 = compare(set1, set2);
		Set<String> s2s1 = compare(set2, set1);
		if (s1s2.size() == 0 && s2s1.size() == 0) {
			logger.info("Files are similar");
			return true;
		}
		logger.warn("Files are different");

		// Uncomment these lines to display the diff
		//printDiff(s1s2, "<<< ");
		//printDiff(s2s1, ">>> ");
		return false;
	}

	public static void printDiff(Set<String> diff, String prefix) {
		diff.forEach(entry -> {
			logger.warn(prefix + entry);
		});
	}

	/**
	 * Creates a Set of lines by splitting each line in the file and storing it in a Set
	 * @param f File Object to be converted into Set of lines
	 * @return Set containing all lines in the File
	 */
	public static Set<String> toSet(File f) throws IOException {
		Set<String> set = new HashSet<>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = null;
		while ((line = reader.readLine()) != null) {
			set.add(line.trim());
		}
		reader.close();
		return set;
	}

	/**
	 * Compares if a set of strings is a subset of a different set of strings.
	 * @param s1 The set which needs to be checked as a subset
	 * @param s2 The set which needs to be checked as a superset
	 * @return A set of strings which are present in s1 but not in s2
	 */
	public static Set<String> compare(Set<String> s1, Set<String> s2) {
		Set<String> diff = new HashSet<>();
		s1.forEach(entry -> {
			if (!s2.contains(entry)) {
				diff.add(entry);
			}
		});
		return diff;
	}
}
